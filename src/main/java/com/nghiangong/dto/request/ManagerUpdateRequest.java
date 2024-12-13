package com.nghiangong.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerUpdateRequest {
    @NotNull(message = "FULL_NAME_REQUIRED")
    String fullName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "INVALID_PHONE_FORMAT")
    String phoneNumber;
}
