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
import org.h2.server.web.ConnectionInfo;
import org.h2.server.web.WebServer;
import org.h2.server.web.WebServlet;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "spring.h2.console", name = "enabled", havingValue = "true")
public class H2Config {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

    @Bean
    public ServletRegistrationBean<WebServlet> h2ConsoleRegistrationBean(final ServletRegistrationBean<WebServlet> h2c,
                                                                         final DataSourceProperties dataSourceProps) {
        h2c.setServlet(new WebServlet() {
            @Override
            public void init() {
                super.init();
                updateWebServlet(this, dataSourceProps);
            }
        });
        return h2c;
    }

    public void updateWebServlet(final WebServlet webServlet,
                                 final DataSourceProperties dataSourceProps) {
        try {
            final WebServer webServer = getWebServer(webServlet);
            updateWebServer(webServer, dataSourceProps);
        } catch (Exception ex) {
            log.error("Unable to set a custom ConnectionInfo for H2 console", ex);
        }
    }

    private WebServer getWebServer(final WebServlet webServlet) throws Exception {
        final Field field = WebServlet.class.getDeclaredField("server");
        field.setAccessible(true);
        return (WebServer) field.get(webServlet);
    }

    public void updateWebServer(final WebServer webServer,
                                final DataSourceProperties dataSourceProperties) throws Exception {
        String info = String.format("Generic Spring Datasource|%s|%s|%s", dataSourceProperties.determineDriverClassName(),
                dataSourceProperties.determineUrl(), dataSourceProperties.determineUsername());
        log.info("{}", info);
        final ConnectionInfo connectionInfo = new ConnectionInfo(info);
        final Method method = WebServer.class.getDeclaredMethod("updateSetting", ConnectionInfo.class);
        method.setAccessible(true);
        method.invoke(webServer, connectionInfo);
    }

}
