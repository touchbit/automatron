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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.touchbit.qa.automatron.constant.Bug.BugType.*;
import static org.touchbit.qa.automatron.constant.I18N.*;
import static org.touchbit.qa.automatron.constant.ResourceConstants.RID;

@Slf4j
@Getter
@Accessors(chain = true, fluent = true)
public enum Bug {

    BUG_0001(1, SECURITY, I18N_1648168652178, I18N_1648168660813),
    BUG_0002(2, SPECIFICATION, I18N_1648168667369, I18N_1648168674474),
    BUG_0003(3, IMPLEMENTATION, I18N_1648169003925, I18N_1648169012151),
    BUG_0004(4, IMPLEMENTATION, I18N_1648401705690, I18N_1648401737946),
    BUG_0005(5, IMPLEMENTATION, I18N_1648691005453, I18N_1648691079913),
    BUG_0006(6, IMPLEMENTATION, I18N_1648691290758, I18N_1648691345099),
    BUG_0007(7, SECURITY, I18N_1648766329247, I18N_1648766458371),
    BUG_0008(8, ARCHITECTURE, I18N_1648769955903, I18N_1648770063686),
    BUG_0009(9, IMPLEMENTATION, I18N_1648790774801, I18N_1648790967129),
    BUG_0010(10, ARCHITECTURE, I18N_1648791832080, I18N_1648792130782),
    BUG_0011(11, SPECIFICATION, I18N_1648871671095, I18N_1648871756780),
    BUG_0012(12, SECURITY, I18N_1648872173032, I18N_1648872223112),
    BUG_0013(13, IMPLEMENTATION, I18N_1648879561626, I18N_1648879598244),
    ;

    private static final ConcurrentMap<String, List<Bug>> BUGS = new ConcurrentHashMap<>();
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

    public static void register(final Bug bug) {
        final String rid = MDC.get(RID);
        if (rid == null || rid.isBlank()) {
            log.warn("Unable to link bug to request. '{}' header not passed.", RID);
            return;
        }
        BUGS.computeIfAbsent(rid, i -> new ArrayList<>()).add(bug);
        log.warn("Bug registered: {}", bug.id());
    }

    public static List<Bug> takeRegistered() {
        final String rid = MDC.get(RID);
        log.trace("Get registered bugs for request=id: {}", rid);
        try {
            if (rid != null) {
                final List<Bug> bugs = BUGS.get(rid);
                if (bugs != null) {
                    return bugs;
                }
            }
            return new ArrayList<>();
        } finally {
            BUGS.remove(rid);
        }
    }

    public String stringId() {
        return String.valueOf(id);
    }

    public enum BugType {
        SPECIFICATION,
        IMPLEMENTATION,
        ARCHITECTURE,
        SECURITY,
    }

}
