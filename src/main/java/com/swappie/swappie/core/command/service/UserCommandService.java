package com.swappie.swappie.core.command.service;

import com.swappie.swappie.common.exception.*;
import com.swappie.swappie.core.command.mapper.CommandMapper;
import com.swappie.swappie.core.command.models.SignInCommand;
import com.swappie.swappie.core.command.models.SignUpCommand;
import com.swappie.swappie.domain.SessionRecord;
import com.swappie.swappie.domain.UserRecord;
import com.swappie.swappie.domain.UserStatus;
import com.swappie.swappie.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AccountCommandService accountCommandService;
    private final SessionCommandService sessionCommandService;

    @Transactional(rollbackFor = BaseException.class)
    public SessionRecord processCommand(SignInCommand cmd) {
        if (!userRepository.existsByUsername(cmd.username()))
            throw new UserNotFoundException();

        UserRecord record = userRepository.findByUsername(cmd.username());
        if (sessionCommandService.isRevoked(record))
            throw new SessionAlreadyExistException();

        return sessionCommandService.saveRecord(record);
    }

    @Transactional(rollbackFor = BaseException.class)
    public UserRecord processCommand(SignUpCommand cmd) {
        if (!cmd.password().equals(cmd.confirmPassword()))
            throw new PasswordDoesNotMatchException();

        if (userRepository.existsByUsername(cmd.username()))
            throw new UsernameAlreadyExistException();

        UserRecord record = saveRecord(cmd);

        accountCommandService.saveRecord(record);
        record.setStatus(UserStatus.ACTIVE);

        if (cmd.username().equals("test_exception"))
            throw new TestException();

        return userRepository.save(record);
    }

    private UserRecord saveRecord(SignUpCommand command) {
        UserRecord record = CommandMapper.mapUserRecord(command);
        record.setPasswordHash(passwordEncoder.encode(command.password()));

        return userRepository.save(record);
    }
}
