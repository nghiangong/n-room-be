package com.nghiangong.entity.user;

import com.nghiangong.constant.Role;
import com.nghiangong.model.PasswordEncoderC;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

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

    @OneToMany(mappedBy = "repTenant", cascade = CascadeType.ALL)
    List<Tenant> members;

    @PrePersist
    public void prePersist() {
        this.setPassword(PasswordEncoderC.encode("1"));
        if (this.getRole() == Role.REP_TENANT) this.setRepTenant(this);
    }

    @PostPersist
    public void afterInsert() {
        this.setUsername("tenant_" + this.getId());
        if (this.getRole() == Role.REP_TENANT) this.setRepTenant(this);
    }
}
