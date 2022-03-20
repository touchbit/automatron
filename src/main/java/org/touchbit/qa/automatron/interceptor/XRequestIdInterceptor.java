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

package org.touchbit.qa.automatron.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static org.touchbit.qa.automatron.constant.ResourceConstants.RID;

@Slf4j
public class XRequestIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (AutomatronUtils.isNotSwaggerRequest(request)) {
            final String xRequestIdValue = request.getHeader(RID);
            final String rid;
            if (xRequestIdValue == null || xRequestIdValue.isBlank()) {
                rid = UUID.randomUUID().toString();
                log.debug("'{}' header not present. Generated value is used: {}", RID, rid);
            } else {
                rid = xRequestIdValue;
                log.debug("'{}' header present. Header value is used: {}", RID, rid);
            }
            MDC.put(RID, rid);
            log.debug("'{}' was successfully added to the context.", RID);
            response.addHeader(RID, rid);
            log.debug("'{}' header successfully initialized in response servlet.", RID);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object object,
                                Exception arg3) {
        if (AutomatronUtils.isNotSwaggerRequest(request)) {
            final String xRequestIdValue = MDC.get(RID);
            if (xRequestIdValue == null || xRequestIdValue.isBlank()) {
                log.warn("'{}' header value is empty. ", RID);
            } else {
                log.debug("Remove '{}' header value from context.", RID);
                MDC.remove(RID);
            }
        }
    }

}
