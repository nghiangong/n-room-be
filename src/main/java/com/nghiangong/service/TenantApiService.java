package com.nghiangong.service;

import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.elecwater.RecordRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.*;
import com.nghiangong.repository.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantApiService {
    RoomRepository roomRepository;
    ContractRepository contractRepository;
    InvoiceRepository invoiceRepository;
    TenantRepository tenantRepository;

    RoomMapper roomMapper;
    ContractMapper contractMapper;
    InvoiceMapper invoiceMapper;
    TenantMapper tenantMapper;
    RecordMapper recordMapper;

    @Transactional
    public RoomDetailRes2 getRoom() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int tenantId = Integer.parseInt(authentication.getName());
        Tenant tenant = tenantRepository.findById(tenantId).orElseThrow();

        Room rentingRoom = tenant.getRentingRoom();
        if (rentingRoom == null)
            throw new AppException(ErrorCode.TENANT_RENTING_NO_ROOM);

        var response = roomMapper.toRoomDetailDetailRes2(rentingRoom);
        response.setElecRecords(rentingRoom.getElecRecords().stream().map(recordMapper::toRecordRes).toList());
        response.setWaterRecords(rentingRoom.getWaterRecords().stream().map(recordMapper::toRecordRes).toList());
        return response;
    }

    @Transactional
    public List<ContractRes> getContracts() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = authentication.getName();
        return contractRepository.findByTenantId(Integer.parseInt(tenantId)).stream()
                .map(contractMapper::toContractRes)
                .toList();
    }

    @Transactional
    public ContractDetailRes getContractDetailById(int contractId) {
        var contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
        return contractMapper.toContractDetailRes(contract);
    }

    @Transactional
    public List<InvoiceRes> getInvoices() {
        var tenant = getTenant();
        var list = tenant.getContract().getInvoices().stream()
                .map(invoiceMapper::toInvoiceRes)
                .toList();
        return list;
    }

    @Transactional
    public InvoiceDetailRes getInvoiceDetailById(int invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return invoiceMapper.toInvoiceDetailRes(invoice);
    }

    @Transactional
    public List<UserRes> getMembers() {
        var tenant = getTenant();

        return tenant.getMembers().stream().map(tenantMapper::toUserRes).toList();
    }

    Tenant getTenant() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int tenantId = Integer.parseInt(authentication.getName());
        return tenantRepository.findById(tenantId).orElseThrow();
    }
}
