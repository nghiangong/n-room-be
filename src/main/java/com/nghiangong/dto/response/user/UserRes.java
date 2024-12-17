package com.nghiangong.dto.response.user;

import com.nghiangong.constant.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRes {
    int id;
    String personalIdNumber;
    String fullName;
    String phoneNumber;
    String email;
    Role role;
}
