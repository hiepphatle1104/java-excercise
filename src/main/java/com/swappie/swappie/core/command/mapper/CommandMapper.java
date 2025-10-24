package com.swappie.swappie.core.command.mapper;

import com.swappie.swappie.common.dto.SignInResponse;
import com.swappie.swappie.common.dto.SignUpResponse;
import com.swappie.swappie.core.command.models.SignUpCommand;
import com.swappie.swappie.domain.AccountRecord;
import com.swappie.swappie.domain.SessionRecord;
import com.swappie.swappie.domain.UserRecord;

public class CommandMapper {
    public static UserRecord mapUserRecord(SignUpCommand command) {
        return UserRecord.builder()
                .username(command.username())
                .passwordHash(command.password())
                .build();
    }

    public static AccountRecord mapAccountRecord(UserRecord record) {
        return AccountRecord.builder().user(record).build();
    }

    public static SessionRecord mapSessionRecord(UserRecord record) {
        return SessionRecord.builder().user(record).build();
    }

    public static SignUpResponse mapToSignUpResponse(UserRecord userRecord) {
        return new SignUpResponse(userRecord.getId());
    }

    public static SignInResponse mapToSignInResponse(SessionRecord sessionRecord) {
        return new SignInResponse(sessionRecord.getAccessToken(), "Bearer", sessionRecord.getExpiresIn());
    }

}
