package com.nghiangong.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.OtherFee;

@Repository
public interface OtherFeeRepository extends JpaRepository<OtherFee, Integer> {
    List<OtherFee> findByHouseId(int houseId);
}
