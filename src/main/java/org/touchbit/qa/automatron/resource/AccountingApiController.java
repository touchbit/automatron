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
import org.touchbit.qa.automatron.pojo.accounting.login.LoginRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.login.LoginResponseDTO;
import org.touchbit.qa.automatron.pojo.accounting.user.CreateUserRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.user.PatchUserRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.user.PutUserRequestDTO;
import org.touchbit.qa.automatron.pojo.accounting.user.UserResponseDTO;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.mapping.PatchRequest;
import org.touchbit.qa.automatron.resource.mapping.PostRequest;
import org.touchbit.qa.automatron.resource.mapping.PutRequest;
import org.touchbit.qa.automatron.resource.param.GetUserListQuery;
import org.touchbit.qa.automatron.resource.param.LogoutQueryParameters;
import org.touchbit.qa.automatron.resource.param.UserLoginPath;
import org.touchbit.qa.automatron.resource.spec.*;
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

    @PostLoginSpec()
    @PostRequest(path = "/api/accounting/login")
    public Response<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request) {
        log.info(" --> User login request");
        final LoginResponseDTO responseBody = accountingService.authenticate(request.login(), request.password());
        log.info(" <-- Completed successfully.");
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @GetLogoutSpec()
    @GetRequest(path = "/api/accounting/logout")
    public Response<Void> logout(@RequestHeader HttpHeaders headers,
                                 @Valid LogoutQueryParameters parameters) {
        log.info(" --> User logout request");
        final Session session = accountingService.authorize(headers);
        accountingService.logout(session, parameters);
        log.info(" <-- Completed successfully (no response body)");
        return new Response<>(null, HttpStatus.NO_CONTENT);
    }

    @GetUserListSpec()
    @GetRequest(path = "/api/accounting/users")
    public Response<List<UserResponseDTO>> getUserList(@RequestHeader HttpHeaders headers,
                                                       @Valid GetUserListQuery search) {
        log.info(" --> Get users by filter");
        accountingService.authorize(headers);
        final List<UserResponseDTO> responseBody = accountingService.getUsers(search);
        log.info(" <-- Completed successfully. Return {} users.", responseBody.size());
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @GetUserSpec()
    @GetRequest(path = "/api/accounting/users/{login}")
    public Response<UserResponseDTO> getUser(@RequestHeader HttpHeaders headers,
                                             @Valid UserLoginPath parameters) {
        log.info(" --> Get user by login: {}", parameters.getLogin());
        accountingService.authorize(headers);
        final UserResponseDTO responseBody = accountingService.getUser(parameters);
        log.info(" <-- Completed successfully. Return user with login: {}", responseBody.login());
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @PostUserSpec()
    @PostRequest(path = "/api/accounting/users")
    public Response<UserResponseDTO> postUser(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid CreateUserRequestDTO request) {
        log.info(" --> Add user request");
        final Session session = accountingService.authorizeAdmin(headers);
        final UserResponseDTO responseBody = accountingService.addNewUser(session, request);
        log.info(" <-- Completed successfully. Return user with login: {}", responseBody.login());
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @PutUserSpec()
    @PutRequest(path = "/api/accounting/users/{login}")
    public Response<UserResponseDTO> putUser(@RequestHeader HttpHeaders headers,
                                             @Valid UserLoginPath parameters,
                                             @RequestBody @Valid PutUserRequestDTO request) {
        log.info(" --> Replace user request");
        final Session session = accountingService.authorize(headers);
        request.login(parameters.getLogin());
        final UserResponseDTO responseBody = accountingService.putUser(session, request);
        log.info(" <-- Completed successfully. Return user with login: {}", responseBody.login());
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @PatchUserSpec()
    @PatchRequest(path = "/api/accounting/users/{login}")
    public Response<UserResponseDTO> patchUser(@RequestHeader HttpHeaders headers,
                                               @Valid UserLoginPath parameters,
                                               @RequestBody @Valid PatchUserRequestDTO request) {
        log.info(" --> Replace user request");
        final Session session = accountingService.authorize(headers);
        request.login(parameters.getLogin());
        final UserResponseDTO responseBody = accountingService.patchUser(session, request);
        log.info(" <-- Completed successfully. Return user with login: {}", responseBody.login());
        return new Response<>(responseBody, HttpStatus.OK);
    }

}
