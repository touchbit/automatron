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
import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.*;

@Getter
@Accessors(chain = true, fluent = true)
public enum Bug {

    BUG_0001(1, SECURITY, BUG_001_INFO, BUG_001_DESCRIPTION),
    BUG_0002(2, SPECIFICATION, BUG_002_INFO, BUG_002_DESCRIPTION),
    BUG_0003(3, IMPLEMENTATION, BUG_003_INFO, BUG_003_DESCRIPTION),
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
        SECURITY,
        SPECIFICATION,
        IMPLEMENTATION,
    }

}
