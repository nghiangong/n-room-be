package com.nghiangong.service;

import com.nghiangong.repository.InvalidatedTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvalidatedTokenService {
    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Tự động chạy mỗi ngày lúc 00:00
    public void deleteExpiredTokensDaily() {
        invalidatedTokenRepository.deleteExpiredTokens();
    }

    @EventListener(ApplicationReadyEvent.class) // Chạy khi ứng dụng bắt đầu
    public void deleteExpiredTokensOnStartup() {
        invalidatedTokenRepository.deleteExpiredTokens();
    }
}

