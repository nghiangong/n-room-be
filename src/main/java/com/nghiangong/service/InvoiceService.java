package com.nghiangong.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.nghiangong.dto.request.invoice.CreateCheckoutInvoiceReq;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.model.*;
import com.nghiangong.repository.ContractRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.constant.InvoiceStatus;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.InvoiceMapper;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.InvoiceRepository;
import com.nghiangong.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class InvoiceService {
    private final ContractRepository contractRepository;
    HouseRepository houseRepository;
    InvoiceMapper invoiceMapper;
    InvoiceRepository invoiceRepository;
    RoomRepository roomRepository;
    ElecWaterRecordService elecWaterRecordService;

    @Transactional
    public void createMonthlyInvoices(int houseId, LocalDate date) {
        LocalDate month = DateUtils.endOfMonth(date);
        List<Contract> contracts = contractRepository.findByHouseIdWithoutInvoiceOfMonth(houseId, month);

        if (contracts.size() == 0) throw new AppException(ErrorCode.INVOICE_OF_MONTH_EXISTED);

        for (var contract : contracts) {
            Invoice newInvoice = new Invoice();
            newInvoice.setCreateDate(LocalDate.now());
            newInvoice.setName("Hóa đơn tháng " + date.format(DateTimeFormatter.ofPattern("MM/YYYY")));
            newInvoice.setStartDate(DateUtils.latestDate(date.withDayOfMonth(1), contract.getStartDate()));
            newInvoice.setEndDate(date);
            newInvoice.setContract(contract);
            calculateExpense(newInvoice);
            invoiceRepository.save(newInvoice);
        }
    }



    @Transactional
    public void createCheckoutInvoice(int contractId, CreateCheckoutInvoiceReq request) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
        switch (contract.getStatus()) {
            case PENDING_CHECKOUT: {
                break;
            }
            case PENDING_PAYMENT:
                throw new AppException(ErrorCode.INVOICE_EXISTED);
            case ACTIVE:
            case SOON_INACTIVE:
                throw new AppException(ErrorCode.CONTRACT_NOT_EXPIRED);
            case INACTIVE:
                return;
            default: {
                throw new AppException(ErrorCode.CONTRACT_NO_STATUS);
            }
        }

        House house = contract.getRoom().getHouse();
        if (house.isHavingElecIndex()) contract.setEndElecNumber(request.getEndElecNumber());
        if (house.isHavingWaterIndex()) contract.setEndWaterNumber(request.getEndWaterNumber());

        Invoice newInvoice = new Invoice();
        newInvoice.setCheckout(true);
        newInvoice.setCreateDate(LocalDate.now());
        newInvoice.setName("Hóa đơn trả phòng ");
        newInvoice.setStartDate(contract.getEndDate().withDayOfMonth(1));
        newInvoice.setEndDate(contract.getEndDate());
        newInvoice.setContract(contract);

        calculateExpense(newInvoice);
        invoiceRepository.save(newInvoice);
    }

    void calculateExpense(Invoice invoice) {
        BaseRentCalculator.calculate(invoice);
        OtherFeeCalculator.calculate(invoice);
        ElecCalculator.calculate(elecWaterRecordService, invoice);
        WaterCalculator.calculate(elecWaterRecordService, invoice);
    }

    public List<InvoiceRes> getList(int houseId) {
        return invoiceRepository.findAllByHouseId(houseId).stream()
                .map(invoiceMapper::toInvoiceRes)
                .toList();
    }

    public List<InvoiceDetailRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return invoiceRepository.findByContractRoomHouseManagerId(id).stream().map(
                invoiceMapper::toInvoiceDetailRes).toList();
    }

    @Transactional
    public void setPaid(Integer invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        invoice.setStatus(InvoiceStatus.PAID);

    }

    @Transactional
    public void delete(int id) {

        invoiceRepository.deleteById(id);
    }
}
