//package com.nghiangong.service;
//
//import java.util.List;
//
//import org.springframework.transaction.annotation.Transactional;
//
//import com.nghiangong.dto.request.OtherFeeRequest;
//import com.nghiangong.dto.response.OtherFeeResponse;
//import com.nghiangong.entity.House;
//import com.nghiangong.entity.OtherFee;
//import com.nghiangong.mapper.OtherFeeMapper;
//import com.nghiangong.repository.HouseRepository;
//import com.nghiangong.repository.OtherFeeRepository;
//
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//
//@org.springframework.stereotype.Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class OtherFeeService {
//    OtherFeeRepository otherFeeRepository;
//    OtherFeeMapper otherFeeMapper;
//    HouseRepository houseRepository;
//
//    @Transactional
//    public OtherFeeResponse createOtherFee(int houseId, OtherFeeRequest request) {
//        House house = houseRepository.findById(houseId).orElseThrow();
//        OtherFee otherFee = otherFeeMapper.toOtherFee(request);
//        otherFee.setHouse(house);
//        otherFee = otherFeeRepository.save(otherFee);
//        return otherFeeMapper.toOtherFeePriRes(otherFee);
//    }
//
//    @Transactional(readOnly = true)
//    public List<OtherFeeResponse> getList(int houseId) {
//        return otherFeeRepository.findByHouseId(houseId).stream()
//                .map(otherFeeMapper::toOtherFeePriRes)
//                .toList();
//    }
//
//    @Transactional
//    public OtherFeeResponse updateOtherFee(int id, OtherFeeRequest request) {
//        OtherFee otherFee = otherFeeRepository.findById(id).orElseThrow();
//        otherFeeMapper.updateOtherFee(otherFee, request);
//        otherFee = otherFeeRepository.save(otherFee);
//        return otherFeeMapper.toOtherFeePriRes(otherFee);
//    }
//
//    @Transactional
//    public void deleteOtherFee(int id) {
//        otherFeeRepository.deleteById(id);
//    }
//}
