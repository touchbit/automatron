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

package org.touchbit.qa.automatron.advice;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.touchbit.qa.automatron.pojo.bug.BugDTO;
import org.touchbit.qa.automatron.pojo.error.ErrorDTO;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.touchbit.qa.automatron.constant.I18N.getKeys;
import static org.touchbit.qa.automatron.constant.ResourceConstants.LOCALE;

@Slf4j
@RestControllerAdvice
@AllArgsConstructor
public class LocaleAdviser implements BodyAdvice {

    private static final String CURRENT_HOST_PORT = "current_host_port";
    private static final String OPENAPI_DESCRIPTION_LICENCE = "openapi_description_licence";

    private ResourceBundleMessageSource messageSource;
    private URL serverAddress;

    @Override
    public Object beforeBodyWrite(final Object body, MethodParameter returnType,
                                  final MediaType selectedContentType,
                                  final Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  final ServerHttpRequest request,
                                  final ServerHttpResponse response) {
        final String path = request.getURI().getPath();
        final String bodyTypeName = body == null ? "null" : body.getClass().getName();
        final Locale locale = getLocale(request);
        final Object localizedBody;
        log.debug("Locale: language tag: {}", locale);
        log.debug("Locale: request path: {}", path);
        log.debug("Locale: response body type: {}", bodyTypeName);
        if (isApiDocs(request)) {
            localizedBody = getLocalizedApiDocResponseBody(body, request);
        } else if (isBugResponseBody(body)) {
            localizedBody = getLocalizedBugResponseBody(body, locale);
        } else if (isErrorResponseBody(body)) {
            localizedBody = getLocalizedErrorResponseBody(body, locale);
        } else if (body instanceof String stringBody) {
            localizedBody = localizeString(stringBody, locale);
        } else {
            localizedBody = body;
        }
        return localizedBody;
    }

    private Object getLocalizedErrorResponseBody(Object body, Locale locale) {
        if (body instanceof ErrorDTO errorDTO) {
            return localizeErrorDTO(errorDTO, locale);
        }
        if (body instanceof Collection<?> collection) {
            if (log.isDebugEnabled()) {
                final String names = String.join("\n - ", collection.stream()
                        .filter(Objects::nonNull)
                        .map(item -> item.getClass().getName())
                        .collect(Collectors.toSet()));
                if (!names.isEmpty()) {
                    log.debug("Locale: collection types:\n - {}", names);
                }
            }
            for (Object item : collection) {
                if (item instanceof ErrorDTO errorDTO) {
                    localizeErrorDTO(errorDTO, locale);
                }
            }
        }
        return body;
    }

    private Object getLocalizedBugResponseBody(final Object body, final Locale locale) {
        if (body instanceof BugDTO bugDTO) {
            return localizeBugDTO(bugDTO, locale);
        }
        if (body instanceof Collection<?> collection) {
            if (log.isDebugEnabled()) {
                final String names = String.join("\n - ", collection.stream()
                        .filter(Objects::nonNull)
                        .map(item -> item.getClass().getName())
                        .collect(Collectors.toSet()));
                if (!names.isEmpty()) {
                    log.debug("Locale: collection types:\n - {}", names);
                }
            }
            for (Object item : collection) {
                if (item instanceof BugDTO bugDTO) {
                    localizeBugDTO(bugDTO, locale);
                }
            }
        }
        return body;
    }

    private Object getLocalizedApiDocResponseBody(final Object body, final ServerHttpRequest request) {
        final String bodyTypeName = body == null ? "null" : body.getClass().getName();
        if (body instanceof String stringBody) {
            if (isApiDocsDefinition(request)) {
                setDefaultLocaleFromDefinition(request);
                log.debug("Default Locale set: {}", Locale.getDefault());
            }
            String result = localizeString(stringBody, Locale.getDefault());
            if (result.contains(OPENAPI_DESCRIPTION_LICENCE)) {
                try {
                    final String licence = AutomatronUtils.getLicence(Locale.getDefault())
                            .replaceAll("\n", "<br>").replaceAll("\"", "'");
                    result = result.replaceAll(OPENAPI_DESCRIPTION_LICENCE, licence);
                } catch (IOException e) {
                    throw new RuntimeException("Unable to get license file", e);
                }
            }
            result = result.replaceAll(CURRENT_HOST_PORT, String.valueOf(serverAddress));
            return result;
        } else {
            log.debug("Locale of the openapi document has not been done. " +
                      "The body is not in String format: {}", bodyTypeName);
        }
        return body;
    }

    public boolean isBugResponseBody(Object body) {
        if (body instanceof BugDTO) {
            return true;
        }
        if (body instanceof Collection<?> collection) {
            return collection.stream().allMatch(i -> i instanceof BugDTO);
        }
        return false;
    }

    private boolean isErrorResponseBody(Object body) {
        if (body instanceof ErrorDTO) {
            return true;
        }
        if (body instanceof Collection<?> collection) {
            return collection.stream().allMatch(i -> i instanceof ErrorDTO);
        }
        return false;
    }

    private Object localizeBugDTO(final BugDTO bugDTO, Locale locale) {
        final String info = bugDTO.info();
        final String message = messageSource.getMessage(info, null, locale);
        return bugDTO.info(message);
    }

    private Object localizeErrorDTO(final ErrorDTO errorDTO, Locale locale) {
        final String rawSource = errorDTO.source();
        if (rawSource != null) {
            errorDTO.source(messageSource.getMessage(rawSource, null, locale));
        }
        final String rawReason = errorDTO.reason();
        if (rawReason != null) {
            errorDTO.reason(messageSource.getMessage(rawReason, null, locale));
        }
        return errorDTO;
    }

    private String localizeString(final String source, Locale locale) {
        String result = source;
        for (String key : getKeys()) {
            final String rawValue = messageSource.getMessage(key, null, locale);
            final String value = rawValue.replaceAll("\"", "'");
            result = result.replaceAll(key, value);
        }
        return result;
    }

    private boolean isApiDocs(final ServerHttpRequest request) {
        return request.getURI().toString().contains("/v3/api-docs");
    }

    private boolean isApiDocsDefinition(final ServerHttpRequest request) {
        final String uri = request.getURI().toString();
        return uri.contains("/v3/api-docs/Russian") || uri.contains("/v3/api-docs/English");
    }

    private void setDefaultLocaleFromDefinition(final ServerHttpRequest request) {
        final String uri = request.getURI().toString();
        if (uri.contains("/v3/api-docs/Russian")) {
            Locale.setDefault(Locale.forLanguageTag("ru"));
        }
        if (uri.contains("/v3/api-docs/English")) {
            Locale.setDefault(Locale.forLanguageTag("en"));
        }
    }

    private Locale getLocale(final ServerHttpRequest request) {
        final List<String> localeList = request.getHeaders().get(LOCALE);
        if (localeList != null && !localeList.isEmpty()) {
            // take last repeatable header value
            final String headerValue = localeList.get(localeList.size() - 1);
            if (!"ru".equalsIgnoreCase(headerValue) && !"en".equalsIgnoreCase(headerValue)) {
                log.warn("Invalid '{}' header value: {}", LOCALE, headerValue);
                log.debug("The default language tag is used.");
                return Locale.getDefault();
            } else {
                log.debug("The language tag from the request header is used.");
                return Locale.forLanguageTag(headerValue);
            }
        } else {
            log.debug("The default language tag is used.");
            return Locale.getDefault();
        }
    }

}
