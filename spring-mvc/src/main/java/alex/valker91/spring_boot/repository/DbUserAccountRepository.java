package alex.valker91.spring_boot.repository;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;
import alex.valker91.spring_boot.entity.UserAccountDb;

public interface DbUserAccountRepository extends CrudRepository<UserAccountDb, Long> {
    Optional<UserAccountDb> findByUserId(long userId);
}
