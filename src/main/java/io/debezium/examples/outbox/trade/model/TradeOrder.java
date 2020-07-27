/*
 * Copyright Debezium Authors.
 *
 * Licensed under the Apache Software License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.debezium.examples.outbox.trade.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TradeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    private String orderType;
    private Date openDate;
    private String symbol;
    private int quantity;
    private BigDecimal price;
    private BigDecimal orderFee;
    private int accountId;

    TradeOrder() {
    }

    public TradeOrder(long id, String orderType, Date openDate, String symbol, int quantity, BigDecimal price, BigDecimal orderFee, int accountId) {
        this.id = id;
        this.orderType = orderType;
        this.openDate = openDate;
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.orderFee = orderFee;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public String getOrderType() {
        return this.orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol){
        this.symbol = symbol;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public BigDecimal getOrderFee(){
        return this.orderFee;
    }

    public void setOrderFee(BigDecimal orderFee){
        this.orderFee = orderFee;
    }

    public int getAccountId(){
        return this.accountId;
    }

    public void setAccountId(int accountId){
        this.accountId = accountId;
    }
}