package com.nghiangong.entity.user;

import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Invoice;
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

    @Transient
    List<Tenant> members;

    @Transient
    Contract contract;

    @Transient
    Room rentingRoom;
}
