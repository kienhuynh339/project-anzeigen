/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccountName(String accountName);

    boolean existsByAccountName(String accountName);

}
