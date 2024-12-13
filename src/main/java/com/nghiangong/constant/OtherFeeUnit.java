package com.nghiangong.constant;

public enum OtherFeeUnit {
    PER_ROOM("Chi phí trên 1 phòng"),
    PER_PERSON("Chi phí trên 1 người"),
    PER_BICYCLE("Chi phí trên 1 xe đạp"),
    PER_MOTORBIKE("Chi phí trên 1 xe máy");
    private final String description;

    OtherFeeUnit(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
