package com.nghiangong.service;

import com.nghiangong.dto.response.tenant.TenantDetailRes;
import com.nghiangong.dto.response.tenant.TenantRes;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.PasswordEncoderC;
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
    TenantRepository tenantRepository;
    TenantMapper tenantMapper;

    public void encode(Tenant tenant) {
        tenant.setPassword(PasswordEncoderC.encode(tenant.getPersonalIdNumber()));
    }

    //    @PreAuthorize("hasRole('PRE_TENANT')")
    public TenantRes createMember(Tenant tenant) {
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

        return tenantMapper.toTenantRes(tenant);
    }

    @PreAuthorize("hasRole('MANAGER')")
    public List<TenantDetailRes> getTenantsByManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());

        var objects = tenantRepository.findTenantsByManagerId(managerId);

        return objects.stream().map(obj -> {
            Tenant tenant = (Tenant) obj[0];
            Contract contract = (Contract) obj[1];
            Room room = (Room) obj[2];
            House house = (House) obj[3];
            return tenantMapper.toTenantDetailRes(tenant, contract, room, house);
        }).toList();
    }
}
