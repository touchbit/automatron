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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springdoc.api.annotations.ParameterObject;
import org.touchbit.qa.automatron.annotation.QueryPOJO;
import org.touchbit.qa.automatron.constant.LogoutMode;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648399645845;

@Setter
@Getter
@ToString
@QueryPOJO
@ParameterObject
public class LogoutQueryParameters {

    @Parameter(name = "mode", in = QUERY, description = I18N_1648399645845, schema =
    @Schema(implementation = LogoutMode.class))
    private String mode;

}
