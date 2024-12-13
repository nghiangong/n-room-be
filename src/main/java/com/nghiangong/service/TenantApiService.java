package com.nghiangong.service;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.response.contract.ContractRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.dto.response.room.RoomDetailRes2;
import com.nghiangong.dto.response.tenant.TenantRes;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.ContractMapper;
import com.nghiangong.mapper.InvoiceMapper;
import com.nghiangong.mapper.RoomMapper;
import com.nghiangong.mapper.TenantMapper;
import com.nghiangong.repository.ContractRepository;
import com.nghiangong.repository.InvoiceRepository;
import com.nghiangong.repository.RoomRepository;
import com.nghiangong.repository.TenantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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

    RoomMapper roomMapper;
    ContractMapper contractMapper;
    InvoiceMapper invoiceMapper;
    TenantMapper tenantMapper;

    public RoomDetailRes2 getRoomByTenant() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int tenantId = Integer.parseInt(authentication.getName());

        Room room = roomRepository.findRentingByTenantId(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_RENTING_NO_ROOM));
        return roomMapper.toRoomDetailDetailRes2(room);
    }

    public List<ContractRes> getContracts() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String tenantId = authentication.getName();
        return contractRepository.findByTenantId(Integer.parseInt(tenantId)).stream()
                .map(contractMapper::toContractRes)
                .toList();
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

    public List<TenantRes> getMembers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        var tenants = tenantRepository.findMembersByTenantId(tenantId);
        return tenants.stream().map(tenantMapper::toTenantRes).toList();
    }


}
