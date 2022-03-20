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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.touchbit.qa.automatron.pojo.accounting.AuthDTO;
import org.touchbit.qa.automatron.pojo.accounting.LoginDTO;
import org.touchbit.qa.automatron.service.AccountingService;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.CONTROLLER_ACCOUNTING_DESCRIPTION;
import static org.touchbit.qa.automatron.constant.APIExamples.AUTH_DTO;
import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.RESOURCE_ACCOUNTING_POST_LOGIN_DESCRIPTION;
import static org.touchbit.qa.automatron.constant.ResourceConstants.ACCOUNTING_TAG;

@RestController
@RequiredArgsConstructor
@Tag(name = ACCOUNTING_TAG, description = CONTROLLER_ACCOUNTING_DESCRIPTION)
public class AccountingApiController implements AccountingController {

    private final AccountingService accountingService;

    @PostMapping(path = "/api/accounting/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Completed successfully", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthDTO.class), examples = {@ExampleObject(AUTH_DTO)})}),
    })
    @ResponseStatus(HttpStatus.OK)
    @Operation(tags = ACCOUNTING_TAG, summary = RESOURCE_ACCOUNTING_POST_LOGIN_DESCRIPTION)
    public AuthDTO initAuthentication(@RequestBody @Valid LoginDTO request) {
        return accountingService.authenticate(request);
    }

}
