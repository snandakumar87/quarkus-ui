package io.debezium.examples.outbox.trade.model;

public class Transaction {
    private String id;
    private String country;
    private String amount;
    private String merchantId;

    public Transaction(String id, String country, String amount, String merchantId) {
        this.id = id;
        this.country = country;
        this.amount = amount;
        this.merchantId = merchantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }
}
