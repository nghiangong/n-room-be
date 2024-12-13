package com.nghiangong.dto.response.tenant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantRes {
    int id;
    String username;
    String personalIdNumber;
    String fullName;
    String phoneNumber;
    String email;
}
