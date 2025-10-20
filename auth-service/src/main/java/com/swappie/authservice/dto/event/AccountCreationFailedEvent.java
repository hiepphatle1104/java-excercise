package com.swappie.authservice.dto.event;

import com.swappie.authservice.domain.AccountEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountCreationFailedEvent extends AccountEvent {
    private String errorMessage;
}
