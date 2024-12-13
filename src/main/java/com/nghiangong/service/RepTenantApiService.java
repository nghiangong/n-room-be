package com.nghiangong.service;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.response.tenant.TenantRes;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.TenantMapper;
import com.nghiangong.model.PasswordEncoderC;
import com.nghiangong.repository.RoomRepository;
import com.nghiangong.repository.TenantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('REP_TENANT')")
public class RepTenantApiService {
    TenantRepository tenantRepository;
    RoomRepository roomRepository;

    TenantMapper tenantMapper;

    @Transactional
    public void createMember(Tenant tenant) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer tenantId = Integer.valueOf(authentication.getName());

        Room room = roomRepository.findRentingByTenantId(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_RENTING_NO_ROOM));
        var currentContract = room.getCurrentContract();
        var memberCount = tenantRepository.countMembersByRepTenantId(tenantId);
        if (currentContract.getNumberOfPeople() <= memberCount)
            throw new AppException(ErrorCode.ROOM_ENOUGH_MEMBER);

        tenant.setRole(Role.TENANT);
        tenant.setRepTenant(tenantRepository.findById(tenantId).orElseThrow());

        try {
            tenant = tenantRepository.save(tenant);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }
}
