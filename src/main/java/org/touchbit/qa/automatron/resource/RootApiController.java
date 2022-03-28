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

package org.touchbit.qa.automatron.resource;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.touchbit.qa.automatron.service.RootService;

import java.io.IOException;
import java.util.Locale;

@Slf4j
@RestController
@AllArgsConstructor
public class RootApiController {

    private RootService rootService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/")
    public ModelAndView getBugs(@RequestHeader(name = "Accept-Language", required = false) String acceptLanguage,
                                String lang) throws IOException {
        final Locale reqLocale;
        if (lang != null) {
            reqLocale = Locale.forLanguageTag(lang);
        } else if (acceptLanguage != null) {
            reqLocale = Locale.forLanguageTag(acceptLanguage);
        } else {
            reqLocale = Locale.getDefault();
        }
        String language = reqLocale.getLanguage();
        final Locale locale;
        if (language.equalsIgnoreCase("ru") || language.equalsIgnoreCase("en")) {
            locale = Locale.forLanguageTag(language);
        } else {
            locale = Locale.getDefault();
        }
        final String switchLanguage = locale.getLanguage().equalsIgnoreCase("ru") ? "EN" : "RU";
        final ModelAndView modelAndView = new ModelAndView("root");
        final String htmlContent = rootService.getRootMarkdownHtml(locale);
        modelAndView.addObject("htmlContent", htmlContent);
        modelAndView.addObject("lang", switchLanguage);
        modelAndView.addObject("lang_href", "?lang=" + switchLanguage);
        return modelAndView;
    }

}
