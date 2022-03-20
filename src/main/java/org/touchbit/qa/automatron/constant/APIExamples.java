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

    public static final String API_ACCOUNTING_LOGIN_404 = """
            [
              {
                "type": "CONTRACT",
                "source": "loginDTO.password",
                "reason": "must not be null"
              }
            ]
            """;

    public static final String EX_API_500 = """
            [
              {
                "type": "SYSTEM",
                "source": "IllegalArgumentException",
                "reason": "Invalid UUID string: bae8b1a6-26c9-4b3a-9de3"
              }
            ]
            """;

    public static final String AUTH_DTO = """
            {
              "access_token": "bae8b1a6-26c9-4b3a-9de3-dcdb368a207d",
              "refresh_token": "8ea9f275-9314-48cb-bf25-2bb6d8141e91",
              "expires_in": 86400,
              "refresh_expires_in": 86400,
              "token_type": "bearer",
              "not-before-policy": 0,
              "session_state": "273bada6-2f11-4e7e-ac40-b74305e57314",
              "scope": "profile email"
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
