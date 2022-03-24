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


import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;

public class LocaleBundleProperties {

    public static final String CURRENT_HOST_PORT = "current_host_port";
    public static final String HEADER_REQUEST_ID_DESCRIPTION = "header_request_id_description";
    public static final String HEADER_LOCALE_DESCRIPTION = "header_locale_description";
    public static final String CONTROLLER_ACCOUNTING_DESCRIPTION = "controller_accounting_description";
    public static final String CONTROLLER_BUG_DESCRIPTION = "controller_bug_description";
    public static final String RESOURCE_ACCOUNTING_POST_LOGIN_DESCRIPTION = "resource_accounting_get_login_description";
    public static final String RESOURCE_ACCOUNTING_GET_LOGIN_200_DESCRIPTION = "resource_accounting_get_login_200_description";
    public static final String RESOURCE_ACCOUNTING_GET_LOGIN_400_DESCRIPTION = "resource_accounting_get_login_400_description";
    public static final String RESOURCE_GET_BUGS_DESCRIPTION = "resource_get_bugs_description";
    public static final String RESOURCE_GET_BUG_DESCRIPTION = "resource_get_bug_description";
    public static final String OPENAPI_DESCRIPTION = "openapi_description";
    public static final String OPENAPI_DESCRIPTION_LICENCE = "openapi_description_licence";
    public static final String LOGIN_DTO_DESCRIPTION = "login_dto_description";
    public static final String LOGIN_DTO_ADMIN_DESCRIPTION = "login_dto_admin_description";
    public static final String LOGIN_DTO_PASSWORD_DESCRIPTION = "login_dto_password_description";
    public static final String ERROR_DTO_CODE_DESCRIPTION = "error_dto_code_description";
    public static final String ERROR_DTO_SOURCE_DESCRIPTION = "error_dto_source_description";
    public static final String ERROR_DTO_REASON_DESCRIPTION = "error_dto_reason_description";
    public static final String BUG_DTO_ID_DESCRIPTION = "bug_dto_id";
    public static final String BUG_DTO_TYPE_DESCRIPTION = "bug_dto_type";
    public static final String BUG_DTO_INFO_DESCRIPTION = "bug_dto_info";

    public static final String BUG_001_INFO = "0001_info";
    public static final String BUG_001_DESCRIPTION = "0001_description";
    public static final String BUG_002_INFO = "0002_info";
    public static final String BUG_002_DESCRIPTION = "0002_description";
    public static final String BUG_003_INFO = "0003_info";
    public static final String BUG_003_DESCRIPTION = "0003_description";

    public static final String I18N_ERROR_4XX_DESCRIPTION = "i18n_error_4xx_description";
    public static final String I18N_ERROR_400_DESCRIPTION = "i18n_error_400_description";
    public static final String I18N_ERROR_401_DESCRIPTION = "i18n_error_401_description";
    public static final String I18N_ERROR_401_001_MESSAGE = "i18n_error_401_001_message";
    public static final String I18N_ERROR_403_DESCRIPTION = "i18n_error_403_description";
    public static final String I18N_ERROR_403_001_MESSAGE = "i18n_error_403_001_message";
    public static final String I18N_ERROR_5XX_DESCRIPTION = "i18n_error_5xx_description";
    public static final String I18N_ERROR_500_SYSTEM_DESCRIPTION = "i18n_error_500_system_description";
    public static final String I18N_ERROR_500_LOGIC_DESCRIPTION = "i18n_error_500_logic_description";
    public static final String I18N_ERROR_500_NETWORK_DESCRIPTION = "i18n_error_500_network_description";
    public static final String I18N_ERROR_500_001_MESSAGE = "i18n_error_500_001_message";
    public static final String I18N_ERROR_500_002_MESSAGE = "i18n_error_500_002_message";
    public static final String I18N_ERROR_500_003_MESSAGE = "i18n_error_500_003_message";

    public static List<String> getKeys() {
        return FieldUtils.getAllFieldsList(LocaleBundleProperties.class).stream()
                .filter(f -> Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
                .map(LocaleBundleProperties::readField)
                .collect(Collectors.toList());
    }

    private static String readField(final Field field) {
        try {
            return String.valueOf(LocaleBundleProperties.class.getField(field.getName()).get(null));
        } catch (Exception e) {
            throw new RuntimeException("Never throw", e);
        }
    }

}
