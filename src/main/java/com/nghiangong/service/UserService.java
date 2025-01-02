package com.nghiangong.service;

import java.util.List;

import com.nghiangong.dto.request.user.ChangePasswordReq;
import com.nghiangong.dto.response.user.UserRes;
import com.nghiangong.model.PasswordEncoderC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.constant.Role;
import com.nghiangong.dto.request.ManagerCreationRequest;
import com.nghiangong.dto.request.ManagerUpdateRequest;
import com.nghiangong.entity.user.User;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.UserMapper;
import com.nghiangong.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    @Transactional(readOnly = true)
    public UserRes getMyInfo() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserRes(user);
    }

    @Transactional
    public UserRes updateInfo(ManagerUpdateRequest request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        userMapper.updateUser(user, request);
        return userMapper.toUserRes(userRepository.save(user));
    }

    @Transactional
    public void changePassword(ChangePasswordReq request) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = PasswordEncoderC.matches(request.getOldPassword(), user.getPassword());
        if (!authenticated) throw new AppException(ErrorCode.WRONG_PASSWORD);
        user.setPassword(PasswordEncoderC.encode(request.getNewPassword()));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
