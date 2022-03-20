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

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.touchbit.qa.automatron.Application;
import org.touchbit.qa.automatron.interceptor.LocaleInterceptor;
import org.touchbit.qa.automatron.interceptor.XRequestIdInterceptor;

import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;

/**
 * https://springdoc.github.io/springdoc-openapi-demos/faq.html
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String[] PATHS = {"/**/*"};


    @Bean
    public GroupedOpenApi initRussianOpenApiDefinition() {
        log.info("Initializing russian open api definition.");
        return initOpenApiDefinition("Russian");
    }

    @Bean
    public GroupedOpenApi initEnglishOpenApiDefinition() {
        log.info("Initializing english openapi definition.");
        return initOpenApiDefinition("English");
    }

    @Bean
    public LocaleResolver localeResolver() {
        final FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();
        fixedLocaleResolver.setDefaultLocale(null);
        return fixedLocaleResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("Add {} to {}", XRequestIdInterceptor.class.getSimpleName(), registry.getClass().getSimpleName());
        registry.addInterceptor(new XRequestIdInterceptor());
        log.info("Add {} to {}", LocaleInterceptor.class.getSimpleName(), registry.getClass().getSimpleName());
        registry.addInterceptor(new LocaleInterceptor());
    }

    private GroupedOpenApi initOpenApiDefinition(String group) {
        return GroupedOpenApi.builder()
                .group(group)
                .addOperationCustomizer(xRequestIdGlobalHeader())
                .addOperationCustomizer(acceptLocaleHeader())
                .addOpenApiCustomiser(openApiCustomiser())
                .pathsToMatch(PATHS)
                .build();
    }

    private OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.info(new Info()
                .title("Automatron")
                .version(Application.class.getPackage().getImplementationVersion())
                .description(OPENAPI_DESCRIPTION));
    }

    private OperationCustomizer xRequestIdGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (operation.getTags().contains(BUG_TAG)) {
                return operation;
            }
            final Parameter xRequestId = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().type("integer"))
                    .name(RID)
                    .description(HEADER_REQUEST_ID_DESCRIPTION)
                    .required(false);
            operation.addParametersItem(xRequestId);
            return operation;
        };
    }

    private OperationCustomizer acceptLocaleHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final Parameter acceptLanguage = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().addEnumItem("ru").addEnumItem("en"))
                    .name(LOCALE)
                    .description(HEADER_LOCALE_DESCRIPTION)
                    .required(false);
            operation.addParametersItem(acceptLanguage);
            return operation;
        };
    }

}
