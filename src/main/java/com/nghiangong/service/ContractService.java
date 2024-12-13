package com.nghiangong.service;

import java.util.List;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.model.PasswordEncoderC;
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
        Room room = roomRepository
                .findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
        Contract newContract = contractMapper.toContract(request);
        newContract.setStatus(ContractStatus.ACTIVE);
        Tenant newTenant = newContract.getRepTenant();
        newTenant.setRole(Role.REP_TENANT);
        newTenant.setPassword(PasswordEncoderC.encode("1"));
        newContract.setRoom(room);
        room.setCurrentContract(newContract);
    }

    @Transactional
    public void updateContract(int id, ContractReq request) {
        Contract contract = contractRepository.findById(id).orElseThrow();
        contractMapper.update(contract, request);
        if (contract.getRoom().getId() != request.getRoomId()) {
            Room room = roomRepository
                    .findById(request.getRoomId())
                    .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));
            contract.getRoom().setCurrentContract(null);
            room.setCurrentContract(contract);
        }
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

    public void deleteContract(int contractId) {
        contractRepository.deleteById(contractId);
    }

    public List<ContractDetailRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return contractRepository.findByRoomHouseManagerId(id).stream().map(contractMapper::toContractDetailRes).toList();

    }
}
