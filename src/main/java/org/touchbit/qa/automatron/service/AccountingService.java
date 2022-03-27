/*
 * Copyright (c) 2022 Shaburov Oleg
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.touchbit.qa.automatron.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.constant.LogoutMode;
import org.touchbit.qa.automatron.db.entity.Session;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.entity.UserStatus;
import org.touchbit.qa.automatron.db.repository.SessionRepository;
import org.touchbit.qa.automatron.db.repository.UserRepository;
import org.touchbit.qa.automatron.interceptor.BugInterceptor;
import org.touchbit.qa.automatron.pojo.accounting.AuthDTO;
import org.touchbit.qa.automatron.pojo.accounting.UserDTO;
import org.touchbit.qa.automatron.resource.param.GetUserQueryParameters;
import org.touchbit.qa.automatron.util.AutomatronException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.touchbit.qa.automatron.constant.Bug.BUG_0004;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168178176;
import static org.touchbit.qa.automatron.util.AutomatronUtils.errSource;

@Slf4j
@Service
@AllArgsConstructor
public class AccountingService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @SuppressWarnings("unused")
    public AuthDTO authenticate(final String login, final String password) {
        final User user = dbFindUserByLogin(login);
        final String source401 = "login/password";
        if (user == null) {
            log.error("User with login '{}' not found", login);
            throw AutomatronException.http401(source401);
        }
        if (!Objects.equals(password, user.password())) {
            log.error("Incorrect password received for login '{}'", login);
            throw AutomatronException.http401(source401);
        }
        if (user.status().equals(UserStatus.DELETED)) {
            log.error("Authentication is denied. The user status: {}", UserStatus.DELETED);
            throw AutomatronException.http403(errSource(user, User::status, "status"));
        }
        if (user.status().equals(UserStatus.BLOCKED)) {
            log.error("Authentication is denied. User status: {}", UserStatus.BLOCKED);
            BugInterceptor.addBug(Bug.BUG_0003);
            throw AutomatronException.http401(errSource(user, User::status, "status"));
        }
        if (user.status().equals(UserStatus.ACTIVE)) {
            final Session session = getSession(user);
            sessionRepository.save(session);
            log.info("Authentication successful");
            return new AuthDTO()
                    .accessToken(session.accessToken())
                    .refreshToken(session.refreshToken())
                    .accessExpiresIn(session.accessExpiresIn())
                    .refreshExpiresIn(session.refreshExpiresIn())
                    .tokenType("bearer");
        }
        throw AutomatronException.http500(errSource(user, User::status, "status"), I18N_1648168178176);
    }

    private Session getSession(final User user) {
        return new Session()
                .accessToken(UUID.randomUUID().toString())
                .refreshToken(UUID.randomUUID().toString())
                .accessExpiresIn(86400L)
                .refreshExpiresIn(86400L)
                .user(user);
    }

    private User dbFindUserByLogin(String login) {
        log.debug("DB: search user by login: {}", login);
        final User user = userRepository.findByLogin(login);
        if (user != null) {
            log.debug("DB: user found");
        } else {
            log.debug("DB: user is not found");
        }
        return user;
    }

    public void logout(String bearerAuthorizationHeaderValue, String mode) {
        if (bearerAuthorizationHeaderValue == null) {
            throw AutomatronException.http403("Authorization header");
        }
        final String accessToken = bearerAuthorizationHeaderValue.toLowerCase().replace("bearer ", "");
        final Session session = dbFindSessionByAccessToken(accessToken);
        if (session == null) {
            log.debug("There is no session with the received access token.");
            return;
        }
        final LogoutMode logoutMode = Arrays.stream(LogoutMode.values())
                .filter(lm -> lm.name().equalsIgnoreCase(mode))
                .findAny()
                .orElse(null);
        if (mode == null || LogoutMode.CURRENT.equals(logoutMode)) {
            dbDeleteSession(session);
            return;
        }
        if (!LogoutMode.ALL.equals(logoutMode)) {
            BugInterceptor.addBug(BUG_0004);
        }
        dbDeleteSessionByUser(session.user());
    }

    @Nullable
    private Session dbFindSessionByAccessToken(final String accessToken) {
        log.debug("DB: search session by access token");
        final Session session = sessionRepository.findSessionByAccessToken(accessToken);
        if (session != null) {
            log.debug("DB: session found");
        } else {
            log.debug("DB: session not found");
        }
        return session;
    }

    private void dbDeleteSession(final Session session) {
        log.debug("DB: delete session");
        if (session == null) {
            log.debug("DB: session is null. There is nothing to delete.");
        } else {
            sessionRepository.delete(session);
            log.debug("DB: session successfully deleted");
        }
    }

    private void dbDeleteSessionByUser(final User user) {
        log.debug("DB: delete user sessions");
        if (user == null) {
            log.debug("DB: user is null. There is nothing to delete.");
        } else {
            final int count = sessionRepository.deleteAllByUser(user);
            log.debug("DB: successfully deleted user sessions: {}", count);
        }
    }

    public List<UserDTO> getUsers(GetUserQueryParameters filter) {
        final List<User> users = dbFindAllByFilter(filter);
        log.debug("Found users: {}", users.size());
        return users.stream()
                .map(this::userToUserDTO)
                .collect(Collectors.toList());
    }

    private List<User> dbFindAllByFilter(GetUserQueryParameters filter) {
        log.debug("DB: Search users by filter: {}", filter);
        // TODO Bug return self password for Admin / Owner
        // the password must be stored encrypted
        // the password should under no circumstances be returned to the user
        return userRepository.findAllByFilter(filter.getId(), filter.getLogin(), filter.getStatus(), filter.getType());
    }

    private UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.id())
                .login(user.login())
                .status(user.status())
                .type(user.type())
                .phones(user.phones())
                .build();
    }

}
