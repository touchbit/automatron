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

package org.touchbit.qa.automatron.admin;

import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.touchbit.qa.automatron.pojo.admin.ConfigurationDTO;
import org.touchbit.qa.automatron.pojo.admin.OpenAPIConfig;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestControllerEndpoint(id = "config")
public class AdminConfigViewEndpoint {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/", produces = APPLICATION_JSON_VALUE)
    public ConfigurationDTO getConfiguration() {
        return new ConfigurationDTO()
                .openapi(new OpenAPIConfig()
                        .enableDefaultLocaleHeader(true)
                        .enableDefaultRequestIdHeader(true)
                        .enableDefault5xxResponse(true));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ConfigurationDTO todo(RequestEntity<ConfigurationDTO> value) {
        return value.getBody();
    }

}
