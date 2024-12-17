package com.nghiangong.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.nghiangong.constant.ContractStatus;
import com.nghiangong.dto.request.invoice.CheckoutInvoiceReq;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import com.nghiangong.model.elecCalculator.EC1;
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
import com.nghiangong.model.BaseRentCalculator;
import com.nghiangong.model.OtherFeeCalculator;
import com.nghiangong.model.electricityCalculator.EC2;
import com.nghiangong.model.waterCalculator.WC1;
import com.nghiangong.model.waterCalculator.WC4;
import com.nghiangong.repository.HouseRepository;
import com.nghiangong.repository.InvoiceRepository;
import com.nghiangong.repository.RoomRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

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

    public void createInvoice(int houseId, LocalDate date) {
        List<Room> rooms = roomRepository.findRentedRoomsWithoutInvoice(houseId, date);
        if (rooms.size() == 0) throw new AppException(ErrorCode.INVOICE_OF_MONTH_EXISTED);
        House house = houseRepository.findById(houseId).orElseThrow();
        try {
            List<Invoice> invoices = new ArrayList<>();
            for (var room : rooms) {
                Contract contract = room.getCurrentContract();
                Invoice newInvoice = new Invoice();
                newInvoice.setCreateDate(LocalDate.now());
                newInvoice.setName("Hóa đơn tháng " + date.format(DateTimeFormatter.ofPattern("MM/yyyy")));
                newInvoice.setStartDate(getLatestDate(date.withDayOfMonth(1), contract.getStartDate()));
                newInvoice.setEndDate(date);
                newInvoice.setStatus(InvoiceStatus.UNPAID);
                newInvoice.setContract(contract);
                invoices.addLast(newInvoice);
            }
            BaseRentCalculator.calculate(invoices);
            OtherFeeCalculator.calculate(invoices);
            elecCalculator(house, invoices);
            waterCalculator(house, invoices);
            invoiceRepository.saveAll(invoices);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional
    public void createCheckoutInvoice(CheckoutInvoiceReq request) {
        Contract contract = contractRepository.findById(request.getContractId())
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
        if (contract.getStatus() == ContractStatus.PENDING_PAYMENT)
            throw new AppException(ErrorCode.INVOICE_EXISTED);
        contract.setEndELecNumber(request.getEndElecNumber());
        contract.setEndWaterNumber(request.getEndWaterNumber());

        Invoice newInvoice = new Invoice();
        newInvoice.setCheckout(true);
        newInvoice.setCreateDate(LocalDate.now());
        newInvoice.setName("Hóa đơn trả phòng ");
        newInvoice.setStartDate(contract.getEndDate().withDayOfMonth(1));
        newInvoice.setEndDate(contract.getEndDate());
        newInvoice.setStatus(InvoiceStatus.UNPAID);
        newInvoice.setContract(contract);

        BaseRentCalculator.calculate(newInvoice);
        OtherFeeCalculator.calculate(newInvoice);
        elecCalculator(newInvoice);
        waterCalculator(newInvoice);
        invoiceRepository.save(newInvoice);
    }

    private void elecCalculator(House house, List<Invoice> invoices) {
        switch (house.getElecChargeCalc()) {
            case EC1:
                EC1.calculate(elecWaterRecordService, invoices);
                break;
            case EC2:
                EC2.calculate(elecWaterRecordService, invoices);
                break;
//            case EC3:
//                EC3.calculate(elecWaterRecordService, invoices);
//                break;
            default:
                throw new IllegalArgumentException("Tòa nhà chưa có cách tính tiền điện");
        }
    }

    private void elecCalculator(Invoice invoice) {
        switch (invoice.getContract().getRoom().getHouse().getElecChargeCalc()) {
            case EC1:
                EC1.calculate(elecWaterRecordService, invoice);
                break;
            case EC2:
                EC2.calculate(elecWaterRecordService, invoice);
                break;
            default:
                throw new IllegalArgumentException("Tòa nhà chưa có cách tính tiền điện");
        }
    }

    private void waterCalculator(House house, List<Invoice> invoices) {
        switch (house.getWaterChargeCalc()) {
            case WC1:
                WC1.calculate(elecWaterRecordService, invoices);
                break;
//            case WC2:
//                WC2.calculate(elecWaterRecordService, invoices);
//                break;
//            case WC3:
//                WC3.calculate(elecWaterRecordService, invoices);
//                break;
            case WC4:
                WC4.calculate(elecWaterRecordService, invoices);
                break;
            default:
                throw new IllegalArgumentException("Không hỗ trợ loại tính phí nước này.");
        }
    }

    private void waterCalculator(Invoice invoice) {
        switch (invoice.getContract().getRoom().getHouse().getWaterChargeCalc()) {
            case WC1:
                WC1.calculate(elecWaterRecordService, invoice);
                break;
            case WC4:
                WC4.calculate(elecWaterRecordService, invoice);
                break;
            default:
                throw new IllegalArgumentException("Không hỗ trợ loại tính phí nước này.");
        }
    }



    public List<InvoiceRes> getList(int houseId) {
        return invoiceRepository.findAllByHouseId(houseId).stream()
                .map(invoiceMapper::toInvoiceRes)
                .toList();
    }

    public static LocalDate getLatestDate(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    public List<InvoiceDetailRes> getList() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int id = Integer.parseInt(authentication.getName());

        return invoiceRepository.findByContractRoomHouseManagerId(id).stream().map(invoiceMapper::toInvoiceDetailRes).toList();
    }

    @Transactional
    public void setPaid(Integer invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        invoice.setStatus(InvoiceStatus.PAID);
    }
}
