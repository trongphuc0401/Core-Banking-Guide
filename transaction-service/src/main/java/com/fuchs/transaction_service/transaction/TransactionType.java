package com.fuchs.transaction_service.transaction;

public enum TransactionType {
    CASH_IN("CASH-IN"),
    CASH_OUT("CASH-OUT"),
    DEBIT("DEBIT"),
    PAYMENT("PAYMENT"),
    TRANSFER("TRANSFER");

    private final String type;
    TransactionType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TransactionType{" +
                "type='" + type + '\'' +
                '}';
    }

    public static TransactionType fromString(String text) {
        for(TransactionType t: TransactionType.values()){
            if(t.type.equalsIgnoreCase(text)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown transaction type: " + text);
    }
}
