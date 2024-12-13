package com.nghiangong.service;

import com.nghiangong.model.PasswordEncoderC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.response.ManagerResponse;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.ManagerMapper;
import com.nghiangong.repository.ManagerRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ManagerService {
    ManagerRepository managerRepository;
    ManagerMapper managerMapper;

    public ManagerResponse createManager(ManagerCreationRequest request) {
        Manager manager = managerMapper.toManager(request);
        manager.setRole(Role.MANAGER);
        manager.setPassword(PasswordEncoderC.encode(request.getPassword()));

        try {
            manager = managerRepository.save(manager);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        return managerMapper.toManagerResponse(manager);
    }
}
