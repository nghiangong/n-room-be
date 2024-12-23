package com.nghiangong.entity.room;

import java.time.LocalDate;
import java.util.List;

import com.nghiangong.dto.request.room.RecordReq;
import com.nghiangong.entity.elecwater.ElecRecordOfRoom;
import com.nghiangong.exception.AppException;
import com.nghiangong.exception.ErrorCode;
import com.nghiangong.model.DateUtils;
import jakarta.persistence.*;

import com.nghiangong.constant.RoomStatus;
import com.nghiangong.entity.House;
import com.nghiangong.entity.elecwater.WaterRecordOfRoom;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String name;
    int price;

    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "house_id", referencedColumnName = "id")
    House house;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("endDate DESC")
    List<Contract> contracts;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    List<Furniture> furnitureList;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("date DESC")
    List<ElecRecordOfRoom> elecRecords;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @OrderBy("date DESC")
    List<WaterRecordOfRoom> waterRecords;

    @Transient
    RoomStatus status;

    @Transient
    Contract currentContract;

    public void setActive(boolean status) {
        if (status == true) {
            if (!house.isActive())
                throw new AppException(ErrorCode.HOUSE_INACTIVE);
            active = true;
        } else {
            active = false;
        }
    }

    public void createContract(Contract newContract) {
        if (status == RoomStatus.INACTIVE)
            throw new AppException(ErrorCode.ROOM_INACTIVE);
        for (Contract contract : contracts)
            if (contract.isIntersecting(newContract))
                throw new AppException(ErrorCode.CONTRACT_PERIOD_INTERSECTING);
        newContract.setRoom(this);
        contracts.add(newContract);
    }

    public void updateContract(Contract updatingcontract) {
        if (status == RoomStatus.INACTIVE)
            throw new AppException(ErrorCode.ROOM_INACTIVE);
        for (Contract contract : contracts)
            if (contract != updatingcontract)
                if (contract.isIntersecting(updatingcontract))
                    throw new AppException(ErrorCode.CONTRACT_PERIOD_INTERSECTING);
        updatingcontract.setRoom(this);
    }

    public int getElecIndex(LocalDate month) {
        var end = DateUtils.endOfMonth(month);
        var result = elecRecords.stream().filter(record -> record.getDate().isEqual(end)).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.ELEC_NUMBER_NOT_ENTERED));
        return result.getValue();
    }

    public int getWaterIndex(LocalDate month) {
        var end = DateUtils.endOfMonth(month);
        var result = waterRecords.stream().filter(record -> record.getDate().isEqual(end)).findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.WATER_NUMBER_NOT_ENTERED));
        return result.getValue();
    }

    public ElecRecordOfRoom getElecRecord(LocalDate month) {
        var end = DateUtils.endOfMonth(month);
        var result = elecRecords.stream().filter(record -> record.getDate().equals(end)).findFirst()
                .orElse(null);
        return result;
    }

    public WaterRecordOfRoom getWaterRecord(LocalDate month) {
        var end = DateUtils.endOfMonth(month);
        var result = waterRecords.stream().filter(record -> record.getDate().equals(end)).findFirst()
                .orElse(null);
        return result;
    }

    public void addElecRecord(LocalDate date, int value) {
        date = DateUtils.endOfMonth(date);
        System.out.println(1);
        if (!validateElecIndex(date, value))
            throw new AppException(ErrorCode.ELEC_NUMBER_NOT_VALID);
        System.out.println(2);
        var record = getElecRecord(date);
        if (record == null)
            elecRecords.add(ElecRecordOfRoom.builder()
                    .room(this)
                    .date(date)
                    .value(value)
                    .build());
        else record.setValue(value);
        System.out.println(3);
    }

    public void addWaterRecord(LocalDate date, int value) {
        date = DateUtils.endOfMonth(date);
        if (!validateWaterIndex(date, value))
            throw new AppException(ErrorCode.WATER_NUMBER_NOT_VALID);
        var record = getWaterRecord(date);
        if (record == null)
            waterRecords.add(WaterRecordOfRoom.builder()
                    .room(this)
                    .date(date)
                    .value(value)
                    .build());
        else record.setValue(value);
    }

    public boolean validateElecIndex(LocalDate date, int value) {
        var records = elecRecords;
        if (records.isEmpty()) return true;
        for (int i = 0; i < records.size(); i++) {
            var record = records.get(i);
            if (record.getDate().isAfter(date) && record.getValue() < value) return false;
            if (record.getDate().isBefore(date) && record.getValue() > value) return false;
        }
        return true;
    }

    public boolean validateWaterIndex(LocalDate date, int value) {
        var records = waterRecords;
        if (records.isEmpty()) return true;
        for (int i = 0; i < records.size(); i++) {
            var record = records.get(i);
            if (record.getDate().isAfter(date) && record.getValue() < value) return false;
            if (record.getDate().isBefore(date) && record.getValue() > value) return false;
        }
        return true;
    }


    public void addWaterRecord(RecordReq request) {
        var record = getWaterRecord(request.getDate());
        record.setValue(request.getValue());
    }

    @PostLoad
    private void initializeTransients() {
        currentContract = initializeCurrentContract();
        status = initializeStatus();
    }

    @PreRemove
    private void preRemove() {
        if (contracts != null && contracts.size() > 0)
            throw new AppException(ErrorCode.NOT_DELETE_ROOM);
    }

    Contract initializeCurrentContract() {
        LocalDate today = LocalDate.now();
        return contracts.stream()
                .filter(contract ->
                        !contract.getStartDate().isAfter(today) &&
                        (contract.getEndDate() == null || !contract.getEndDate().isBefore(today))
                )
                .findFirst().orElse(null);
    }

    RoomStatus initializeStatus() {
        if (!house.isActive() || !active) return RoomStatus.INACTIVE;
        if (currentContract == null) return RoomStatus.AVAILABLE;
        switch (currentContract.getStatus()) {
            case ACTIVE:
                return RoomStatus.OCCUPIED;
            case SOON_EXPIRED:
            case PENDING_CHECKOUT_OR_INVOICE:
                return RoomStatus.SOON_AVAILABLE;
            case EXPIRED:
                return RoomStatus.AVAILABLE;
            default:
                return null;
        }
    }
}
