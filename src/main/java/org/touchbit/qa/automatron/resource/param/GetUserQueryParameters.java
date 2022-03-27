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

package org.touchbit.qa.automatron.resource.param;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springdoc.api.annotations.ParameterObject;
import org.touchbit.qa.automatron.annotation.QueryPOJO;
import org.touchbit.qa.automatron.db.entity.UserStatus;
import org.touchbit.qa.automatron.db.entity.UserType;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168739660;

@Setter
@Getter
@ToString
@QueryPOJO
@ParameterObject
public class GetUserQueryParameters {

    @Parameter(name = "id", in = ParameterIn.QUERY, description = "TODO")
    @Min(1)
    private Long id;

    @Parameter(name = "login", in = ParameterIn.QUERY, description = I18N_1648168739660)
    @Size(min = 5, max = 25)
    public String login;

    @Parameter(name = "status", in = ParameterIn.QUERY, description = "TODO")
    private UserStatus status;

    @Parameter(name = "type", in = ParameterIn.QUERY, description = "TODO")
    private UserType type;

}
