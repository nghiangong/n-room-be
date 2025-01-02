package com.nghiangong.address.repository;

import com.nghiangong.address.entity.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, String> {
    @Query("SELECT p.fullName FROM Province p")
    List<String> findAllProvinceNames();
}
