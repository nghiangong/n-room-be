package com.nghiangong.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nghiangong.entity.user.Manager;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Integer> {}
