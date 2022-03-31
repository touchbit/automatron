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
import org.touchbit.qa.automatron.pojo.bug.BugDTO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168270342;
import static org.touchbit.qa.automatron.constant.ResourceConstants.BUG_TAG;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = BUG_TAG, summary = I18N_1648168270342, responses = {
        @ApiResponse(responseCode = "200", description = "Completed successfully", content = {
                @Content(mediaType = APPLICATION_JSON_VALUE, array =
                @ArraySchema(minItems = 0, uniqueItems = true, schema =
                @Schema(implementation = BugDTO.class)), examples = {@ExampleObject(GetBugListSpec.EXAMPLE)})}),
})
public @interface GetBugListSpec {

    String EXAMPLE = """
            [
              {
                "id": 42,
                "type": "UNPROCESSABLE",
                "info": "The answer to life, universe and everything."
              }
            ]
            """;

}
