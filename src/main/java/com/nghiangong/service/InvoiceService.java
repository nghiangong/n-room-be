package com.nghiangong.service;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.dto.request.invoice.CheckoutInvoiceReq;
import com.nghiangong.dto.request.invoice.InvoiceOfHouseReq;
import com.nghiangong.dto.request.invoice.InvoiceReq;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.model.*;
import com.nghiangong.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Invoice;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.mapper.InvoiceMapper;

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
    ManagerRepository managerRepository;
    InvoiceMapper invoiceMapper;
    InvoiceRepository invoiceRepository;

    @Transactional
    public List<InvoiceDetailRes> getList() {
        var manager = getManager();
        var invoices = manager.getInvoices();

        return invoices.stream()
                .map(invoiceMapper::toInvoiceDetailRes).toList();
    }

    @Transactional
    public void createInvoice(int contractId, InvoiceReq request) {
        var manager = getManager();
        var contract = manager.getContract(contractId);

        contract.createInvoice(request.getMonth());
    }

    @Transactional
    public void createInvoices(InvoiceOfHouseReq request) {
        var manager = getManager();
        var house = manager.getHouse(request.getHouseId());
        var month = request.getMonth();
        house.getRooms().stream()
                .filter(room -> room.getContracts() != null)
                .flatMap(room -> room.getContracts().stream())
                .filter(contract -> contract.contain(month)) //
                .filter(contract -> !contract.isHavingInvoice(month))
                .map(contract -> {
                    try {
                        return contract.createInvoice(month);
                    } catch (AppException e) {
                        System.out.println(contract.getRoom().getName() + " " + e.getErrorCode().getMessage());
                        return null;
                    }
                })
                .filter(invoice -> invoice != null)
                .toList();
    }

    @Transactional
    public void createCheckoutInvoice(int contractId, CheckoutInvoiceReq request) {
        var manager = getManager();
        var contract = manager.getContract(contractId);
        System.out.println(1);
        House house = contract.getRoom().getHouse();
        if (house.isHavingElecIndex()) contract.setEndElecNumber(request.getEndElecNumber());
        if (house.isHavingWaterIndex()) contract.setEndWaterNumber(request.getEndWaterNumber());
        contract.createCheckoutInvoice();
    }

    @Transactional
    public void setPaid(Integer invoiceId) {
        var invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
        invoice.setPaid();
    }

    @Transactional
    public void delete(int id) {
        var manager = getManager();
        var invoice = manager.getInvoice(id);
        var contract = invoice.getContract();
        contract.deleteInvoice(invoice);
        invoiceRepository.deleteById(id);
    }

    Manager getManager() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        int managerId = Integer.parseInt(authentication.getName());
        return managerRepository.findById(managerId).orElseThrow();
    }

}
