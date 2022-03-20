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

package org.touchbit.qa.automatron.pojo.bug;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.constant.Bug;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.*;

@Setter
@Getter
@Accessors(chain = true, fluent = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BugDTO {

    @JsonProperty("id")
    @Schema(description = BUG_DTO_ID_DESCRIPTION)
    @NotNull
    @Positive
    private Integer id;

    @JsonProperty("type")
    @Schema(description = BUG_DTO_TYPE_DESCRIPTION)
    @NotNull
    private Bug.BugType type;

    @JsonProperty("info")
    @Schema(description = BUG_DTO_INFO_DESCRIPTION)
    @NotNull
    private String info;

    public BugDTO(Bug bug) {
        this.id = bug.id();
        this.type = bug.type();
        this.info = bug.info();
    }

}
