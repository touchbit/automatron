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

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.touchbit.qa.automatron.constant.Bug;

import java.util.List;

import static org.touchbit.qa.automatron.constant.ResourceConstants.BID;
import static org.touchbit.qa.automatron.constant.ResourceConstants.RID;

@Slf4j
public class Response<E> extends ResponseEntity<E> {

    public Response(E body, HttpStatus status) {
        super(body, getDefaultHeaders(), status);
    }

    public Response(E body, HttpHeaders headers, HttpStatus status) {
        super(body, mergeDefaultHeaders(headers), status);
    }

    private static HttpHeaders mergeDefaultHeaders(HttpHeaders headers) {
        headers.addAll(getDefaultHeaders());
        return headers;
    }

    private static HttpHeaders getDefaultHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        final List<Bug> bugs = Bug.takeRegistered();
        log.trace("Registered bugs: {}", bugs.size());
        for (Bug bug : bugs) {
            headers.add(BID, bug.stringId());
            log.trace("Add bug header to the response: '{}: {}'", BID, bug.stringId());
        }
        final String xRequestIdValue = MDC.get(RID);
        if (xRequestIdValue != null && !xRequestIdValue.isBlank()) {
            headers.add(RID, xRequestIdValue);
        }
        return headers;
    }

}
