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
import org.touchbit.qa.automatron.constant.Bug;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.touchbit.qa.automatron.constant.ResourceConstants.BID;
import static org.touchbit.qa.automatron.constant.ResourceConstants.RID;

@Slf4j
public class BugInterceptor implements HandlerInterceptor {

    private static final ConcurrentMap<String, List<Bug>> BUGS = new ConcurrentHashMap<>();

    public static void addBug(final Bug bug) {
        final String rid = MDC.get(RID);
        if (rid == null || rid.isBlank()) {
            log.warn("Unable to link bug to request. '{}' header not passed.", RID);
            return;
        }
        BUGS.computeIfAbsent(rid, i -> new ArrayList<>()).add(bug);
        log.warn("Bug registered: {}", bug.id());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object object,
                                Exception arg3) {
        final String rid = MDC.get(RID);
        log.debug("Get bugs for request=id: {}", rid);
        if (rid != null) {
            final List<Bug> bugs = BUGS.get(rid);
            log.debug("Registered bugs: {}", bugs == null ? 0 : bugs.size());
            if (bugs != null && !bugs.isEmpty()) {
                for (Bug bug : bugs) {
                    log.info("Add bug header to the response: '{}: {}'", BID, bug.stringId());
                    response.setHeader(BID, bug.stringId());
                }
            }
            BUGS.remove(rid);
        }
    }

}
