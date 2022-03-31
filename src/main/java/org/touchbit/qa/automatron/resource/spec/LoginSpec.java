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
import org.touchbit.qa.automatron.pojo.accounting.LoginResponseDTO;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;
import static org.touchbit.qa.automatron.resource.spec.LoginSpec.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = ACCOUNTING_TAG, summary = I18N_1648168212897, responses = {
        @ApiResponse(responseCode = "200", description = I18N_1648168229890, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, schema =
                @Schema(implementation = LoginResponseDTO.class))}),
        @ApiResponse(responseCode = "4xx", description = I18N_1648168086907, content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, array =
                @ArraySchema(schema =
                @Schema(implementation = ErrorDTO.class)), examples = {
                        @ExampleObject(summary = EX_400_BAD_REQUEST_SUMMARY, value = EXAMPLE_400, name = I18N_1648168095253),
                        @ExampleObject(summary = EX_401_UNAUTHORIZED_SUMMARY, value = EXAMPLE_401, name = I18N_1648168104107),
                        @ExampleObject(summary = EX_403_FORBIDDEN_SUMMARY, value = EXAMPLE_403, name = I18N_1648168125864),
                })})})
public @interface LoginSpec {

    String EXAMPLE_400 = """
            [{
              "type": "CONTRACT",
              "source": "authentication.password",
              "reason": "must not be null"
            }]
            """;
    String EXAMPLE_401 = """
            [{
              "type": "ACCESS",
              "source": "login/password",
              "reason": "I18N_1648168111078"
            }]
            """;
    String EXAMPLE_403 = """
            [{
              "type": "ACCESS",
              "source": "User{status=BLOCKED}",
              "reason": "I18N_1648168132812"
            }]
            """;

}
