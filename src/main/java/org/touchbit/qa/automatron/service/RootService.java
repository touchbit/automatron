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

package org.touchbit.qa.automatron.service;

import lombok.AllArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.touchbit.qa.automatron.util.AutomatronUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import static org.touchbit.qa.automatron.advice.LocaleAdviser.CURRENT_HOST_PORT;
import static org.touchbit.qa.automatron.advice.LocaleAdviser.OPENAPI_DESCRIPTION_LICENCE;

@Service
@AllArgsConstructor
public class RootService {

    private URL serverAddress;

    public String getRootMarkdownHtml(Locale locale) throws IOException {
        final String lang = locale.getLanguage().toLowerCase();
        String markdown = AutomatronUtils.readResource("i18n/root_" + lang + ".md");
        if (markdown.contains(OPENAPI_DESCRIPTION_LICENCE)) {
            final String licence = AutomatronUtils.getLicence(locale).replaceAll("\n", "<br>").replaceAll("\"", "'");
            markdown = markdown.replaceAll(OPENAPI_DESCRIPTION_LICENCE, licence);
        }
        markdown = markdown.replaceAll(CURRENT_HOST_PORT, String.valueOf(serverAddress));
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

}
