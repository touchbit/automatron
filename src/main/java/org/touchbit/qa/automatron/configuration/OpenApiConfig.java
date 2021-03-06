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
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.core.SwaggerUiOAuthProperties;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.method.HandlerMethod;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.mapping.PostRequest;
import org.touchbit.qa.automatron.service.ConfigService;

import java.util.*;

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

    @Bean
    public SwaggerIndexTransformer swaggerIndexTransformer(SwaggerUiConfigProperties sUiConfig,
                                                           SwaggerUiOAuthProperties sUiOAuthProperties,
                                                           SwaggerUiConfigParameters sUiConfigParameters,
                                                           SwaggerWelcomeCommon sWelcomeCommon) {
        return new SwaggerCodeBlockTransformer(sUiConfig, sUiOAuthProperties, sUiConfigParameters, sWelcomeCommon);
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
            if (getOperationPaths(handlerMethod).stream().anyMatch(path -> path.contains("/api/bugs") ||
                                                                           path.contains("/api/accounting/login"))) {
                return operation;
            }
            if (configService.isGlobalAuthorizationHeaderEnabled()) {
                List<Parameter> parameters = operation.getParameters();
                if (parameters == null) {
                    parameters = new ArrayList<>();
                }
                parameters.add(new Parameter()
                        .in(HEADER.toString())
                        .name(SECURITY_SCHEME_HEADER)
                        .description(I18N_1648880914205)
                        .example("Bearer b6017ba4-79ef-4dc1-bd9a-6d039406a662"));
                operation.setParameters(parameters);
            }
            return operation.addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_KEY));
        };
    }

    private List<String> getOperationPaths(HandlerMethod handlerMethod) {
        final GetRequest get = handlerMethod.getMethodAnnotation(GetRequest.class);
        if (get != null) {
            return Arrays.asList(get.path());
        }
        final PostRequest post = handlerMethod.getMethodAnnotation(PostRequest.class);
        if (post != null) {
            return Arrays.asList(post.path());
        }
        return new ArrayList<>();
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
                log.debug("Global 'Request-Id' header enabled");
                final Parameter xRequestId = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema().type("integer"))
                        .name(RID)
                        .description(I18N_1648168060464)
                        .required(false);
                operation.addParametersItem(xRequestId);
            } else {
                log.debug("Global 'Request-Id' header disabled");
            }
            return operation;
        };
    }

    private OperationCustomizer responsesCustomizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final ApiResponses responses = operation.getResponses();
            final Set<String> keys = new TreeSet<>(responses.keySet());
            if (configService.isGlobal5xxResponseEnabled()) {
                log.debug("Global 5xx response group enabled");
            } else {
                log.debug("Global 5xx response group disabled");
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
                log.debug("Global 'Locale' header enabled");
                final Parameter acceptLanguage = new Parameter()
                        .in(ParameterIn.HEADER.toString())
                        .schema(new StringSchema().addEnumItem("ru").addEnumItem("en"))
                        .name(LOCALE)
                        .description(I18N_1648168069261)
                        .required(false);
                operation.addParametersItem(acceptLanguage);
            } else {
                log.debug("Global 'Locale' header disabled");
            }
            return operation;
        };
    }

}
