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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.touchbit.qa.automatron.constant.ConfigParameter;
import org.touchbit.qa.automatron.db.entity.Config;
import org.touchbit.qa.automatron.db.entity.ConfigParamValues;
import org.touchbit.qa.automatron.db.repository.ConfigurationRepository;
import org.touchbit.qa.automatron.pojo.admin.ConfigDTO;

import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public record ConfigService(ConfigurationRepository repository) {

    public Set<ConfigDTO> getConfiguration() {
        return repository.findAll().stream()
                .map(config -> new ConfigDTO()
                        .parameter(config.parameter())
                        .type(config.type())
                        .value(config.value())
                        .values(config.values().stream()
                                .map(ConfigParamValues::value)
                                .collect(Collectors.toSet())))
                .collect(Collectors.toSet());
    }

    public void updateConfig(ConfigDTO dto) {
        repository.saveAndFlush(new Config()
                .parameter(dto.parameter())
                .value(dto.value()));
    }

    public Locale getDefaultLocale() {
        final Config config = repository.findById(ConfigParameter.DEFAULT_LANGUAGE).orElse(null);
        if (config == null) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(config.value());
    }

}
