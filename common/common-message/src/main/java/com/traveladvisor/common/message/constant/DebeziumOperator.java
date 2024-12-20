package com.traveladvisor.common.message.constant;

public enum DebeziumOperator {

    CREATE("c"),
    UPDATE("u"),
    DELETE("d"),
    ;

    private String value;

    DebeziumOperator(String val) {
        this.value = val;
    }

    public String getValue() {
        return value;
    }

}
