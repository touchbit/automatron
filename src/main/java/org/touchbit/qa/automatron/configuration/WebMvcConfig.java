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
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.touchbit.qa.automatron.Application;
import org.touchbit.qa.automatron.annotation.QueryPOJO;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.interceptor.BugInterceptor;
import org.touchbit.qa.automatron.interceptor.LocaleInterceptor;
import org.touchbit.qa.automatron.interceptor.XRequestIdInterceptor;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;
import static org.touchbit.qa.automatron.constant.Bug.BugType;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.*;

/**
 * https://springdoc.github.io/springdoc-openapi-demos/faq.html
 */
@Slf4j
@Configuration
public class WebMvcConfig {

    public static final String SECURITY_SCHEME_KEY = "access_token";
    public static final String SECURITY_SCHEME_HEADER = "Authorization";
    private static final String[] PATHS = {"/**/*"};


    @Bean("appVersion")
    public String appVersion() {
        String version = Application.class.getPackage().getImplementationVersion();
        return version == null || version.isBlank() ? "0.0.0" : version;
    }

    @Bean
    @DependsOn({"appVersion"})
    public GroupedOpenApi initRussianOpenApiDefinition(final String appVersion) {
        log.info("Initializing russian open api definition.");
        return initOpenApiDefinition("Russian", appVersion);
    }

    @Bean
    @DependsOn({"appVersion"})
    public GroupedOpenApi initEnglishOpenApiDefinition(final String appVersion) {
        log.info("Initializing english openapi definition.");
        return initOpenApiDefinition("English", appVersion);
    }

    @Bean
    public LocaleResolver localeResolver() {
        final FixedLocaleResolver fixedLocaleResolver = new FixedLocaleResolver();
        fixedLocaleResolver.setDefaultLocale(null);
        return fixedLocaleResolver;
    }

    @Bean
    public ResourceBundleMessageSource resourceBundleMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("i18n/msg", "i18n/bug");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    @SuppressWarnings("HttpUrlsUsage")
    public URL serverAddress(@Value("${server.port}") int port) throws MalformedURLException {
        final String url = "http://" + InetAddress.getLoopbackAddress().getHostName() + ":" + port;
        return new URL(url);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                log.info("Add {} to {}", XRequestIdInterceptor.class.getSimpleName(), registry.getClass().getSimpleName());
                registry.addInterceptor(new XRequestIdInterceptor());
                log.info("Add {} to {}", BugInterceptor.class.getSimpleName(), registry.getClass().getSimpleName());
                registry.addInterceptor(new BugInterceptor());
                log.info("Add {} to {}", LocaleInterceptor.class.getSimpleName(), registry.getClass().getSimpleName());
                registry.addInterceptor(new LocaleInterceptor());
            }

        };
    }

    @Bean(name = "restControllerClasses")
    public Set<Class<?>> restControllerClasses() {
        log.info("Search resource classes with annotation @{}", RestController.class.getSimpleName());
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        return AutomatronUtils.getBeanDefinitionClasses(scanner);
    }

    @Bean(name = "queryPOJOClasses")
    public Set<Class<?>> queryPOJOClasses() {
        log.info("Search query POJO classes with annotation @{}", QueryPOJO.class.getSimpleName());
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(QueryPOJO.class));
        return AutomatronUtils.getBeanDefinitionClasses(scanner);
    }

    @Bean(name = "queryPOJOClassSimpleNames")
    public Set<String> queryPOJOClassNames(@Qualifier("queryPOJOClasses") Set<Class<?>> queryPOJOClasses) {
        return queryPOJOClasses.stream().map(Class::getSimpleName).collect(Collectors.toSet());
    }

    @Bean
    public Map<BugType, List<Bug>> collectBugs() {
        final Map<BugType, List<Bug>> result = new HashMap<>();
        Arrays.stream(BugType.values()).forEach(t -> result.put(t, new ArrayList<>()));
        Arrays.stream(Bug.values()).forEach(b -> result.get(b.type()).add(b));
        return result;
    }

    @Bean
    public Map<BugType, Integer> bugsCount(final Map<BugType, List<Bug>> bugs) {
        return bugs.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().size()));
    }

    @Bean
    public InetUtils inetUtils(InetUtilsProperties properties) {
        properties.setTimeoutSeconds(0);
        return new InetUtils(properties);
    }

    private GroupedOpenApi initOpenApiDefinition(String group, String appVersion) {
        return GroupedOpenApi.builder()
                .group(group)
                .addOperationCustomizer(xRequestIdGlobalHeader())
                .addOperationCustomizer(acceptLocaleHeader())
                .addOperationCustomizer(sortResponses())
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

    private OperationCustomizer xRequestIdGlobalHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            if (operation.getTags().contains(BUG_TAG)) {
                return operation;
            }
            final Parameter xRequestId = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().type("integer"))
                    .name(RID)
                    .description(I18N_1648168060464)
                    .required(false);
            operation.addParametersItem(xRequestId);
            return operation;
        };
    }

    private OperationCustomizer sortResponses() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final ApiResponses responses = operation.getResponses();
            final Set<String> keys = new TreeSet<>(responses.keySet());
            final ApiResponses sortedApiResponses = new ApiResponses();
            keys.forEach(key -> sortedApiResponses.put(key, responses.get(key)));
            operation.setResponses(sortedApiResponses);
            return operation;
        };
    }

    private OperationCustomizer acceptLocaleHeader() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            final Parameter acceptLanguage = new Parameter()
                    .in(ParameterIn.HEADER.toString())
                    .schema(new StringSchema().addEnumItem("ru").addEnumItem("en"))
                    .name(LOCALE)
                    .description(I18N_1648168069261)
                    .required(false);
            operation.addParametersItem(acceptLanguage);
            return operation;
        };
    }

}
