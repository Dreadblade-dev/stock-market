package ru.dreadblade.stockmarket.stockservice.event.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.dreadblade.stockmarket.shared.event.handler.IntegrationEventHandler;
import ru.dreadblade.stockmarket.stockservice.domain.Stock;
import ru.dreadblade.stockmarket.stockservice.event.StockPriceChangeIntegrationEvent;
import ru.dreadblade.stockmarket.stockservice.repository.StockRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockPriceChangeIntegrationEventHandler implements IntegrationEventHandler<StockPriceChangeIntegrationEvent> {
    private final SimpMessagingTemplate messagingTemplate;
    private final StockRepository stockRepository;

    @KafkaListener(groupId = "${app.kafka.consumer.group}", topics = "${app.kafka.topic.stock-price-change}")
    @Override
    public void handleIntegrationEvent(StockPriceChangeIntegrationEvent integrationEvent) {
        log.trace("Handling integration event: {} ({}): {}", integrationEvent.getId().toString(),
                integrationEvent.getClass().getSimpleName(), integrationEvent.getCreatedAt().toString());

        Stock stock = stockRepository.findById(integrationEvent.getStockId()).orElseThrow();

        stock.setPrice(integrationEvent.getNewPrice());
        stock.setUpdatedAt(integrationEvent.getChangedAt());

        stockRepository.save(stock);

        messagingTemplate.convertAndSend("/topic/stock-price-change", integrationEvent);
    }
}
