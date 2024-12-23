package com.nghiangong.service;

import com.nghiangong.dto.response.user.TenantDetailRes;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.PasswordEncoderC;
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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TenantService {
    private final ManagerRepository managerRepository;
    private final HouseRepository houseRepository;
    TenantRepository tenantRepository;
    TenantMapper tenantMapper;

    //    @PreAuthorize("hasRole('PRE_TENANT')")
    public UserRes createMember(Tenant tenant) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        tenant.setRole(Role.TENANT);
        tenant.setPassword(PasswordEncoderC.encode("1"));
        tenant.setRepTenant(tenantRepository.findById(tenantId).orElseThrow());

        try {
            tenant = tenantRepository.save(tenant);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return tenantMapper.toUserRes(tenant);
    }

    @PreAuthorize("hasRole('MANAGER')")
    public List<TenantDetailRes> getTenantsByManager() {
        var manager = getManager();

        return manager.getTenants().stream().map(tenant -> tenantMapper.toTenantDetailRes(tenant)).toList();
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }
}
