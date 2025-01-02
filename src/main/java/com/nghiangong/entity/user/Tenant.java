package com.nghiangong.entity.user;

import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Room;
import com.nghiangong.listener.TenantListener;
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
@EntityListeners(TenantListener.class)
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
}
