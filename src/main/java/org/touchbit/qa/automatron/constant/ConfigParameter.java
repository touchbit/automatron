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

package org.touchbit.qa.automatron.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static org.touchbit.qa.automatron.constant.ConfigParameterType.SWITCH;

@Getter
public enum ConfigParameter {

    ENABLE_DEFAULT_5_XX_RESPONSE(Boolean.TRUE.toString(), SWITCH, Set.of(Boolean.TRUE.toString(), Boolean.FALSE.toString())),
    ENABLE_DEFAULT_LOCALE_HEADER(Boolean.TRUE.toString(), SWITCH, Set.of(Boolean.TRUE.toString(), Boolean.FALSE.toString())),
    ENABLE_DEFAULT_REQUEST_ID_HEADER(Boolean.TRUE.toString(), SWITCH, Set.of(Boolean.TRUE.toString(), Boolean.FALSE.toString())),
    DEFAULT_LANGUAGE("EN", SWITCH, Set.of("RU", "EN")),
    ;

    private final String defaultValue;
    private final ConfigParameterType type;
    private final Set<String> possibleValues;

    ConfigParameter(String defaultValue, ConfigParameterType type, Set<String> possibleValues) {
        this.defaultValue = defaultValue;
        this.type = type;
        this.possibleValues = possibleValues;
    }

    public static Set<ConfigParameter> valuesSet() {
        return Arrays.stream(ConfigParameter.values()).collect(Collectors.toSet());
    }

}
