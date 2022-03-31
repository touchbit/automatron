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

package org.touchbit.qa.automatron.resource.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648168278936;
import static org.touchbit.qa.automatron.constant.ResourceConstants.BUG_TAG;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Operation(tags = BUG_TAG, summary = I18N_1648168278936, responses = {
        @ApiResponse(responseCode = "200", description = "Completed successfully", content = {
                @Content(examples = {@ExampleObject(GetBugSpec.EXAMPLE)})}),
})
public @interface GetBugSpec {

    String EXAMPLE = """
            ID: 42
            Type: UNPROCESSABLE
            Info: The answer to life, universe and everything.
            Description:
            The number 42 is especially significant to fans of science fiction
            novelist Douglas Adams’ “The Hitchhiker’s Guide to the Galaxy,”
            because that number is the answer given by a supercomputer to
            “the Ultimate Question of Life, the Universe, and Everything.”
            """;

}
