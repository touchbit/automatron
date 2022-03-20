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
import org.springframework.web.servlet.HandlerInterceptor;
import org.touchbit.qa.automatron.advice.BugAdviser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.touchbit.qa.automatron.constant.Bug.BUG_0002;
import static org.touchbit.qa.automatron.constant.ResourceConstants.LOCALE;

@Slf4j
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        final String locale = request.getHeader(LOCALE);
        // let's make it a little more difficult
        if (locale != null) {
            log.debug("Validate '{}' header value: '{}'", LOCALE, locale);
            if (!"ru".equalsIgnoreCase(locale) && !"en".equalsIgnoreCase(locale)) {
                BugAdviser.addBug(BUG_0002);
            }
        }
        return true;
    }

}
