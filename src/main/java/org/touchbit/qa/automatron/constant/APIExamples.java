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

    public static final String EX_ACCOUNTING_LOGIN_400 = """
            [{
              "type": "CONTRACT",
              "source": "authentication.password",
              "reason": "must not be null"
            }]
            """;

    public static final String EX_ACCOUNTING_LOGOUT_400 = """
            [{
              "type": "CONTRACT",
              "source": "*.bearerAuthorizationHeader",
              "reason": "не должно равняться null"
            }]
            """;

    public static final String EX_CODE_401_001 = """
            [{
              "type": "ACCESS",
              "source": "login/password",
              "reason": "I18N_1648168111078"
            }]
            """;

    public static final String EX_CODE_401_002 = """
            [{
              "type": "ACCESS",
              "source": "*.bearerAuthorizationHeader",
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

    public static final String EX_CODE_403_002 = """
            [{
              "type": "ACCESS",
              "source": "User{status=BLOCKED}",
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

    public static final String AUTH_DTO = """
            {
              "access_token": "d0b8b863-3f47-4881-96b8-68c4b4e4f892",
              "access_expires_in": 86400,
              "refresh_expires_in": 86400,
              "refresh_token": "bfb35bf5-311b-4e77-8b04-82582cc5a5f2",
              "token_type": "bearer"
            }
            """;

    public static final String BUG_LIST = """
            [
              {
                "id": 42,
                "type": "UNPROCESSABLE",
                "info": "The answer to life, universe and everything."
              }
            ]
            """;

    public static final String BUG_INFO_EXAMPLE = """
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
