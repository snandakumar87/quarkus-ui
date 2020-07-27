/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.examples.outbox.trade.model.TradeOrder;

import com.fasterxml.jackson.databind.JsonNode;

@ApplicationScoped
public class TradeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeOrderService.class);

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value=TxType.MANDATORY)
    public void orderCreated(JsonNode event) {
        LOGGER.info("Processing 'OrderCreated' event: {}", event.asText());

        LOGGER.info("ID: " + event.get("id").toString() + " " + event.get("id").asText() + " " +  event.get("id").asLong());

        final long id = Long.valueOf(event.get("id").asText());
        final String orderType = event.get("orderType").asText();
        final Date openDate = new Date(event.get("openDate").asLong());
        final String symbol = event.get("symbol").asText();
        final int quantity = Integer.valueOf(event.get("quantity").asText());
        final BigDecimal price = new BigDecimal(event.get("price").asText());
        final BigDecimal orderFee= new BigDecimal(event.get("orderFee").asText());
        final int accountId = Integer.valueOf(event.get("accountId").asText());

        LOGGER.info("Going to persist 'TradeOrder'");

        TradeOrder tradeOrder = new TradeOrder(id, orderType, openDate, symbol, quantity, price, orderFee, accountId);

        LOGGER.info("Persisting 'TradeOrder': {}", tradeOrder);

        entityManager.persist(tradeOrder);
    }

    @Transactional(value=TxType.MANDATORY)
    public void orderLineUpdated(JsonNode event) {
        LOGGER.info("Processing 'OrderLineUpdated' event: {}", event);
    }
}
