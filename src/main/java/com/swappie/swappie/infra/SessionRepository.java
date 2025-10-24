package com.swappie.swappie.infra;

import com.swappie.swappie.domain.SessionRecord;
import com.swappie.swappie.domain.UserRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionRecord, String> {
    boolean existsByUser(UserRecord user);
}
