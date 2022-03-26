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

package org.touchbit.qa.automatron.util;

import org.springframework.http.server.ServerHttpRequest;
import org.touchbit.qa.automatron.Application;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.function.Function;

public class AutomatronUtils {

    private AutomatronUtils() {

    }

    public static boolean isNotSwaggerRequest(HttpServletRequest request) {
        final String servletPath = request.getServletPath();
        return !servletPath.contains("/v3/api-docs/") && !servletPath.contains("/swagger-ui/index.html");
    }

    public static boolean isNotSwaggerRequest(ServerHttpRequest request) {
        final String servletPath = request.getURI().toString();
        return !servletPath.contains("/v3/api-docs/") && !servletPath.contains("/swagger-ui/index.html");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String getLicence(Locale locale) throws IOException {
        final String licSource = "META-INF/LICENSE_" + locale.getLanguage();
        try (InputStream is = Application.class.getClassLoader().getResourceAsStream(licSource)) {
            if (is != null) {
                byte[] targetArray = new byte[is.available()];
                is.read(targetArray);
                return new String(targetArray);
            }
            throw new IOException("The license file is missing from the project resources: " + licSource);
        }
    }

    public static <O> String errSource(O obj, Function<O, Object> func, String name) {
        return obj.getClass().getSimpleName() + "{" + name + "=" + func.apply(obj) + "}";
    }

    public static <O> String errSource(O obj, Object val, String name) {
        return obj.getClass().getSimpleName() + "{" + name + "=" + val + "}";
    }

}
