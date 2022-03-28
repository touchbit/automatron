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

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.touchbit.qa.automatron.Application;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AutomatronUtils {

    private AutomatronUtils() {

    }

    public static String getLicence(Locale locale) throws IOException {
        final String licSource = "META-INF/LICENSE_" + locale.getLanguage();
        return readResource(licSource);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String readResource(String resourcePath) throws IOException {
        try (InputStream is = Application.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is != null) {
                byte[] targetArray = new byte[is.available()];
                is.read(targetArray);
                return new String(targetArray);
            }
            throw new IOException("File is missing from the project resources: " + resourcePath);
        }
    }

    public static <O> String errSource(O obj, Function<O, Object> func, String name) {
        return obj.getClass().getSimpleName() + "{" + name + "=" + func.apply(obj) + "}";
    }

    public static <O> String errSource(O obj, Object val, String name) {
        return obj.getClass().getSimpleName() + "{" + name + "=" + val + "}";
    }

    public static Set<Class<?>> getBeanDefinitionClasses(ClassPathScanningCandidateComponentProvider scanner) {
        return scanner.findCandidateComponents("org.touchbit.qa.automatron")
                .stream()
                .map(s -> classForName(s.getBeanClassName()))
                .collect(Collectors.toSet());

    }

    public static Class<?> classForName(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected class name loaded: " + name, e);
        }
    }

}
