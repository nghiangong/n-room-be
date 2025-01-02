package com.nghiangong.address.repository;

import com.nghiangong.address.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictId(String districtId);

    @Query("SELECT w.fullName FROM Ward w WHERE w.district.fullName = :districtFullName")
    List<String> findWardNamesByDistrictFullName(@Param("districtFullName") String districtFullName);
}
