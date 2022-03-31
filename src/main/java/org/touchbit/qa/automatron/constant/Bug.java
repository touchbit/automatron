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

package org.touchbit.qa.automatron.constant;

import lombok.Getter;
import lombok.experimental.Accessors;

import static org.touchbit.qa.automatron.constant.Bug.BugType.*;
import static org.touchbit.qa.automatron.constant.I18N.*;

@Getter
@Accessors(chain = true, fluent = true)
public enum Bug {

    BUG_0001(1, SECURITY, I18N_1648168652178, I18N_1648168660813),
    BUG_0002(2, SPECIFICATION, I18N_1648168667369, I18N_1648168674474),
    BUG_0003(3, IMPLEMENTATION, I18N_1648169003925, I18N_1648169012151),
    BUG_0004(4, IMPLEMENTATION, I18N_1648401705690, I18N_1648401737946),
    BUG_0005(5, IMPLEMENTATION, I18N_1648691005453, I18N_1648691079913),
    BUG_0006(6, IMPLEMENTATION, I18N_1648691290758, I18N_1648691345099),
    ;

    private final int id;
    private final BugType type;
    private final String info;
    private final String description;

    Bug(int id, BugType type, String info, String description) {
        this.id = id;
        this.type = type;
        this.info = info;
        this.description = description;
    }

    public String stringId() {
        return String.valueOf(id);
    }

    public enum BugType {
        SPECIFICATION,
        IMPLEMENTATION,
        SECURITY,
    }

}
