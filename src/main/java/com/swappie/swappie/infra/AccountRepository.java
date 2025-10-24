package com.swappie.swappie.infra;

import com.swappie.swappie.domain.AccountRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountRecord, String> {
}
