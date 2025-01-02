package com.nghiangong.repository;

import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.user.Tenant;

import java.util.List;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Integer> {


    List<Tenant> findByRepTenantId(int id);
}
