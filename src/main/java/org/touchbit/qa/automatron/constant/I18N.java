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

public class I18N {

    public static final String I18N_1648700713896 = "I18N_1648700713896";
    public static final String I18N_1648700640862 = "I18N_1648700640862";
    public static final String I18N_1648700575095 = "I18N_1648700575095";
    public static final String I18N_1648700334011 = "I18N_1648700334011";
    public static final String I18N_1648700259699 = "I18N_1648700259699";
    public static final String I18N_1648691345099 = "I18N_1648691345099";
    public static final String I18N_1648691290758 = "I18N_1648691290758";
    public static final String I18N_1648691079913 = "I18N_1648691079913";
    public static final String I18N_1648691005453 = "I18N_1648691005453";
    public static final String I18N_1648689343652 = "I18N_1648689343652";
    public static final String I18N_1648688789502 = "I18N_1648688789502";
    public static final String I18N_1648688724365 = "I18N_1648688724365";
    public static final String I18N_1648685296628 = "I18N_1648685296628";
    public static final String I18N_1648685056666 = "I18N_1648685056666";
    public static final String I18N_1648684256974 = "I18N_1648684256974";
    public static final String I18N_1648684060672 = "I18N_1648684060672";
    public static final String I18N_1648678859481 = "I18N_1648678859481";
    public static final String I18N_1648678768714 = "I18N_1648678768714";
    public static final String I18N_1648673602540 = "I18N_1648673602540";
    public static final String I18N_1648673498876 = "I18N_1648673498876";
    public static final String I18N_1648401737946 = "I18N_1648401737946";
    public static final String I18N_1648401705690 = "I18N_1648401705690";
    public static final String I18N_1648399645845 = "I18N_1648399645845";
    public static final String I18N_1648397948840 = "I18N_1648397948840";
    public static final String I18N_1648397690214 = "I18N_1648397690214";
    public static final String I18N_1648397372228 = "I18N_1648397372228";
    public static final String I18N_1648168060464 = "I18N_1648168060464";
    public static final String I18N_1648168069261 = "I18N_1648168069261";
    public static final String I18N_1648168206689 = "I18N_1648168206689";
    public static final String I18N_1648168263223 = "I18N_1648168263223";
    public static final String I18N_1648168212897 = "I18N_1648168212897";
    public static final String I18N_1648168229890 = "I18N_1648168229890";
    public static final String I18N_1648168270342 = "I18N_1648168270342";
    public static final String I18N_1648168278936 = "I18N_1648168278936";
    public static final String I18N_1648167967145 = "I18N_1648167967145";
    public static final String I18N_1648168739660 = "I18N_1648168739660";
    public static final String I18N_1648168744616 = "I18N_1648168744616";
    public static final String I18N_1648168710928 = "I18N_1648168710928";
    public static final String I18N_1648168716654 = "I18N_1648168716654";
    public static final String I18N_1648168723241 = "I18N_1648168723241";
    public static final String I18N_1648168751163 = "I18N_1648168751163";
    public static final String I18N_1648168758028 = "I18N_1648168758028";
    public static final String I18N_1648168762498 = "I18N_1648168762498";
    public static final String I18N_1648168652178 = "I18N_1648168652178";
    public static final String I18N_1648168660813 = "I18N_1648168660813";
    public static final String I18N_1648168667369 = "I18N_1648168667369";
    public static final String I18N_1648168674474 = "I18N_1648168674474";
    public static final String I18N_1648169003925 = "I18N_1648169003925";
    public static final String I18N_1648169012151 = "I18N_1648169012151";
    public static final String I18N_1648168086907 = "I18N_1648168086907";
    public static final String I18N_1648168095253 = "I18N_1648168095253";
    public static final String I18N_1648168104107 = "I18N_1648168104107";
    public static final String I18N_1648168111078 = "I18N_1648168111078";
    public static final String I18N_1648168125864 = "I18N_1648168125864";
    public static final String I18N_1648168132812 = "I18N_1648168132812";
    public static final String I18N_1648168141132 = "I18N_1648168141132";
    public static final String I18N_1648168152659 = "I18N_1648168152659";
    public static final String I18N_1648168162035 = "I18N_1648168162035";
    public static final String I18N_1648168167873 = "I18N_1648168167873";
    public static final String I18N_1648168178176 = "I18N_1648168178176";

    // ---------------------------- EXAMPLES ----------------------------
    public static final String I18N_1648673246598 = "I18N_1648673246598";
    public static final String I18N_1648168186580 = "I18N_1648168186580";
    public static final String I18N_1648168193476 = "I18N_1648168193476";
    public static final String I18N_1648688495987 = "I18N_1648688495987";
    public static final String I18N_1648168250730 = "I18N_1648168250730";

    public static List<String> getKeys() {
        return FieldUtils.getAllFieldsList(I18N.class).stream()
                .filter(f -> Modifier.isStatic(f.getModifiers()) && Modifier.isFinal(f.getModifiers()))
                .map(I18N::readField)
                .collect(Collectors.toList());
    }

    private static String readField(final Field field) {
        try {
            return String.valueOf(I18N.class.getField(field.getName()).get(null));
        } catch (Exception e) {
            throw new RuntimeException("Never throw", e);
        }
    }

}
