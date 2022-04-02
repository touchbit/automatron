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
                        .parameterName(config.parameter())
                        .parameterType(config.type())
                        .parameterValue(config.value())
                        .parameterValues(config.parameter().getPossibleValues()))
                .collect(Collectors.toSet());
    }

    public void updateConfig(ConfigDTO dto) {
        repository.saveAndFlush(new Config()
                .parameter(dto.parameterName())
                .value(dto.parameterValue()));
    }

    public Locale getDefaultLocale() {
        final Config config = repository.findById(ConfigParameter.DEFAULT_LANGUAGE).orElse(null);
        if (config == null) {
            return Locale.getDefault();
        }
        return Locale.forLanguageTag(config.value());
    }

    public boolean isGlobalRequestIdHeaderEnabled() {
        final Config config = repository.findById(ConfigParameter.ENABLE_DEFAULT_REQUEST_ID_HEADER).orElse(null);
        final boolean result;
        if (config != null) {
            result = Boolean.parseBoolean(config.value());
        } else {
            result = Boolean.parseBoolean(ConfigParameter.ENABLE_DEFAULT_REQUEST_ID_HEADER.getDefaultValue());
        }
        return result;
    }

    public boolean isGlobalLocaleHeaderEnabled() {
        final Config config = repository.findById(ConfigParameter.ENABLE_DEFAULT_LOCALE_HEADER).orElse(null);
        final boolean result;
        if (config != null) {
            result = Boolean.parseBoolean(config.value());
        } else {
            result = Boolean.parseBoolean(ConfigParameter.ENABLE_DEFAULT_LOCALE_HEADER.getDefaultValue());
        }
        return result;
    }

    public boolean isGlobal5xxResponseEnabled() {
        final Config config = repository.findById(ConfigParameter.ENABLE_DEFAULT_5_XX_RESPONSE).orElse(null);
        final boolean result;
        if (config != null) {
            result = Boolean.parseBoolean(config.value());
        } else {
            result = Boolean.parseBoolean(ConfigParameter.ENABLE_DEFAULT_5_XX_RESPONSE.getDefaultValue());
        }
        return result;
    }

    public boolean isGlobalAuthorizationHeaderEnabled() {
        final Config config = repository.findById(ConfigParameter.ENABLE_DEFAULT_AUTHORIZATION_HEADER).orElse(null);
        final boolean result;
        if (config != null) {
            result = Boolean.parseBoolean(config.value());
        } else {
            result = Boolean.parseBoolean(ConfigParameter.ENABLE_DEFAULT_AUTHORIZATION_HEADER.getDefaultValue());
        }
        return result;
    }

}
