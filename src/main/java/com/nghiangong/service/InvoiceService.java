package com.nghiangong.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.nghiangong.dto.request.invoice.CheckoutInvoiceReq;
import com.nghiangong.dto.request.invoice.InvoiceOfHouseReq;
import com.nghiangong.dto.request.invoice.InvoiceReq;
import com.nghiangong.dto.response.invoice.CreateInvoiceStatus;
import com.nghiangong.dto.response.invoice.CreateInvoicesRes;
import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.user.Manager;
import com.nghiangong.exception.AppException;
import com.nghiangong.repository.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nghiangong.entity.House;
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
    public InvoiceDetailRes createInvoice(int contractId, InvoiceReq request) {
        var manager = getManager();
        var contract = manager.getContract(contractId);

        var newInvoice = contract.createInvoice(request.getMonth());
        return invoiceMapper.toInvoiceDetailRes(newInvoice);
    }

    @Transactional
    public CreateInvoicesRes createInvoices(InvoiceOfHouseReq request) {
        var manager = getManager();
        var house = manager.getHouse(request.getHouseId());
        var month = request.getMonth();

        CreateInvoicesRes response = new CreateInvoicesRes();
        response.setHouseName(house.getName());
        response.setMonth(month);
        var statuses = new ArrayList<CreateInvoiceStatus>();
        response.setStatuses(statuses);

        house.getRooms().stream()
                .filter(room -> room.getHouse().isActive())
                .forEach(room -> {
                    var status = new CreateInvoiceStatus();
                    statuses.add(status);
                    status.setRoomName(room.getName());

                    Optional<Contract> optionalContract = room.getContracts().stream()
                            .filter(contract -> contract.contain(month)).findFirst();
                    if (optionalContract.isPresent()) {
                        Contract contract = optionalContract.get();
                        if (contract.isHavingInvoice(month)) {
                            status.setStatus("WARN");
                            status.setMessage("Hóa đơn tháng đã tồn tại");
                            return;
                        } else {
                            try {
                                contract.createInvoice(month);
                                status.setStatus("SUCCESS");
                                status.setMessage("Hóa đơn đã được tạo thành công");
                                return;
                            } catch (AppException e) {
                                status.setStatus("ERROR");
                                status.setMessage(e.getMessage());
                                return;
                            }
                        }
                    } else {
                        status.setStatus("WARN");
                        status.setMessage("Không có hợp đồng thuê tháng này");
                        return;
                    }
                });
        return response;
    }

    @Transactional
    public InvoiceDetailRes createCheckoutInvoice(int contractId, CheckoutInvoiceReq request) {
        var manager = getManager();
        var contract = manager.getContract(contractId);
        House house = contract.getRoom().getHouse();
        if (house.isHavingElecIndex()) contract.setEndElecNumber(request.getEndElecNumber());
        if (house.isHavingWaterIndex()) contract.setEndWaterNumber(request.getEndWaterNumber());
        var newInvoice = contract.createCheckoutInvoice();
        return invoiceMapper.toInvoiceDetailRes(newInvoice);
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
