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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class TradeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeOrderService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value = TxType.MANDATORY)
    public void orderCreated(JsonNode event) throws JsonMappingException, JsonProcessingException {
        LOGGER.info("Processing 'OrderCreated' event: {}", event.asText());

        event = objectMapper.readTree(event.asText());

        LOGGER.info("type: " + event.get("type") + " " + event.get("type").asText());

        final long id = Long.valueOf(event.get("id").asText());
        final String orderType = event.get("type").asText();
        final Date openDate = new Date(event.get("openDate").asLong());
        final String symbol = event.get("symbol").asText();
        final int quantity = event.get("quantity").asInt();
        final BigDecimal price = new BigDecimal(event.get("price").asText());
        final BigDecimal orderFee= new BigDecimal(event.get("orderFee").asText());
        final int accountId = event.get("accountId").asInt();

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
