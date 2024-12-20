//package com.nghiangong.configuration;
//
//import com.nghiangong.constant.ContractStatus;
//import com.nghiangong.entity.room.Contract;
//import com.nghiangong.model.DateUtils;
//import com.nghiangong.repository.ContractRepository;
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DailyTask {
//
//    private final ContractRepository contractRepository;
//
//    // Chạy ngay khi ứng dụng khởi động
//    @PostConstruct
//    public void runOnStartup() {
//        System.out.println("Ứng dụng đã khởi động. Chạy ngay lập tức.");
//        // Thực hiện công việc cần thiết ngay khi khởi động ứng dụng
//        executeTask();
//    }
//
//    // Chạy vào lúc 00:00 mỗi ngày
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void runAtMidnight() {
//        System.out.println("Chạy vào đầu ngày mới: " + java.time.LocalDate.now());
//        // Thực hiện công việc định kỳ mỗi ngày
//        executeTask();
//    }
//
//    @Transactional
//    private void executeTask() {
//        System.out.println("Thực hiện công việc.");
//        List<Contract> contracts = contractRepository.findAll();
//        for (Contract contract : contracts) {
//            if (contract.getEndDate() != null)
//            switch (contract.getStatus()){
//                case ACTIVE -> {
//                    if (DateUtils.remainingDateLessAMonth(contract.getEndDate()))
//                        contract.setStatus(ContractStatus.SOON_EXPIRED);
//                    if (!contract.getEndDate().isAfter(LocalDate.now()))
//                        contract.setStatus(ContractStatus.PENDING_CHECKOUT);
//                }
//            }
//        }
//    }
//}
