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

package org.touchbit.qa.automatron.resource.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.touchbit.qa.automatron.constant.APIExamples;
import org.touchbit.qa.automatron.pojo.accounting.GetUserResponseDTO;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;
import static org.touchbit.qa.automatron.resource.spec.GetUserListSpec.EXAMPLE_400;
import static org.touchbit.qa.automatron.resource.spec.GetUserListSpec.EXAMPLE_401;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = ACCOUNTING_TAG, summary = I18N_1648684060672, responses = {
        @ApiResponse(responseCode = "200", description = I18N_1648684256974, content = {
                @Content(array =
                @ArraySchema(minItems = 0, uniqueItems = true, schema =
                @Schema(implementation = GetUserResponseDTO.class)))}),
        @ApiResponse(responseCode = "4xx", description = I18N_1648168086907, content = {
                @Content(array =
                @ArraySchema(schema =
                @Schema(implementation = ErrorDTO.class)), examples = {
                        @ExampleObject(summary = EX_400_BAD_REQUEST_SUMMARY, value = EXAMPLE_400, name = I18N_1648168095253),
                        @ExampleObject(summary = EX_401_UNAUTHORIZED_SUMMARY, value = EXAMPLE_401, name = I18N_1648168104107),
                })})})
public @interface GetUserListSpec {

    String EXAMPLE_401 = APIExamples.EX_CODE_401_002;
    String EXAMPLE_400 = """
            [
              {
                "type": "CONTRACT",
                "source": "Query.status",
                "reason": "... No enum constant org.touchbit.qa.automatron.constant.UserStatus.ACTIVE1"
              }
            ]
            """;

}
