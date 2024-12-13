package com.nghiangong.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.House;

@Repository
public interface HouseRepository extends JpaRepository<House, Integer> {
    List<House> findByManagerId(int managerId);
}
