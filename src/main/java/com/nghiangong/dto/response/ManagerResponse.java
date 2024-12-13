package com.nghiangong.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerResponse {
    int id;
    String username;
    String fullName;
    String phoneNumber;
    String role;
}
