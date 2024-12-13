package com.nghiangong.constant;

import lombok.*;
import lombok.experimental.FieldDefaults;

public enum ElecChargeCalc {
    EC1(
            "Chi phí điện theo số người trong 1 phòng",
            "Tiền điện = (Tiền điện theo số người) x (Số ngày ở) : (Số ngày trong tháng)"),
    EC2("Giá mỗi số điện cố định", ""),
    EC3("Giá mỗi số điện dựa theo bảng giá điện bán lẻ", ""),
    ;

    private final String description;
    private final String formula;

    ElecChargeCalc(String description, String formula) {
        this.description = description;
        this.formula = formula;
    }

    public String getDescription() {
        return description;
    }

    public String getFormula() {
        return formula;
    }

    public String getName() {
        return this.name();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Json {
        private String name;
        private String description;
        private String formula;
    }
}
