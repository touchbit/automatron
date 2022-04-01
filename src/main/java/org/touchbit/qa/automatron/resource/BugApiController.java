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

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.pojo.bug.BugDTO;
import org.touchbit.qa.automatron.resource.mapping.GetRequest;
import org.touchbit.qa.automatron.resource.param.GetBugPathParameters;
import org.touchbit.qa.automatron.resource.spec.GetBugListSpec;
import org.touchbit.qa.automatron.resource.spec.GetBugSpec;
import org.touchbit.qa.automatron.util.AutomatronException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168263223;
import static org.touchbit.qa.automatron.constant.ResourceConstants.BUG_TAG;

@Slf4j
@RestController
@Tag(name = BUG_TAG, description = I18N_1648168263223)
public class BugApiController {

    @GetBugListSpec()
    @GetRequest(path = "/api/bugs")
    public Response<List<BugDTO>> getBugsLIst() {
        log.info("The method for getting the list of errors registered in the system has been called.");
        final List<BugDTO> responseBody = Arrays.stream(Bug.values())
                .map(BugDTO::new)
                .collect(Collectors.toList());
        log.info("Sending a list of {} defects.", responseBody.size());
        return new Response<>(responseBody, HttpStatus.OK);
    }

    @GetBugSpec()
    @GetRequest(path = "/api/bugs/{id}", responseMediaType = TEXT_PLAIN_VALUE)
    public Response<String> getBugById(@Valid GetBugPathParameters parameters) {
        log.info("Search for a defect with ID {}", parameters);
        if (parameters != null) {
            final Bug bug = Arrays.stream(Bug.values())
                    .filter(b -> b.id() == parameters.getId())
                    .findFirst().orElse(null);
            if (bug == null) {
                throw AutomatronException.http404("/api/bugs/" + parameters.getId());
            }
            log.info("Defect found. Sending defect information.");
            final String responseBody = new StringJoiner("\n")
                    .add("ID: " + bug.id())
                    .add("Type: " + bug.type())
                    .add("Info: " + bug.info())
                    .add("Description:")
                    .add(bug.description())
                    .add("\n\n")
                    .toString();
            return new Response<>(responseBody, HttpStatus.OK);
        }
        throw AutomatronException.http404("/api/bugs/null");
    }

}
