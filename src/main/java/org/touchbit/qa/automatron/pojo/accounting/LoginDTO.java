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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashMap;
import java.util.Map;

import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.*;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@Schema(description = LOGIN_DTO_DESCRIPTION)
@ToString
public class LoginDTO extends POJO {

    @JsonIgnore
    private final Map<String, Object> additionalProperties = new HashMap<>();
    @JsonProperty("login")
    @Schema(description = LOGIN_DTO_ADMIN_DESCRIPTION, example = "admin")
    @NotNull
    @Size(min = 2, max = 20)
    private String login;
    @JsonProperty("password")
    @Schema(description = LOGIN_DTO_PASSWORD_DESCRIPTION, example = "IDDQD")
    @NotNull
    @Size(min = 5, max = 36)
    private String password;

}
