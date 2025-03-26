package com.example.spm.enums;

public enum OrderStatus {
    PENDING(0),
    PAID(1),
    SHIPPED(2),
    COMPLETED(3),
    CANCELLED(4);

    private final Integer code;

    OrderStatus(Integer code) {
        this.code = code;
    }

    public static OrderStatus fromCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus status : OrderStatus.values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null; // 或抛出自定义异常
    }

    public Integer getCode() {
        return code;
    }
}