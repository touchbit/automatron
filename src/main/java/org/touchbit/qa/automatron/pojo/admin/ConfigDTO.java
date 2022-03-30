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

package org.touchbit.qa.automatron.pojo.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.constant.ConfigParameter;
import org.touchbit.qa.automatron.constant.ConfigParameterType;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@ToString
public class ConfigDTO {

    @JsonProperty("parameterName")
    @NotNull
    private ConfigParameter parameterName;

    @JsonProperty("parameterValue")
    @NotNull
    private String parameterValue;

    @JsonProperty("parameterType")
    @NotNull
    private ConfigParameterType parameterType;

    @JsonProperty("parameterValues")
    @NotNull
    private Set<String> parameterValues;

}
