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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.touchbit.qa.automatron.db.entity.Session;
import org.touchbit.qa.automatron.pojo.accounting.GetUserResponseDTO;
import org.touchbit.qa.automatron.pojo.accounting.LoginRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.LoginResponseDTO;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.mapping.PostRequest;
import org.touchbit.qa.automatron.resource.param.GetUserListQueryParameters;
import org.touchbit.qa.automatron.resource.param.GetUserPathParameters;
import org.touchbit.qa.automatron.resource.param.LogoutQueryParameters;
import org.touchbit.qa.automatron.resource.spec.GetUserListSpec;
import org.touchbit.qa.automatron.resource.spec.GetUserSpec;
import org.touchbit.qa.automatron.resource.spec.LoginSpec;
import org.touchbit.qa.automatron.resource.spec.LogoutSpec;
import org.touchbit.qa.automatron.service.AccountingService;

import javax.validation.Valid;
import java.util.List;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168206689;
import static org.touchbit.qa.automatron.constant.ResourceConstants.ACCOUNTING_TAG;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = ACCOUNTING_TAG, description = I18N_1648168206689)
public class AccountingApiController {

    private final AccountingService accountingService;

    @LoginSpec()
    @PostRequest(path = "/api/accounting/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO request) {
        log.info("Request: User authentication by login {}", request.login());
        return accountingService.authenticate(request.login(), request.password());
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
    @GetRequest(path = "/api/accounting/user")
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
    @GetRequest(path = "/api/accounting/user/{login}")
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
