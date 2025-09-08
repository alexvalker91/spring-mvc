package alex.valker91.spring_boot.repository;

import org.springframework.data.repository.CrudRepository;
import alex.valker91.spring_boot.entity.UserAccountDb;

public interface DbUserAccountRepository extends CrudRepository<UserAccountDb, Long> {
}
