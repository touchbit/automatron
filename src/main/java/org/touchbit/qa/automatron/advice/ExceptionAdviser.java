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

package org.touchbit.qa.automatron.advice;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;
import org.touchbit.qa.automatron.util.AutomatronException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.touchbit.qa.automatron.constant.APIExamples.*;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.RID;
import static org.touchbit.qa.automatron.pojo.error.ErrorType.CONTRACT;
import static org.touchbit.qa.automatron.pojo.error.ErrorType.SYSTEM;

@Slf4j
@RestControllerAdvice
public class ExceptionAdviser {
    // ResponseEntityExceptionHandler

    @ApiResponse(responseCode = "5xx", description = I18N_1648168141132, content =
    @Content(mediaType = APPLICATION_JSON_VALUE, examples = {
            @ExampleObject(name = "500 (SYSTEM)", value = API_SYSTEM_ERR_500, description = I18N_1648168152659),
            @ExampleObject(name = "500 (LOGIC)", value = API_LOGICAL_ERR_500, description = I18N_1648168162035),
            @ExampleObject(name = "500 (NETWORK)", value = API_NETWORK_ERR_500, description = I18N_1648168167873),
    }))
    @ExceptionHandler({Exception.class, RuntimeException.class})
    public ResponseEntity<List<ErrorDTO>> internalServerErrorException(Exception exception) {
        List<ErrorDTO> errors = new ArrayList<>();
        errors.add(new ErrorDTO().type(SYSTEM).source(exception.getClass().getSimpleName())
                .reason(exception.getMessage()));
        log.error("Internal Server Error", exception);
        return buildResponseEntity(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public ResponseEntity<List<ErrorDTO>> handleValidationExceptions(Exception exception) {
        List<ErrorDTO> errors = new ArrayList<>();
        if (exception instanceof BindException bindException) {
            for (ObjectError error : bindException.getAllErrors()) {
                String source = "";
                final Object[] arguments = error.getArguments();
                if (arguments != null) {
                    for (Object argument : arguments) {
                        if (argument instanceof MessageSourceResolvable messageSourceResolvable) {
                            final String[] codes = messageSourceResolvable.getCodes();
                            if (codes != null && codes.length > 0) {
                                source = messageSourceResolvable.getCodes()[0];
                            }
                        }
                    }
                }
                errors.add(new ErrorDTO().type(CONTRACT).source(source).reason(error.getDefaultMessage()));
            }
        }
        if (exception instanceof ConstraintViolationException constraintViolationException) {
            for (ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()) {
                final String source = constraintViolation.getPropertyPath().toString();
                final String reason = constraintViolation.getMessage();
                errors.add(new ErrorDTO().type(CONTRACT).source(source).reason(reason));
            }
        }
        log.error("Bad Request: contract violation", exception);
        return buildResponseEntity(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({AutomatronException.class})
    public ResponseEntity<List<ErrorDTO>> automatronException(AutomatronException e) {
        final ErrorDTO error = new ErrorDTO().type(e.type()).source(e.source()).reason(e.reason());
        final List<ErrorDTO> errors = Collections.singletonList(error);
        return buildResponseEntity(errors, e.status());
    }

    private <B> ResponseEntity<B> buildResponseEntity(final B body, final HttpStatus status) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        final String xRequestIdValue = MDC.get(RID);
        if (xRequestIdValue != null && !xRequestIdValue.isBlank()) {
            headers.add(RID, xRequestIdValue);
        }
        return new ResponseEntity<>(body, headers, status);
    }

}
