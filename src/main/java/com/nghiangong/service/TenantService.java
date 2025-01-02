package com.nghiangong.service;

import com.nghiangong.dto.request.tenant.TenantReq;
import com.nghiangong.dto.response.user.TenantDetailRes;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.PasswordEncoderC;
import com.nghiangong.repository.ContractRepository;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.ManagerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.constant.Role;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.mapper.TenantMapper;
import com.nghiangong.repository.TenantRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@PreAuthorize("hasRole('MANAGER')")
@Slf4j
public class TenantService {
    private final ContractRepository contractRepository;
    private final ManagerRepository managerRepository;
    private final HouseRepository houseRepository;
    TenantRepository tenantRepository;
    TenantMapper tenantMapper;

//    public UserRes createMember(Tenant tenant) {
//        var authentication = SecurityContextHolder.getContext().getAuthentication();
//        Integer tenantId = Integer.valueOf(authentication.getName());
//
//        tenant.setRole(Role.TENANT);
//        tenant.setPassword(PasswordEncoderC.encode("1"));
//        tenant.setRepTenant(tenantRepository.findById(tenantId).orElseThrow());
//
//        try {
//            tenant = tenantRepository.save(tenant);
//        } catch (DataIntegrityViolationException exception) {
//            throw new AppException(ErrorCode.USER_EXISTED);
//        }
//
//        return tenantMapper.toUserRes(tenant);
//    }

    @Transactional
    public void updateTenant(Integer tenantId, TenantReq request) {
        var manager = getManager();
        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_NOT_EXIST));
        tenantMapper.updateTenant(tenant, request);
    }

    @Transactional
    public List<TenantDetailRes> getTenants() {
        var manager = getManager();
        var tenants = manager.getRooms().stream().map(room -> room.getCurrentContract())
                .filter(contract -> contract != null)
                .flatMap(contract -> tenantRepository.findByRepTenantId(contract.getRepTenant().getId()).stream())
                .toList();
        tenants.forEach(tenant -> findContract(tenant));

        return tenants.stream().map(tenant -> tenantMapper.toTenantDetailRes(tenant)).toList();
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }

    Contract findContract(Tenant tenant) {
        Contract contract = contractRepository.findByRepTenantId(tenant.getRepTenant().getId());
        tenant.setContract(contract);
        return contractRepository.findByRepTenantId(tenant.getRepTenant().getId());
    }
}
