package com.nghiangong.listener;

import com.nghiangong.configuration.SpringContextHolder;
import com.nghiangong.constant.Role;
import com.nghiangong.email.BrevoEmailService;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.entity.user.Tenant;
import com.nghiangong.model.PasswordEncoderC;
import com.nghiangong.repository.ContractRepository;
import com.nghiangong.repository.TenantRepository;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

public class TenantListener {
    TenantRepository tenantRepository;
    ContractRepository contractRepository;

    @PrePersist
    public void prePersist(Tenant tenant) {
        tenant.setPassword(PasswordEncoderC.encode("1"));
        if (tenant.getRole() == Role.REP_TENANT) tenant.setRepTenant(tenant);
    }

    @PostPersist
    public void afterInsert(Tenant tenant) {
        tenant.setUsername("tenant_" + tenant.getId());
        if (tenant.getRole() == Role.REP_TENANT) tenant.setRepTenant(tenant);

        BrevoEmailService brevoEmailService = SpringContextHolder.getBean(BrevoEmailService.class);
        brevoEmailService.sendAccount(tenant);
    }

    @PreUpdate
    public void preUpdate(Tenant tenant) {
        if (tenant.isChangedEmail() == true)
            tenant.setPassword(PasswordEncoderC.encode("1"));
    }

    @PostUpdate
    public void afterUpdate(Tenant tenant) {
        if (tenant.isChangedEmail() == true) {
            BrevoEmailService brevoEmailService = SpringContextHolder.getBean(BrevoEmailService.class);
            brevoEmailService.sendAccount(tenant);
        }
    }

    @PostLoad
    public void initializeTransients(Tenant tenant) {
        contractRepository = SpringContextHolder.getBean(ContractRepository.class);
        tenantRepository = SpringContextHolder.getBean(TenantRepository.class);

        tenant.setContract(findContract(tenant));
        tenant.setMembers(findMembers(tenant));
        Contract contract = tenant.getContract();
        Room room = contract.getRoom();
        if (room.getCurrentContract() == contract)
            tenant.setRentingRoom(room);
    }

    List<Tenant> findMembers(Tenant tenant) {
        return tenantRepository.findByRepTenantId(tenant.getRepTenant().getId());
    }

    Contract findContract(Tenant tenant) {
        return contractRepository.findByRepTenant_Id(tenant.getRepTenant().getId());
    }
}
