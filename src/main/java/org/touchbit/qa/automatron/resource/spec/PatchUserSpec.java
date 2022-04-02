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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.touchbit.qa.automatron.pojo.accounting.user.PatchUserRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.user.UserResponseDTO;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;
import static org.touchbit.qa.automatron.resource.spec.PatchUserSpec.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = ACCOUNTING_TAG, summary = I18N_1648869878121, description = I18N_1648863454853, requestBody =
@RequestBody(content = @Content(mediaType = APPLICATION_JSON_VALUE, schema =
@Schema(implementation = PatchUserRequestDTO.class))), responses = {
        @ApiResponse(responseCode = "200", description = I18N_1648865056908, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                @Schema(implementation = UserResponseDTO.class))}),
        @ApiResponse(responseCode = "4xx", description = I18N_1648168086907, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, array =
                @ArraySchema(schema =
                @Schema(implementation = ErrorDTO.class)), examples = {
                        @ExampleObject(summary = EX_400_BAD_REQUEST_SUMMARY, value = EXAMPLE_400, name = I18N_1648168095253),
                        @ExampleObject(summary = EX_403_FORBIDDEN_SUMMARY, value = EXAMPLE_403, name = I18N_1648168125864),
                        @ExampleObject(summary = EX_404_ENTITY_NOT_FOUND_SUMMARY, value = EXAMPLE_404, name = I18N_1648689343652),
                })})})
public @interface PatchUserSpec {

    String EXAMPLE_400 = """
            [{
              "type": "CONTRACT",
              "source": "Path.login",
              "reason": "I18N_1648870070924"
            }]
            """;
    String EXAMPLE_403 = """
            [{
              "type": "ACCESS",
              "source": "Authorized user role",
              "reason": "I18N_1648168132812"
            }]
            """;
    String EXAMPLE_404 = """
            [{
              "type": "CONDITION",
              "source": "login",
              "reason": "I18N_1648688495987"
            }]
            """;

}
