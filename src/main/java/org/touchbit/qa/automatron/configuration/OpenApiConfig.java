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
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.method.HandlerMethod;
import org.touchbit.qa.automatron.service.ConfigService;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;

/**
 * https://springdoc.github.io/springdoc-openapi-demos/faq.html
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class OpenApiConfig {

    public static final String SECURITY_SCHEME_KEY = "access_token";
    public static final String SECURITY_SCHEME_HEADER = "Authorization";
    private static final String[] PATHS = {"/**/*"};

    private final ConfigService configService;

    @Bean
    @DependsOn({"appVersion"})
    public GroupedOpenApi initRussianOpenApiDefinition(final String appVersion) {
        log.info("Initializing russian openapi definition.");
        return initOpenApiDefinition("Russian", appVersion);
    }

    @Bean
    @DependsOn({"appVersion"})
    public GroupedOpenApi initEnglishOpenApiDefinition(final String appVersion) {
        log.info("Initializing english openapi definition.");
        return initOpenApiDefinition("English", appVersion);
    }

    private GroupedOpenApi initOpenApiDefinition(String group, String appVersion) {
        return GroupedOpenApi.builder()
                .group(group)
                .addOperationCustomizer(requestIdGlobalHeader())
                .addOperationCustomizer(acceptLocaleHeader())
                .addOperationCustomizer(responsesCustomizer())
                .addOperationCustomizer(addSecurityItem())
                .addOpenApiCustomiser(openApiInfoCustomiser(appVersion))
                .addOpenApiCustomiser(openApiSecuritySchemeCustomiser())
                .pathsToMatch(PATHS)
                .build();
    }

    private OpenApiCustomiser openApiSecuritySchemeCustomiser() {
        return openApi -> openApi.getComponents()
                .addSecuritySchemes(SECURITY_SCHEME_KEY, new SecurityScheme()
                        .scheme("bearer")
                        .name(SECURITY_SCHEME_HEADER)
                        .type(HTTP)
                        .in(HEADER)
                        .description(I18N_1648397372228));
    }

    private OperationCustomizer addSecurityItem() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final List<Parameter> parameters = operation.getParameters();
            if (parameters != null) {
                operation.getParameters().removeIf(p ->
                        HEADER.toString().equals(p.getIn()) && SECURITY_SCHEME_HEADER.equalsIgnoreCase(p.getName()));
                final GetMapping get = handlerMethod.getMethodAnnotation(GetMapping.class);
                if (get != null) {
                    final List<String> paths = Arrays.asList(get.path());
                    if (paths.contains("/api/bug") ||
                        paths.contains("/api/bugs") ||
                        paths.contains("/api/accounting/login")) {
                        return operation;
                    }
                }
            }
            return operation.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_KEY));
        };
    }

    private OpenApiCustomiser openApiInfoCustomiser(String appVersion) {
        return openApi -> openApi.info(new Info()
                .title("Automatron")
                .version(appVersion)
                .description(I18N_1648167967145));
    }

    private OperationCustomizer requestIdGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (operation.getTags().contains(BUG_TAG)) {
                return operation;
            }
            if (configService.isGlobalRequestIdHeaderEnabled()) {
                final Parameter xRequestId = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema().type("integer"))
                        .name(RID)
                        .description(I18N_1648168060464)
                        .required(false);
                operation.addParametersItem(xRequestId);
            }
            return operation;
        };
    }

    private OperationCustomizer responsesCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final ApiResponses responses = operation.getResponses();
            final Set<String> keys = new TreeSet<>(responses.keySet());
            if (!configService.isGlobal5xxResponseEnabled()) {
                keys.remove("5xx");
            }
            final ApiResponses sortedApiResponses = new ApiResponses();
            keys.forEach(key -> sortedApiResponses.put(key, responses.get(key)));
            operation.setResponses(sortedApiResponses);
            return operation;
        };
    }

    private OperationCustomizer acceptLocaleHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (configService.isGlobalLocaleHeaderEnabled()) {
                final Parameter acceptLanguage = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema().addEnumItem("ru").addEnumItem("en"))
                        .name(LOCALE)
                        .description(I18N_1648168069261)
                        .required(false);
                operation.addParametersItem(acceptLanguage);
            }
            return operation;
        };
    }

}
