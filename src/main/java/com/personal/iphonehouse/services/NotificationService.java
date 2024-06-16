package com.personal.iphonehouse.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendStockUpdateNotification(Integer productId, int newStock, int newRegisterStock, int newCounterStock) {
        logger.info("Sending message to product: " + productId + " with new stock quantity of: " + newStock);
        messagingTemplate.convertAndSend("/topic/stock-updates", new StockUpdateMessage(productId, newStock, newRegisterStock, newCounterStock));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockUpdateMessage {
        private Integer productId;
        private int newStock;
        private int newRegisterStock;
        private int newCounterStock;
    }
}
