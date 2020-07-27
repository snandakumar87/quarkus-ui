/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.facade;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.debezium.examples.outbox.trade.messagelog.MessageLog;
import io.debezium.examples.outbox.trade.service.TradeOrderService;
 
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@ApplicationScoped
public class OrderEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventHandler.class);

    @Inject
    MessageLog log;

    @Inject
    TradeOrderService shipmentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void onOrderEvent(String payload) {
        LOGGER.info("Starting onOrderEvent");

        JsonNode eventJson = deserialize(payload).get("after");
        LOGGER.info("#1 " + eventJson.asText());

        UUID eventId = UUID.fromString(eventJson.get("ID").textValue());
        LOGGER.info("#2 " + eventId.toString());

        String eventType = eventJson.get("TYPE").textValue();
        LOGGER.info("#3 " + eventType);


        if (log.alreadyProcessed(eventId)) {
            LOGGER.info("Event with UUID {} was already retrieved, ignoring it", eventId);
            return;
        }

        LOGGER.info("Continuing onOrderEvent");

        JsonNode eventPayload = eventJson.get("PAYLOAD");

        LOGGER.info("Received 'Order' event -- event id: '{}', event type: '{}'", eventId, eventType);

        if (eventType.equals("OrderCreated")) {
            shipmentService.orderCreated(eventPayload);
        }
        else {
            LOGGER.warn("Unkown event type");
        }

        log.processed(eventId);
    }

    private JsonNode deserialize(String event) {
        JsonNode eventPayload;

        try {
            String unescaped = objectMapper.readValue(event, String.class);
            eventPayload = objectMapper.readTree(unescaped);
        }
        catch (IOException e) {
            throw new RuntimeException("Couldn't deserialize event", e);
        }

        return eventPayload.has("schema") ? eventPayload.get("payload").get("after") : eventPayload;
    }
}
