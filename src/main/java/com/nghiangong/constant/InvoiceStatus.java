package com.nghiangong.constant;

public enum InvoiceStatus {
    PROCESSING("Đang xử lý thanh toán"),
    PAID("Hóa đơn đã thanh toán"),
    UNPAID("Hóa đơn chưa thanh toán"),
    CANCELLED("Hóa đơn đã hủy");

    private final String description;

    InvoiceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
