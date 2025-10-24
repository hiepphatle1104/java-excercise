package com.swappie.swappie.infra;

import com.swappie.swappie.domain.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserRecord, String> {
    boolean existsByUsername(String username);

    UserRecord findByUsername(String username);
}
