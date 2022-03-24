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

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Type;

public interface BodyAdvice extends ResponseBodyAdvice<Object>, RequestBodyAdvice {

    @Override
    default boolean supports(MethodParameter methodParameter,
                             Type targetType,
                             Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    default HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage,
                                            MethodParameter parameter,
                                            Type targetType,
                                            Class<? extends HttpMessageConverter<?>> converterType) {
        return inputMessage;
    }

    @Override
    default Object afterBodyRead(Object body,
                                 HttpInputMessage inputMessage,
                                 MethodParameter parameter,
                                 Type targetType,
                                 Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    default Object handleEmptyBody(Object body,
                                   HttpInputMessage inputMessage,
                                   MethodParameter parameter,
                                   Type targetType,
                                   Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    default boolean supports(MethodParameter returnType,
                             Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    default Object beforeBodyWrite(Object body,
                                   MethodParameter returnType,
                                   MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request,
                                   ServerHttpResponse response) {
        return body;
    }

}
