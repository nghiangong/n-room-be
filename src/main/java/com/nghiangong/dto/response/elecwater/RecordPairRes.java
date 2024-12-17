package com.nghiangong.dto.response.elecwater;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordPairRes {
    RecordRes prev;
    RecordRes cur;
}
