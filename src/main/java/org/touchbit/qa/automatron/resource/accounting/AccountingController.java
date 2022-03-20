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

package org.touchbit.qa.automatron.resource.accounting;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.APIExamples.API_ACCOUNTING_LOGIN_404;
import static org.touchbit.qa.automatron.constant.APIExamples.EX_API_500;
import static org.touchbit.qa.automatron.constant.ResourceConstants.ACCOUNTING_TAG;

@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@Tags({
        @Tag(name = ACCOUNTING_TAG),
})
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request", content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorDTO.class), minItems = 1, uniqueItems = true), examples = {@ExampleObject(API_ACCOUNTING_LOGIN_404)})}),
        @ApiResponse(responseCode = "500", description = "Internal server error", content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorDTO.class), minItems = 1, uniqueItems = true), examples = {@ExampleObject(EX_API_500)})}),
})
public interface AccountingController {
}
