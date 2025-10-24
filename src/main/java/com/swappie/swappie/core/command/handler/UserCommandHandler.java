package com.swappie.swappie.core.command.handler;

import com.swappie.swappie.common.dto.SignInResponse;
import com.swappie.swappie.common.dto.SignUpResponse;
import com.swappie.swappie.core.command.mapper.CommandMapper;
import com.swappie.swappie.core.command.models.SignInCommand;
import com.swappie.swappie.core.command.models.SignUpCommand;
import com.swappie.swappie.core.command.service.UserCommandService;
import com.swappie.swappie.domain.SessionRecord;
import com.swappie.swappie.domain.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandHandler {
    private final UserCommandService userCommandService;

    public SignUpResponse handle(SignUpCommand cmd) {
        UserRecord record = userCommandService.processCommand(cmd);

        return CommandMapper.mapToSignUpResponse(record);
    }

    public SignInResponse handle(SignInCommand cmd) {
        SessionRecord record = userCommandService.processCommand(cmd);

//        if (cmd.username().equals("test_exception"))
//            throw new TestException();

        return CommandMapper.mapToSignInResponse(record);
    }
}
