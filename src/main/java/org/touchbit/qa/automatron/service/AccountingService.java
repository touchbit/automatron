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

import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.I18N_ERROR_500_001_MESSAGE;

@Slf4j
@Service
@AllArgsConstructor
public class AccountingService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    @SuppressWarnings("unused")
    public AuthDTO authenticate(final String login, final String password) {
        log.info("Authentication Request Received");
        final User user = userRepository.findByLogin(login);
        if (user == null ||
            !Objects.equals(password, user.password()) ||
            user.status().equals(UserStatus.DELETED)) {
            throw AutomatronException.http401("login/password");
        }
        if (user.status().equals(UserStatus.BLOCKED)) {
            BugAdviser.addBug(Bug.BUG_0003);
            throw AutomatronException.http401(user.getClass().getSimpleName() + "{status=" + user.status() + "}");
        }
        if (user.status().equals(UserStatus.ACTIVE)) {
            final Session session = getSession(user);
            sessionRepository.save(session);
            return new AuthDTO()
                    .accessToken(session.accessToken())
                    .refreshToken(session.refreshToken())
                    .accessExpiresIn(session.accessExpiresIn())
                    .refreshExpiresIn(session.refreshExpiresIn())
                    .tokenType("bearer");
        }
        throw AutomatronException.http500(user.getClass().getSimpleName() + ".id=" + user.id(), I18N_ERROR_500_001_MESSAGE);
    }

    private Session getSession(final User user) {
        return new Session()
                .accessToken(UUID.randomUUID().toString())
                .refreshToken(UUID.randomUUID().toString())
                .accessExpiresIn(86400L)
                .refreshExpiresIn(86400L)
                .user(user);
    }

}
