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

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.touchbit.qa.automatron.interceptor.BugInterceptor;
import org.touchbit.qa.automatron.pojo.accounting.UserDTO;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import java.lang.reflect.Type;

import static org.touchbit.qa.automatron.constant.Bug.BUG_0001;

@Slf4j
@RestControllerAdvice
public class LogBodyAdviser implements BodyAdvice {

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("Request body:\n{}", body);
        if (body instanceof UserDTO userDTO && userDTO.password() != null) {
            BugInterceptor.addBug(BUG_0001);
        }
        return body;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (AutomatronUtils.isNotSwaggerRequest(request)) {
            log.debug("Response body:\n{}", body);
        }
        if (body instanceof UserDTO userDTO && userDTO.password() != null) {
            BugInterceptor.addBug(BUG_0001);
        }
        return body;
    }

}
