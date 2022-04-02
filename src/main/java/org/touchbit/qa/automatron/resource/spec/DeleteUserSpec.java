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
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.ACCOUNTING_TAG;
import static org.touchbit.qa.automatron.constant.ResourceConstants.EX_400_BAD_REQUEST_SUMMARY;
import static org.touchbit.qa.automatron.resource.spec.DeleteUserSpec.EXAMPLE_400;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = ACCOUNTING_TAG, summary = I18N_1648878983237, description = I18N_1648879073286, responses = {
        @ApiResponse(responseCode = "204", description = I18N_1648879024173),
        @ApiResponse(responseCode = "4xx", description = I18N_1648168086907, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, array =
                @ArraySchema(schema =
                @Schema(implementation = ErrorDTO.class)), examples = {
                        @ExampleObject(summary = EX_400_BAD_REQUEST_SUMMARY, value = EXAMPLE_400, name = I18N_1648168095253),
                })})})
public @interface DeleteUserSpec {

    String EXAMPLE_400 = """
            [{
              "type": "CONTRACT",
              "source": "Path.login",
              "reason": "I18N_1648870070924"
            }]
            """;

}
