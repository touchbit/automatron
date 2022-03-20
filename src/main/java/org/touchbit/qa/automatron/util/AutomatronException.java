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

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.touchbit.qa.automatron.constant.LocaleBundleProperties.ERROR_401_001;
import static org.touchbit.qa.automatron.pojo.error.ErrorType.ACCESS;
import static org.touchbit.qa.automatron.pojo.error.ErrorType.SYSTEM;

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
        return new AutomatronException(UNAUTHORIZED, ACCESS, source, ERROR_401_001);
    }

    public static AutomatronException http500(String source, String reason) {
        return new AutomatronException(INTERNAL_SERVER_ERROR, SYSTEM, source, reason);
    }

}
