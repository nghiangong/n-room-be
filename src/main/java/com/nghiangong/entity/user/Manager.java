package com.nghiangong.entity.user;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.House;
import com.nghiangong.entity.room.Contract;
import com.nghiangong.entity.room.Invoice;
import com.nghiangong.entity.room.Room;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Manager extends User {
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL)
    List<House> houses;

    public void createHouse(House newHouse) {
        houses.add(newHouse);
        newHouse.setManager(this);
        newHouse.setActive(true);
    }

    public void deleteHouse(House house) {
        houses.remove(house);
    }

    public List<Room> getRooms() {
        return houses.stream()
                .filter(house -> house.getRooms() != null)
                .flatMap(house -> house.getRooms().stream())
                .collect(Collectors.toList());
    }

    public List<Contract> getContracts() {
        return getRooms().stream()
                .filter(room -> room.getContracts() != null)
                .flatMap(room -> room.getContracts().stream())
                .collect(Collectors.toList());
    }

    public List<Invoice> getInvoices() {
        return getContracts().stream()
                .filter(contract -> contract.getInvoices() != null)
                .flatMap(contract -> contract.getInvoices().stream())
                .collect(Collectors.toList());
    }

    public List<Tenant> getTenants() {


        return getContracts().stream()
                .filter(contract -> contract.getRepTenant().getMembers() != null)
                .flatMap(contract -> contract.getRepTenant().getMembers().stream())
                .collect(Collectors.toList());
    }

    public House getHouse(int id) {
        return getHouses().stream().filter(house -> house.getId() == id).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.HOUSE_NOT_EXISTED));
    }

    public Room getRoom(int id) {
        return getRooms().stream().filter(room -> room.getId() == id).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
    }

    public Contract getContract(int id) {
        return getContracts().stream().filter(contract -> contract.getId() == id).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.CONTRACT_NOT_EXISTED));
    }

    public Invoice getInvoice(int id) {
        return getInvoices().stream().filter(invoice -> invoice.getId() == id).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));
    }

    public Tenant getTenant(int id) {
        return getTenants().stream().filter(tenant -> tenant.getId() == id).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.TENANT_NOT_EXIST));
    }
}
