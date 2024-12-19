package com.nghiangong.controller;

import com.nghiangong.dto.response.ApiResponse;
import com.nghiangong.dto.response.elecwater.ElecwaterRecordRes;
import com.nghiangong.service.ElecWaterRecordService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ElecWaterRecordController {
    ElecWaterRecordService elecWaterRecordService;

    @GetMapping("/records")
    ApiResponse<List<ElecwaterRecordRes>> getRecord(@RequestParam int houseId, @RequestParam LocalDate date) {
        return ApiResponse.<List<ElecwaterRecordRes>>builder()
                .result(elecWaterRecordService.get(houseId, date))
                .build();
    }

    @PutMapping("/elecRecords/{id}")
    ApiResponse editElecRecord(@PathVariable int id, @RequestParam int value) {
        elecWaterRecordService.editElecRecord(id, value);
        return ApiResponse.builder().build();
    }

    @PutMapping("/waterRecords/{id}")
    ApiResponse editWaterRecord(@PathVariable int id, @RequestParam int value) {
        elecWaterRecordService.editWaterRecord(id, value);
        return ApiResponse.builder().build();
    }
}
