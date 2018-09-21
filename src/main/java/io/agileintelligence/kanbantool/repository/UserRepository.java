package io.agileintelligence.kanbantool.repository;

import io.agileintelligence.kanbantool.domain.User;
import org.hibernate.annotations.Synchronize;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
    User getById(Long id);

}
