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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.touchbit.qa.automatron.constant.Bug;
import org.touchbit.qa.automatron.pojo.bug.BugDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.touchbit.qa.automatron.constant.APIExamples.BUG_INFO_EXAMPLE;
import static org.touchbit.qa.automatron.constant.APIExamples.BUG_LIST;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.BUG_TAG;

@Slf4j
@RestController
@Tag(name = BUG_TAG, description = I18N_1648168263223)
public class BugApiController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Completed successfully", content = {@Content(mediaType = APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation = BugDTO.class)), examples = {@ExampleObject(BUG_LIST)})}),
    })
    @Operation(tags = BUG_TAG, summary = I18N_1648168270342)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/bugs")
    public List<BugDTO> getBugs() {
        log.info("The method for getting the list of errors registered in the system has been called.");
        final List<BugDTO> defects = Arrays.stream(Bug.values())
                .map(BugDTO::new)
                .collect(Collectors.toList());
        log.info("Sending a list of {} defects.", defects.size());
        return defects;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Completed successfully", content = {@Content(examples = {@ExampleObject(BUG_INFO_EXAMPLE)})}),
    })
    @Operation(tags = BUG_TAG, summary = I18N_1648168278936)
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/api/bug", produces = TEXT_PLAIN_VALUE)
    public String getBugs(@RequestParam(value = "id") @Valid @Min(1) Integer bugId) {
        log.info("Search for a defect with ID {}", bugId);
        final Bug bug = Arrays.stream(Bug.values())
                .filter(b -> b.id() == bugId)
                .findFirst().orElse(null);
        if (bug == null) {
            throw new RuntimeException("ToDo");
        }
        log.info("Defect found. Sending defect information.");
        return new StringJoiner("\n")
                .add("ID: " + bug.id())
                .add("Type: " + bug.type())
                .add("Info: " + bug.info())
                .add("Description:")
                .add(bug.description())
                .add("\n\n")
                .toString();
    }

}
