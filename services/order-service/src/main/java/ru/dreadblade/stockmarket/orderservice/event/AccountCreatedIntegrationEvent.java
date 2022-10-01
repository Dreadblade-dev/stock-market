package ru.dreadblade.stockmarket.orderservice.event;

import lombok.Getter;
import lombok.Setter;
import ru.dreadblade.stockmarket.shared.event.IntegrationEvent;

@Getter
@Setter
public class AccountCreatedIntegrationEvent extends IntegrationEvent {
    private Long accountId;
    private String ownerId;
}
