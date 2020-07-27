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
        LOGGER.info("Processing 'OrderCreated' event: {}", event);

        final long id = event.get("id").asLong();
        final String orderType = event.get("orderType").asText();
        final Date openDate = new Date(event.get("openDate").asLong());
        final String symbol = event.get("symbol").asText();
        final int quantity = event.get("quantity").asInt();
        final BigDecimal price = new BigDecimal(event.get("price").asText());
        final BigDecimal orderFee= new BigDecimal(event.get("orderFee").asText());
        final int accountId = event.get("accountId").asInt();

        entityManager.persist(new TradeOrder(id, orderType, openDate, symbol, quantity, price, orderFee, accountId));
    }

    @Transactional(value=TxType.MANDATORY)
    public void orderLineUpdated(JsonNode event) {
        LOGGER.info("Processing 'OrderLineUpdated' event: {}", event);
    }
}
