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

package org.touchbit.qa.automatron.http;

import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

public class AutomatronHeaders extends HttpHeaders {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "bearer";

    public AutomatronHeaders(Map<String, List<String>> httpHeaders) {
        putAll(httpHeaders);
    }

    public List<String> getAuthorization() {
        return get(AUTHORIZATION);
    }

    public String getBearerAuthorization() {
        final List<String> authorization = getAuthorization();
        if (authorization == null) {
            return null;
        }
        return authorization.stream()
                .map(String::toLowerCase)
                .map(token -> token.replace(BEARER, ""))
                .map(String::trim)
                .findFirst()
                .orElse(null);
    }

}
