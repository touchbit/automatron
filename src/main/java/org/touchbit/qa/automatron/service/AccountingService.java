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
import org.springframework.stereotype.Service;
import org.touchbit.qa.automatron.advice.BugAdviser;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.db.entity.Session;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.entity.UserStatus;
import org.touchbit.qa.automatron.db.repository.SessionRepository;
import org.touchbit.qa.automatron.db.repository.UserRepository;
import org.touchbit.qa.automatron.pojo.accounting.AuthDTO;
import org.touchbit.qa.automatron.util.AutomatronException;

import java.util.Objects;
import java.util.UUID;

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
        log.info("User authentication request with login {}", login);
        final User user = findUserByLogin(login);
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
            BugAdviser.addBug(Bug.BUG_0003);
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

    private User findUserByLogin(String login) {
        log.debug("DB: search user by login: {}", login);
        final User user = userRepository.findByLogin(login);
        if (user != null) {
            log.debug("DB: user found");
        } else {
            log.debug("DB: user is not found");
        }
        return user;
    }

}
