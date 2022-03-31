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

package org.touchbit.qa.automatron.pojo.accounting;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168739660;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168744616;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@ToString
public class LoginRequestDTO extends POJO {

    @JsonProperty("login")
    @NotNull
    @Size(min = 5, max = 25)
    @Schema(description = I18N_1648168739660, example = "admin")
    private String login;

    @JsonProperty("password")
    @NotNull
    @Size(min = 5, max = 25)
    @Schema(description = I18N_1648168744616, example = "admin")
    private String password;

}
