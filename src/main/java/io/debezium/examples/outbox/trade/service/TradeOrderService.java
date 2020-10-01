/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.service;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import io.debezium.examples.outbox.trade.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.core.eventbus.EventBus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class TradeOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeOrderService.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    EventBus eventBus;

    @PersistenceContext
    EntityManager entityManager;

    @Transactional(value = TxType.MANDATORY)
    public void orderCreated(JsonNode event) throws JsonMappingException, JsonProcessingException {
        LOGGER.info("Processing 'OrderCreated' event: {}", event.asText());

        event = objectMapper.readTree(event.asText());

        LOGGER.info("type: " + event.get("type") + " " + event.get("type").asText());

        final String id =event.get("id").asText();
        final String amount = event.get("amount").asText();
        final String county = event.get("country").asText();
        final String merchantId = event.get("merchantId").asText();

        Transaction transaction = new Transaction(id, county,amount,merchantId);

        LOGGER.info("Persisting 'TradeOrder': {}", transaction);

        entityManager.persist(transaction);

        final JsonObject jsonObject = JsonObject.mapFrom(transaction);
        eventBus.publish("order_stream", jsonObject);
    }

    @Transactional(value=TxType.MANDATORY)
    public void orderLineUpdated(JsonNode event) {
        LOGGER.info("Processing 'OrderLineUpdated' event: {}", event);
    }
}
