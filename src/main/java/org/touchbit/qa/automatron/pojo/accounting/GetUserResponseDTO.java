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
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.constant.UserRole;
import org.touchbit.qa.automatron.constant.UserStatus;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168739660;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648678859481;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
public class GetUserResponseDTO extends POJO {

    @JsonProperty("login")
    @Schema(description = I18N_1648168739660, example = "touchbit")
    private @NotNull @Size(min = 2, max = 20) String login;

    @JsonProperty("status")
    private @NotNull UserStatus status;

    @JsonProperty("role")
    private @NotNull UserRole role;

    @JsonProperty("phones")
    @Schema(description = I18N_1648678859481)
    private @NotNull @Size(max = 2) Set<PhoneNumberDTO> phones;

}
