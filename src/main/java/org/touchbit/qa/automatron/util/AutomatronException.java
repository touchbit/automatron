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

package org.touchbit.qa.automatron.util;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;
import org.touchbit.qa.automatron.pojo.error.ErrorType;

import static org.springframework.http.HttpStatus.*;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.pojo.error.ErrorType.*;

@Getter
@Accessors(chain = true, fluent = true)
public class AutomatronException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorType type;
    private final String source;
    private final String reason;

    public AutomatronException(HttpStatus status, ErrorType type, String source, String reason) {
        this.status = status;
        this.type = type;
        this.source = source;
        this.reason = reason;
    }

    public static AutomatronException http401(String source) {
        return new AutomatronException(UNAUTHORIZED, ACCESS, source, I18N_1648168111078);
    }

    public static AutomatronException http403(String source) {
        return new AutomatronException(FORBIDDEN, ACCESS, source, I18N_1648168132812);
    }

    public static AutomatronException http404(String source) {
        return new AutomatronException(NOT_FOUND, CONDITION, source, I18N_1648704047156);
    }

    public static AutomatronException http409(String source) {
        return new AutomatronException(CONFLICT, CONDITION, source, I18N_1648765587105);
    }

    public static AutomatronException http500(String source, String reason) {
        return new AutomatronException(INTERNAL_SERVER_ERROR, SYSTEM, source, reason);
    }

}
