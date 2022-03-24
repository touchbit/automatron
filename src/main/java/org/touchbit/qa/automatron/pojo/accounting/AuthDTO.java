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
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.pojo.POJO;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@ToString
public class AuthDTO extends POJO {

    @JsonProperty("access_token")
    @JsonPropertyDescription("auth.dto.object.description")
    @NotNull
    private String accessToken;

    @JsonProperty("access_expires_in")
    @DecimalMin("1")
    @DecimalMax("86400")
    @NotNull
    private Long accessExpiresIn;

    @JsonProperty("refresh_token")
    @NotNull
    private String refreshToken;

    @JsonProperty("refresh_expires_in")
    @NotNull
    private Long refreshExpiresIn;

    @JsonProperty("token_type")
    @NotNull
    private String tokenType;

//    @JsonProperty("not-before-policy")
//    @NotNull
//    private Long notBeforePolicy;
//
//    @JsonProperty("session_state")
//    @NotNull
//    private String sessionState;

//    @JsonProperty("scope")
//    @NotNull
//    private String scope;

}
