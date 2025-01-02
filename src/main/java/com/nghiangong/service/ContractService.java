package com.nghiangong.service;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.contract.ContractReq;
import com.nghiangong.dto.request.contract.StopContractReq;
import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.repository.ManagerRepository;
import com.nghiangong.repository.TenantRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ManagerRepository managerRepository;
    ContractRepository contractRepository;
    ContractMapper contractMapper;
    RoomRepository roomRepository;

    @Transactional
    public List<ContractDetailRes> getList() {
        var manager = getManager();
        return manager.getContracts().stream()
                .map(contractMapper::toContractDetailRes).toList();
    }

    @Transactional
    public ContractRes getContract(int id) {
        var manager = getManager();
        var contract = manager.getContract(id);

        return contractMapper.toContractRes(contract);
    }

    @Transactional
    public void createContract(ContractReq request) {
        var manager = getManager();
        var room = manager.getRoom(request.getRoomId());

        Contract newContract = contractMapper.toContract(request);
        room.createContract(newContract);

        Tenant newTenant = newContract.getRepTenant();
        newTenant.setRole(Role.REP_TENANT);
    }

    @Transactional
    public void updateContract(int id, ContractReq request) {
        var manager = getManager();
        var room = manager.getRoom(request.getRoomId());
        var contract = manager.getContract(id);

        contractMapper.update(contract, request);
        room.updateContract(contract);
    }

    @Transactional
    public void stopContract(int id, StopContractReq request) {
        var manager = getManager();
        var contract = manager.getContract(id);

        contract.setEndDate(request.getEndDate());

        contract.getRoom().updateContract(contract);
    }

    @Transactional
    public void delete(int id) {
        var contract = contractRepository.findById(id).orElse(null);
        contractRepository.delete(contract);
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }

}
