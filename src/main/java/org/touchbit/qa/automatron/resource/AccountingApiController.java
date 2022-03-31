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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.touchbit.qa.automatron.db.entity.Session;
import org.touchbit.qa.automatron.pojo.accounting.GetUserResponseDTO;
import org.touchbit.qa.automatron.pojo.accounting.LoginResponseDTO;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.param.GetUserListQueryParameters;
import org.touchbit.qa.automatron.resource.param.GetUserPathParameters;
import org.touchbit.qa.automatron.resource.param.LogoutQueryParameters;
import org.touchbit.qa.automatron.resource.spec.GetUserListSpec;
import org.touchbit.qa.automatron.resource.spec.GetUserSpec;
import org.touchbit.qa.automatron.resource.spec.LoginSpec;
import org.touchbit.qa.automatron.resource.spec.LogoutSpec;
import org.touchbit.qa.automatron.service.AccountingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.ACCOUNTING_TAG;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = ACCOUNTING_TAG, description = I18N_1648168206689)
public class AccountingApiController {

    private final AccountingService accountingService;

    @LoginSpec()
    @GetRequest(path = "/api/accounting/login", status = HttpStatus.OK)
    public LoginResponseDTO login(
            @Parameter(description = I18N_1648168739660, in = QUERY, example = "admin") @NotNull @Size(min = 5, max = 25) String login,
            @Parameter(description = I18N_1648168744616, in = QUERY, example = "admin") @NotNull @Size(min = 5, max = 25) String password) {
        log.info("Request: User authentication by login {}", login);
        return accountingService.authenticate(login, password);
    }

    @LogoutSpec()
    @GetRequest(path = "/api/accounting/logout", status = HttpStatus.NO_CONTENT)
    public void logout(
            @RequestHeader HttpHeaders headers,
            @Valid LogoutQueryParameters logoutQueryParameters) {
        log.info(" --> User logout request");
        final Session session = accountingService.authorize(headers);
        accountingService.logout(session, logoutQueryParameters);
        log.info(" <-- Completed successfully (no response body)");
    }

    @GetUserListSpec()
    @GetRequest(path = "/api/accounting/user", status = HttpStatus.OK)
    public List<GetUserResponseDTO> getUserList(
            @RequestHeader HttpHeaders headers,
            @Valid GetUserListQueryParameters search) {
        log.info(" --> Get users by filter");
        accountingService.authorize(headers);
        final List<GetUserResponseDTO> users = accountingService.getUsers(search);
        log.info(" <-- Completed successfully. Return {} users.", users.size());
        return users;
    }

    @GetUserSpec()
    @GetRequest(path = "/api/accounting/user/{login}", status = HttpStatus.OK)
    public GetUserResponseDTO getUser(
            @RequestHeader HttpHeaders headers,
            @Valid GetUserPathParameters pathParameters) {
        log.info(" --> Get user by login: {}", pathParameters.getLogin());
        accountingService.authorize(headers);
        final GetUserResponseDTO user = accountingService.getUser(pathParameters);
        log.info(" <-- Completed successfully. Return user.");
        return user;
    }

}
