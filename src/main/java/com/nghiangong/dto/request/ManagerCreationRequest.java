package com.nghiangong.dto.request;

import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerCreationRequest {
    @Size(min = 6, message = "INVALID_USERNAME")
    String username;

    @Size(min = 6, message = "INVALID_PASSWORD")
    String password;

    @Pattern(regexp = "^\\d{12}$", message = "INVALID_PERSONAL_ID_NUMBER")
    String personalIdNumber;

    @NotNull(message = "FULL_NAME_REQUIRED")
    String fullName;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "INVALID_PHONE_FORMAT")
    String phoneNumber;
}
