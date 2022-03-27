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
import org.springframework.transaction.annotation.Transactional;
import org.touchbit.qa.automatron.db.entity.User;
import org.touchbit.qa.automatron.db.entity.UserStatus;
import org.touchbit.qa.automatron.db.entity.UserType;

import java.util.List;

@Transactional
public interface UserRepository extends JpaRepository<User, Long> {


    User findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("""
           SELECT c FROM User c WHERE
             (:id is null or c.id = :id) and
             (:login is null or c.login = :login) and
             (:status is null or c.status = :status) and
             (:type is null or c.type = :type)
           """)
    List<User> findAllByFilter(Long id, String login, UserStatus status, UserType type);

}
