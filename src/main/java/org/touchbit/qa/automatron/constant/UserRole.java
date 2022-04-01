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

import io.swagger.v3.oas.annotations.media.Schema;

import static org.touchbit.qa.automatron.constant.I18N.I18N_1648673602540;

@Schema(description = I18N_1648673602540, example = "MEMBER")
public enum UserRole {

    MEMBER,
    ADMIN,
    OWNER,
    ;

    public boolean canChangeUserRoleTo(UserRole target, boolean isSelfChange) {
        if (this.equals(MEMBER) && target.equals(MEMBER) && isSelfChange) {
            return true;
        }
        if (this.equals(ADMIN) && (target.equals(MEMBER))) {
            return true;
        }
        if (this.equals(ADMIN) && target.equals(ADMIN) && isSelfChange) {
            return true;
        }
        if (this.equals(OWNER) && (target.equals(ADMIN) || target.equals(MEMBER))) {
            return true;
        }
        return this.equals(OWNER) && target.equals(OWNER) && isSelfChange;
    }

}
