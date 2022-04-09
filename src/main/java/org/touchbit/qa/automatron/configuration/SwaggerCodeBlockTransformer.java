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

package org.touchbit.qa.automatron.configuration;

import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {

    private static final String OLD = ".renderedMarkdown code{" +
                                      "background:rgba(0,0,0,.05);" +
                                      "border-radius:4px;" +
                                      "color:#9012fe;" +
                                      "font-family:monospace;" +
                                      "font-size:14px;font-weight:600;" +
                                      "padding:5px 7px}";
    private static final String NEW = ".renderedMarkdown code{" +
                                      "color:#9012fe;" +
                                      "font-family:monospace;" +
                                      "font-size:14px;font-weight:600}";
    private Resource transformedSwaggerUiCssResource = null;

    public SwaggerCodeBlockTransformer(SwaggerUiConfigProperties swaggerUiConfig,
                                       SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                       SwaggerUiConfigParameters swaggerUiConfigParameters,
                                       SwaggerWelcomeCommon swaggerWelcomeCommon) {
        super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon);
    }

    @Override
    public Resource transform(HttpServletRequest request,
                              Resource resource,
                              ResourceTransformerChain transformerChain) throws IOException {
        if (resource.toString().contains("swagger-ui.css")) {
            if (transformedSwaggerUiCssResource == null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    final String css = reader.lines().collect(Collectors.joining());
                    final byte[] transformedContent = css.replace(OLD, NEW).getBytes();
                    transformedSwaggerUiCssResource = new TransformedResource(resource, transformedContent);
                }
            }
            return transformedSwaggerUiCssResource;
        }
        return super.transform(request, resource, transformerChain);
    }

}
