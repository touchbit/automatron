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

package org.touchbit.qa.automatron.pojo.accounting;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.touchbit.qa.automatron.constant.PhoneType;
import org.touchbit.qa.automatron.pojo.POJO;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648685056666;
import static org.touchbit.qa.automatron.constant.I18N.I18N_1648685296628;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@Builder
@AllArgsConstructor
@Schema(description = I18N_1648685296628)
public class PhoneNumberDTO extends POJO {

    @JsonProperty("phone")
    @Schema(description = I18N_1648685056666, example = "+79990000000")
    private String phone;

    private PhoneType type;

}
