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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.touchbit.qa.automatron.Application;
import org.touchbit.qa.automatron.annotation.PathPOJO;
import org.touchbit.qa.automatron.annotation.QueryPOJO;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.http.AutomatronHeadersResolver;
import org.touchbit.qa.automatron.interceptor.LocaleInterceptor;
import org.touchbit.qa.automatron.interceptor.XRequestIdInterceptor;
import org.touchbit.qa.automatron.util.AutomatronUtils;
import org.touchbit.qa.automatron.util.UpperCaseSourceEnumConverterFactory;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.touchbit.qa.automatron.constant.Bug.BugType;

/**
 * https://springdoc.github.io/springdoc-openapi-demos/faq.html
 */
@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new UpperCaseSourceEnumConverterFactory());
    }

    @Bean
    public Object transformRequestMappingHandlerAdapter(RequestMappingHandlerAdapter adapter) {
        final List<HandlerMethodArgumentResolver> commonArgumentResolvers = adapter.getArgumentResolvers();
        final List<HandlerMethodArgumentResolver> newArgumentResolvers = new ArrayList<>();
        newArgumentResolvers.add(new AutomatronHeadersResolver());
        if (commonArgumentResolvers != null)
            newArgumentResolvers.addAll(commonArgumentResolvers);
        adapter.setArgumentResolvers(newArgumentResolvers);
        return new Object();
    }

    @Bean("appVersion")
    public String appVersion() {
        String version = Application.class.getPackage().getImplementationVersion();
        return version == null || version.isBlank() ? "0.0.0" : version;
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

    @Bean(name = "pathPOJOClasses")
    public Set<Class<?>> pathPOJOClasses() {
        log.info("Search query POJO classes with annotation @{}", PathPOJO.class.getSimpleName());
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(PathPOJO.class));
        return AutomatronUtils.getBeanDefinitionClasses(scanner);
    }

    @Bean(name = "pathPOJOClassSimpleNames")
    public Set<String> pathPOJOClassSimpleNames(@Qualifier("pathPOJOClasses") Set<Class<?>> queryPOJOClasses) {
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
        return new InetUtils(properties) {

            public HostInfo findFirstNonLoopbackHostInfo() {
                HostInfo hostInfo = new HostInfo();
                hostInfo.setHostname(properties.getDefaultHostname());
                hostInfo.setIpAddress(properties.getDefaultIpAddress());
                return hostInfo;
            }

        };
    }

}
