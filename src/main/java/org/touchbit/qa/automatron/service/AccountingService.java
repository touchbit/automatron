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
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.constant.LogoutMode;
import org.touchbit.qa.automatron.constant.UserRole;
import org.touchbit.qa.automatron.constant.UserStatus;
import org.touchbit.qa.automatron.db.entity.Session;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.repository.SessionRepository;
import org.touchbit.qa.automatron.db.repository.UserRepository;
import org.touchbit.qa.automatron.pojo.accounting.LoginResponseDTO;
import org.touchbit.qa.automatron.pojo.accounting.UserRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.UserResponseDTO;
import org.touchbit.qa.automatron.resource.param.GetUserListQuery;
import org.touchbit.qa.automatron.resource.param.GetUserPath;
import org.touchbit.qa.automatron.resource.param.LogoutQueryParameters;
import org.touchbit.qa.automatron.util.AutomatronException;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.touchbit.qa.automatron.constant.Bug.*;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168178176;
import static org.touchbit.qa.automatron.constant.UserRole.ADMIN;
import static org.touchbit.qa.automatron.constant.UserRole.OWNER;
import static org.touchbit.qa.automatron.util.AutomatronUtils.errSource;

@Slf4j
@Service
@AllArgsConstructor
public class AccountingService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @SuppressWarnings("unused")
    public LoginResponseDTO authenticate(final String login, final String password) {
        log.debug("User authentication by login {}", login);
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
            Bug.register(Bug.BUG_0003);
            throw AutomatronException.http401(errSource(user, User::status, "status"));
        }
        if (user.status().equals(UserStatus.ACTIVE)) {
            final Session session = getSession(user);
            sessionRepository.save(session);
            return new LoginResponseDTO()
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

    public Session authorize(HttpHeaders headers) {
        log.debug("Authorization");
        final List<String> authorization = headers.get("Authorization");
        if (authorization == null || authorization.isEmpty()) {
            throw AutomatronException.http403("Header.Authorization");
        }
        final Session session = authorization.stream()
                .map(String::toLowerCase)
                .map(t -> t.replace("bearer ", ""))
                .map(this::dbFindSessionByAccessToken)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
        if (session == null) {
            throw AutomatronException.http403("Header.Authorization");
        }
        log.debug("Session found. User: {}", session.user().login());
        return session;
    }

    public void logout(Session session, LogoutQueryParameters logoutQueryParameters) {
        final String mode = logoutQueryParameters == null ? null : logoutQueryParameters.getMode();
        log.debug("Logout: login={}, mode={}", session.user().login(), mode);
        final LogoutMode logoutMode = Arrays.stream(LogoutMode.values())
                .filter(lm -> lm.name().equalsIgnoreCase(mode))
                .findAny()
                .orElse(null);
        if (mode == null || LogoutMode.CURRENT.equals(logoutMode)) {
            log.debug("Delete current session");
            dbDeleteSession(session);
            return;
        }
        log.debug("Delete all sessions");
        if (!LogoutMode.ALL.equals(logoutMode)) {
            Bug.register(BUG_0004);
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

    public List<UserResponseDTO> getUsers(GetUserListQuery filter) {
        log.debug("Get user list by filter: {}", filter);
        final List<User> users = dbFindAllByFilter(filter);
        log.debug("Found users: {}", users.size());
        return users.stream()
                .map(this::userToGetUserResponseDTO)
                .collect(Collectors.toList());
    }

    private List<User> dbFindAllByFilter(GetUserListQuery filter) {
        log.debug("DB: Search users by filter: {}", filter);
        // TODO Bug
        // the password must be stored encrypted
        // the password should under no circumstances be returned to the user
        return userRepository.findAllByFilter(filter.getLogin(), filter.getStatus(), filter.getRole());
    }

    public UserResponseDTO getUser(GetUserPath pathParameters) {
        final User user = dbFindUserByLogin(pathParameters.getLogin());
        if (user == null) {
            Bug.register(BUG_0005);
            Bug.register(BUG_0006);
            return null;
        }
        return userToGetUserResponseDTO(user);
    }

    private UserResponseDTO userToGetUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .login(user.login())
                .status(user.status())
                .role(user.role())
                .build();
    }

    public void authorizeAdmin(HttpHeaders headers) {
        final Session session = this.authorize(headers);
        final User user = session.user();
        final UserRole role = user.role();
        if (!ADMIN.equals(role) && !OWNER.equals(role)) {
            throw AutomatronException.http403(AutomatronUtils.errSource(user, role, "role"));
        }
    }

    public UserResponseDTO addNewUser(UserRequestDTO request) {
        log.debug("Creating a new user in the system with a login: {}", request.login());
        if (userRepository.existsById(request.login())) {
            log.error("User with login '{}' already exists", request.login());
            throw AutomatronException.http409(AutomatronUtils.errSource(request, UserRequestDTO::login, "login"));
        }
        final User savedUser = saveUser(request);
        Bug.register(BUG_0007);
        final UserResponseDTO result = new UserResponseDTO()
                .login(savedUser.login())
                .role(savedUser.role())
                .status(savedUser.status());
        log.debug("Response body is formed");
        return result;
    }

    private User saveUser(UserRequestDTO request) {
        log.debug("DB: save user: {}", request.login());
        final User user = new User().login(request.login())
                .password(request.password())
                .status(request.status())
                .role(request.role());
        try {
            final User result = userRepository.saveAndFlush(user);
            log.debug("DB: successfully saved");
            return result;
        } catch (Exception e) {
            final String msg = e.getMessage().toLowerCase();
            if (msg.contains("on public.user(password)") && msg.contains("constraint")) {
                Bug.register(BUG_0008);
            }
            throw e;
        }
    }

}
