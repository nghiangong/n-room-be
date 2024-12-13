package com.nghiangong.dto.request.tenant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TenantReq {
    String username;
    String fullName;
    String personalIdNumber;
    String phoneNumber;
    String email;
}
