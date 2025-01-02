package com.nghiangong.address;

import com.nghiangong.dto.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@RequestMapping("/address")
public class AddressController {
    AddressService addressService;

    @GetMapping("/provinces")
    ApiResponse<List<String>> getProvinces() {
        return ApiResponse.<List<String>>builder()
                .result(addressService.getProvinceNames())
                .build();
    }

    @GetMapping("/districts")
    ApiResponse<List<String>> getDistricts(@RequestParam String province) {
        return ApiResponse.<List<String>>builder()
                .result(addressService.getDistrictNames(province))
                .build();
    }

    @GetMapping("/wards")
    ApiResponse<List<String>> getWards(@RequestParam String district) {
        return ApiResponse.<List<String>>builder()
                .result(addressService.getWardNames(district))
                .build();
    }

    @PostMapping
    ApiResponse createData(@RequestBody DataRequest data) {
        addressService.initData(data);
        return ApiResponse.builder().build();
    }
}
