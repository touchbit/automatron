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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.db.entity.PhoneNumber;
import org.touchbit.qa.automatron.db.entity.UserStatus;
import org.touchbit.qa.automatron.db.entity.UserType;

import java.util.Set;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168739660;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168744616;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Data
@Builder
public class UserDTO {

    @JsonProperty("id")
    @Schema(description = "TODO", required = true, example = "1", accessMode = READ_ONLY)
    private long id;

    @JsonProperty("login")
    @Parameter(name = "login", schema =
    @Schema(required = true, description = I18N_1648168739660, example = "admin"))
    private String login;

    @JsonProperty("password")
    @Parameter(
            name = "password",
            required = true,
            schema = @Schema(description = I18N_1648168744616, example = "example_password"))
    private String password;

    @JsonProperty("status")
    private UserStatus status;

    @JsonProperty("type")
    private UserType type;

    @JsonProperty("phones")
    private Set<PhoneNumber> phones;

}
