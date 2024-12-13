package com.nghiangong.constant;

import lombok.*;

public enum WaterChargeCalc {
    WC1("Giá mỗi khối nước cố định"),
    WC2("Giá mỗi khối nước thay đổi theo tổng số nước của cả nhà"),
    WC3("Giá mỗi khối nước thay đổi theo số nước trung bình mỗi người sử dụng"),
    WC4("Chi phí nước theo trung bình chi phí nước chung"),
    WC5("Chi phí nước mặc định cho mỗi người");

    private final String description;

    WaterChargeCalc(String description) {
        this.description = description;
    }
}
