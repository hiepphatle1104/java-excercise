package com.swappie.accountservice.dto.event;

import com.swappie.accountservice.domain.AccountEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AccountCreatedEvent extends AccountEvent {
    private String message;
}
