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

package org.touchbit.qa.automatron.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.repository.UserRepository;

import static org.touchbit.qa.automatron.db.entity.UserStatus.ACTIVE;
import static org.touchbit.qa.automatron.db.entity.UserType.OWNER;

@Slf4j
@Configuration
public class MigrationConfig {

    // looks like a piece of shit
    // but for this solution it's easier than 'flyway'
    @Bean
    public Void migrate(UserRepository userRepository) {
        log.info("DB migrations");
        userRepository.save(new User().login("admin").password("IDDQD").status(ACTIVE).type(OWNER));
        return null;
    }

}
