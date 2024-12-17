package com.nghiangong.service;

import com.nghiangong.dto.response.contract.ContractDetailRes;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.elecwater.RecordRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.room.Room;
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
    WaterRecordOfRoomRepository waterRecordOfRoomRepository;
    ElecRecordOfRoomRepository elecRecordOfRoomRepository;

    RoomMapper roomMapper;
    ContractMapper contractMapper;
    InvoiceMapper invoiceMapper;
    TenantMapper tenantMapper;
    RecordMapper recordMapper;

    public RoomDetailRes2 getRoom() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int tenantId = Integer.parseInt(authentication.getName());

        Room room = roomRepository.findRentingByTenantId(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_RENTING_NO_ROOM));

        Pageable pageable = PageRequest.of(0, 12);
        var response = roomMapper.toRoomDetailDetailRes2(room);
        var elecRecords = elecRecordOfRoomRepository.findByTenantId(tenantId, pageable);
        var waterRecords = waterRecordOfRoomRepository.findByTenantId(tenantId, pageable);
        response.setElecRecords(elecRecords.stream().map(recordMapper::toRecordRes).toList());
        response.setWaterRecords(waterRecords.stream().map(recordMapper::toRecordRes).toList());
        return response;
    }

    public List<ContractRes> getContracts() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = authentication.getName();
        return contractRepository.findByTenantId(Integer.parseInt(tenantId)).stream()
                .map(contractMapper::toContractRes)
                .toList();
    }

    public ContractDetailRes getContractDetailById(int contractId) {
        var contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
        return contractMapper.toContractDetailRes(contract);
    }

    public List<InvoiceRes> getInvoices() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = authentication.getName();
        return invoiceRepository.findByTenantId(Integer.parseInt(tenantId)).stream()
                .map(invoiceMapper::toInvoiceRes)
                .toList();
    }

    public InvoiceDetailRes getInvoiceDetailById(int invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        return invoiceMapper.toInvoiceDetailRes(invoice);
    }

    public List<UserRes> getMembers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        var tenants = tenantRepository.findMembersByTenantId(tenantId);
        return tenants.stream().map(tenantMapper::toUserRes).toList();
    }

    public List<RecordRes> getElecRecords() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        Pageable pageable = PageRequest.of(0, 12);
        var records = elecRecordOfRoomRepository.findByTenantId(tenantId, pageable);
        return records.stream().map(recordMapper::toRecordRes).toList();
    }

    public List<RecordRes> getWaterRecords() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        Pageable pageable = PageRequest.of(0, 12);
        var records = waterRecordOfRoomRepository.findByTenantId(tenantId, pageable);
        return records.stream().map(recordMapper::toRecordRes).toList();
    }

}
