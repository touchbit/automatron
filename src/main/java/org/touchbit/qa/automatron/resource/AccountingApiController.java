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

package org.touchbit.qa.automatron.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.touchbit.qa.automatron.pojo.accounting.AuthDTO;
import org.touchbit.qa.automatron.pojo.accounting.GetUserResponseDTO;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.param.GetUserListQueryParameters;
import org.touchbit.qa.automatron.resource.param.GetUserPathParameters;
import org.touchbit.qa.automatron.resource.param.LogoutQueryParameters;
import org.touchbit.qa.automatron.resource.spec.GetUserListSpec;
import org.touchbit.qa.automatron.resource.spec.GetUserSpec;
import org.touchbit.qa.automatron.resource.spec.LogoutSpec;
import org.touchbit.qa.automatron.service.AccountingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.APIExamples.*;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = ACCOUNTING_TAG, description = I18N_1648168206689)
public class AccountingApiController {

    private final AccountingService accountingService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/api/accounting/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(tags = ACCOUNTING_TAG, summary = I18N_1648168212897, responses = {
            @ApiResponse(responseCode = "200", description = I18N_1648168229890, content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = AuthDTO.class), examples = {@ExampleObject(AUTH_DTO)})}),
            @ApiResponse(responseCode = "4xx", description = I18N_1648168086907, content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = ErrorDTO.class)), examples = {
                    @ExampleObject(summary = EX_400_BAD_REQUEST_SUMMARY, value = EX_ACCOUNTING_LOGIN_400, name = I18N_1648168095253),
                    @ExampleObject(summary = EX_401_UNAUTHORIZED_SUMMARY, value = EX_CODE_401_001, name = I18N_1648168104107),
                    @ExampleObject(summary = EX_403_FORBIDDEN_SUMMARY, value = EX_CODE_403_002, name = I18N_1648168125864),})})})
    public AuthDTO authentication(
            @Parameter(description = I18N_1648168739660, in = QUERY, example = "admin") @NotNull @Size(min = 5, max = 25) String login,
            @Parameter(description = I18N_1648168744616, in = QUERY, example = "admin") @NotNull @Size(min = 5, max = 25) String password) {
        log.info("Request: User authentication by login {}", login);
        return accountingService.authenticate(login, password);
    }

    @LogoutSpec()
    @GetRequest(path = "/api/accounting/logout", status = HttpStatus.NO_CONTENT)
    public void logout(
            @RequestHeader(value = "Authorization", required = false) @Pattern(regexp = "^(?i)(bearer [a-f0-9-]{36})$") String bearerAuthorizationHeader,
            LogoutQueryParameters logoutQueryParameters) {
        log.info("Request: User logout request");
        accountingService.logout(bearerAuthorizationHeader, logoutQueryParameters);
    }

    @GetUserListSpec()
    @GetRequest(path = "/api/accounting/user", status = HttpStatus.OK)
    public List<GetUserResponseDTO> getUserList(@Valid GetUserListQueryParameters search) {
        log.info("Request: Get users by filter");
        return accountingService.getUsers(search);
    }

    @GetUserSpec()
    @GetRequest(path = "/api/accounting/user/{login}", status = HttpStatus.OK)
    public GetUserResponseDTO getUser(@Valid GetUserPathParameters pathParameters) {
        log.info("Request: Get user by login");
        return accountingService.getUser(pathParameters);
    }

}
