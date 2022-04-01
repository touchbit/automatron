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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static org.touchbit.qa.automatron.constant.I18N.*;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponseDTO extends POJO {

    @JsonProperty("access_token")
    @NotNull
    @Schema(description = I18N_1648700259699, example = "d0b8b863-3f47-4881-96b8-68c4b4e4f892")
    private String accessToken;

    @JsonProperty("access_expires_in")
    @Min(60)
    @Max(86400)
    @NotNull
    @Schema(description = I18N_1648700334011, example = "86400")
    private Long accessExpiresIn;

    @JsonProperty("refresh_token")
    @NotNull
    @Schema(description = I18N_1648700575095, example = "bfb35bf5-311b-4e77-8b04-82582cc5a5f2")
    private String refreshToken;

    @JsonProperty("refresh_expires_in")
    @Min(86400)
    @Max(604800)
    @NotNull
    @Schema(description = I18N_1648700640862, example = "86400")
    private Long refreshExpiresIn;

    @JsonProperty("token_type")
    @NotNull
    @Schema(description = I18N_1648700713896, defaultValue = "bearer", example = "bearer")
    private String tokenType;

}
