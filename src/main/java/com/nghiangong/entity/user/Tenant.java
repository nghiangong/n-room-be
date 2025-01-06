package com.nghiangong.entity.user;

import com.nghiangong.configuration.SpringContextHolder;
import com.nghiangong.constant.Role;
import com.nghiangong.email.BrevoEmailService;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.model.PasswordEncoderC;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Tenant extends User {
    @ManyToOne
    @JoinColumn(name = "rep_tenant_id")
    Tenant repTenant;

    @Override
    public void setEmail(String email) {
        if (!this.getEmail().equals(email)) {
            changedEmail = true;
            super.setEmail(email);
        }
    }

    @Transient
    private Contract contract;

    @Transient
    private boolean changedEmail = false;

    @PrePersist
    public void prePersist() {
        this.setPassword(PasswordEncoderC.encode("1"));
        if (this.getRole() == Role.REP_TENANT) this.setRepTenant(this);
    }

    @PostPersist
    public void afterInsert() {
        this.setUsername("tenant_" + this.getId());
        if (this.getRole() == Role.REP_TENANT) this.setRepTenant(this);

        BrevoEmailService brevoEmailService = SpringContextHolder.getBean(BrevoEmailService.class);
        brevoEmailService.sendAccount(this);
    }

    @PreUpdate
    public void preUpdate() {
        if (this.isChangedEmail() == true)
            this.setPassword(PasswordEncoderC.encode("1"));
    }

    @PostUpdate
    public void afterUpdate() {
        if (this.isChangedEmail() == true) {
            BrevoEmailService brevoEmailService = SpringContextHolder.getBean(BrevoEmailService.class);
            brevoEmailService.sendAccount(this);
        }
    }
}
