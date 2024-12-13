package com.nghiangong.constant;

public enum RoomStatus {
    AVAILABLE("Phòng trống và sẵn sàng cho thuê"),
    OCCUPIED("Phòng đang được thuê"),
    INACTIVE("Phòng ngưng sử dụng"),
    SOON_AVAILABLE("Phòng sắp trống");

    private final String description;

    RoomStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
