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

public class APIExamples {

    public static final String EX_CODE_401_002 = """
            [{
              "type": "ACCESS",
              "source": "Header.Authorization",
              "reason": "I18N_1648168111078"
            }]
            """;

    public static final String EX_CODE_403_001 = """
            [{
              "type": "ACCESS",
              "source": "User{type=MEMBER}",
              "reason": "I18N_1648168132812"
            }]
            """;

    public static final String API_CONDITION_ERR_500 = """
            [{
              "type": "CONDITION",
              "source": "User{id=1, status=null}",
              "reason": "I18N_1648168178176"
            }]
            """;

    public static final String API_NETWORK_ERR_500 = """
            [{
              "type": "NETWORK",
              "source": "JdbcSQLException",
              "reason": "I18N_1648168186580"
            }]
            """;

    public static final String API_SYSTEM_ERR_500 = """
            [{
              "type": "SYSTEM",
              "source": "TransactionSystemException",
              "reason": "I18N_1648168193476"
            }]
            """;

}
