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

package org.touchbit.qa.automatron.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.touchbit.qa.automatron.constant.UserRole;
import org.touchbit.qa.automatron.constant.UserStatus;
import org.touchbit.qa.automatron.db.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {

    User findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("""
            SELECT c FROM User c WHERE
              (:login is null or c.login = :login) and
              (:status is null or c.status = :status) and
              (:role is null or c.role = :role)
            """)
    List<User> findAllByFilter(String login, UserStatus status, UserRole role);

}
