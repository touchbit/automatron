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
import org.springframework.util.ResourceUtils;
import org.touchbit.qa.automatron.Application;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Locale;

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

}
