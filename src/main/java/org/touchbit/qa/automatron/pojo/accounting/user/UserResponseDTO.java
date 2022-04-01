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

package org.touchbit.qa.automatron.pojo.accounting.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.constant.UserRole;
import org.touchbit.qa.automatron.constant.UserStatus;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168739660;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168744616;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDTO extends POJO {

    @JsonProperty("login")
    @Schema(description = I18N_1648168739660, example = "touchbit")
    private @NotNull @Size(min = 2, max = 20) String login;

    @JsonProperty("password")
    @Schema(description = I18N_1648168744616, example = "touchbit", hidden = true)
    private @NotNull @Size(min = 5, max = 36) String password;

    @JsonProperty("status")
    private @NotNull UserStatus status;

    @JsonProperty("role")
    private @NotNull UserRole role;

}
