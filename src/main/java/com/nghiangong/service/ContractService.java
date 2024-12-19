package com.nghiangong.service;

import java.util.List;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nghiangong.constant.ContractStatus;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.ContractMapper;
import com.nghiangong.repository.ContractRepository;
import com.nghiangong.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ContractService {
    ContractRepository contractRepository;
    ContractMapper contractMapper;
    RoomRepository roomRepository;

    @Transactional
    public void createContract(ContractReq request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        Contract newContract = contractMapper.toContract(request);
        newContract.setStatus(ContractStatus.ACTIVE);
        newContract.setRoom(room);

        Tenant newTenant = newContract.getRepTenant();
        newTenant.setRole(Role.REP_TENANT);
    }

    @Transactional
    public void updateContract(int id, ContractReq request) {
        Contract contract = contractRepository.findById(id).orElseThrow();
        contractMapper.update(contract, request);
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        contract.setRoom(room);
    }

    public ContractRes getContract(int contractId) {
        Contract contract = contractRepository
                .findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        return contractMapper.toContractRes(contract);
    }

    public List<ContractRes> getListByRoomId(int roomId) {
        return contractRepository.findByRoomId(roomId).stream()
                .map(contractMapper::toContractRes)
                .toList();
    }


    public List<ContractDetailRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return contractRepository.findByRoomHouseManagerId(id).stream().map(
                contractMapper::toContractDetailRes).toList();

    }

    @Transactional
    public void deleteContract(int id) {
        contractRepository.deleteById(id);
        System.out.println("delete contract " + id);
    }
}
