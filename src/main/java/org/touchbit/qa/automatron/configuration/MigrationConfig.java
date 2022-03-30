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
import org.touchbit.qa.automatron.constant.ConfigParameter;
import org.touchbit.qa.automatron.db.entity.Config;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.repository.ConfigurationRepository;
import org.touchbit.qa.automatron.db.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.touchbit.qa.automatron.db.entity.UserStatus.ACTIVE;
import static org.touchbit.qa.automatron.db.entity.UserType.ADMIN;
import static org.touchbit.qa.automatron.db.entity.UserType.OWNER;

@Slf4j
@Configuration
public class MigrationConfig {

    // looks like a piece of shit
    // but for this solution it's easier than 'flyway'
    @Bean
    public Void migrateUsers(UserRepository userRepository, ConfigurationRepository configRepository) {
        log.info("DB migrations");
        final Set<ConfigParameter> parameters = ConfigParameter.valuesSet();
        final Set<ConfigParameter> existsParams = configRepository.findAllById(parameters).stream()
                .map(Config::parameter)
                .collect(Collectors.toSet());
        parameters.removeAll(existsParams);
        final Set<Config> configs = parameters.stream()
                .map(p -> new Config()
                        .parameter(p)
                        .value(p.getDefaultValue())
                        .type(p.getType()))
                .collect(Collectors.toSet());
        configRepository.saveAll(configs);
        Stream.of(new User().login("automatron").password("IDDQD").status(ACTIVE).type(OWNER),
                        new User().login("admin").password("admin").status(ACTIVE).type(ADMIN))
                .filter(user -> !userRepository.existsByLogin(user.login()))
                .forEach(userRepository::save);
        return null;
    }

}
