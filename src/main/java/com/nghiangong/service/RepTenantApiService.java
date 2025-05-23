package com.nghiangong.service;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.tenant.TenantReq;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.TenantMapper;
import com.nghiangong.model.DateUtils;
import com.nghiangong.repository.ContractRepository;
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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@PreAuthorize("hasRole('REP_TENANT')")
public class RepTenantApiService {
    private final ContractRepository contractRepository;
    TenantRepository tenantRepository;
    RoomRepository roomRepository;

    TenantMapper tenantMapper;

    @Transactional
    public void createMember(TenantReq request) {
        var repTenant = getTenant();

        var memberCount = findMembers(repTenant).size();
        var contract = repTenant.getContract();
        if (contract.getNumberOfPeople() <= memberCount)
            throw new AppException(ErrorCode.ROOM_ENOUGH_MEMBER);

        Tenant newTenant = tenantMapper.toTenant(request);
        newTenant.setRole(Role.TENANT);
        newTenant.setRepTenant(contract.getRepTenant());
        try {
            tenantRepository.save(newTenant);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }

    @Transactional
    public void deleteMember(int tenantId) {
        var repTenantId = getTenant().getId();

        var tenant = tenantRepository.findById(tenantId).orElseThrow();
        if (tenant.getRepTenant().getId() == repTenantId)
            tenantRepository.delete(tenant);
        else throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    @Transactional
    public void updateMember(Integer tenantId, TenantReq request) {
        var repTenantId = getTenant().getId();

        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (tenant.getRepTenant().getId() == repTenantId)
            tenantMapper.updateTenant(tenant, request);
        else throw new AppException(ErrorCode.UNAUTHORIZED);
    }

    Tenant getTenant() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int tenantId = Integer.parseInt(authentication.getName());
        var tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_NOT_EXIST));
        tenant.setContract(findContract(tenant));
        return tenant;
    }

    List<Tenant> findMembers(Tenant tenant) {
        var members = tenantRepository.findByRepTenantId(tenant.getRepTenant().getId());
        return members;
    }

    Contract findContract(Tenant tenant) {
        Contract contract = contractRepository.findByRepTenantId(tenant.getRepTenant().getId());
        return contractRepository.findByRepTenantId(tenant.getRepTenant().getId());
    }
}
