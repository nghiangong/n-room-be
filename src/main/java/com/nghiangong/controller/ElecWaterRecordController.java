package com.nghiangong.controller;

import com.nghiangong.dto.request.room.RecordReq;
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

    @PutMapping("/elecRecords/{roomId}")
    ApiResponse addElecRecord(@PathVariable int roomId, @RequestBody RecordReq request) {
        elecWaterRecordService.addElecRecord(roomId, request);
        return ApiResponse.builder().build();
    }

    @PutMapping("/waterRecords/{roomId}")
    ApiResponse addWaterRecord(@PathVariable int roomId, @RequestBody RecordReq request) {
        elecWaterRecordService.addWaterRecord(roomId, request);
        return ApiResponse.builder().build();
    }
}
