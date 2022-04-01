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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.pojo.accounting.LoginRequestDTO;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Set;

import static org.touchbit.qa.automatron.constant.Bug.BUG_0001;

@Slf4j
@RestControllerAdvice
public class LogBodyAdviser implements BodyAdvice {

    private final Set<Class<?>> restControllerClasses;

    public LogBodyAdviser(@Qualifier("restControllerClasses") Set<Class<?>> restControllerClasses) {
        this.restControllerClasses = restControllerClasses;
    }

    @Override
    public Object afterBodyRead(Object body,
                                HttpInputMessage inputMessage,
                                MethodParameter parameter,
                                Type targetType,
                                Class<? extends HttpMessageConverter<?>> converterType) {
        final Method method = parameter.getMethod();
        if (method != null && restControllerClasses.contains(method.getDeclaringClass())) {
            log.debug("Request body:\n{}", body);
            if (body instanceof LoginRequestDTO login && login.password() != null) {
                Bug.register(BUG_0001);
            }
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
        final String path = request.getURI().toString();
        if (path.contains("/api/")) {
            log.debug("Response body:\n{}", body);
        }
        return body;
    }

}
