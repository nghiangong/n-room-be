package com.nghiangong.address.repository;

import com.nghiangong.address.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {
    List<District> findByProvinceId(String provinceId);

    @Query("SELECT d.fullName FROM District d WHERE d.province.fullName = :provinceFullName")
    List<String> findDistrictNamesByProvinceFullName(@Param("provinceFullName") String provinceFullName);
}
