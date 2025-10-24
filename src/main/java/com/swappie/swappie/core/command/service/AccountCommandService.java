package com.swappie.swappie.core.command.service;

import com.swappie.swappie.core.command.mapper.CommandMapper;
import com.swappie.swappie.domain.AccountRecord;
import com.swappie.swappie.domain.UserRecord;
import com.swappie.swappie.infra.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCommandService {
    private final AccountRepository accountRepository;

    public void saveRecord(UserRecord userRecord) {
        AccountRecord record = CommandMapper.mapAccountRecord(userRecord);

        accountRepository.save(record);
    }
}
