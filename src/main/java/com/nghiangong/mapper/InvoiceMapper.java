package com.nghiangong.mapper;

import com.nghiangong.dto.response.invoice.InvoiceDetailRes;
import com.nghiangong.dto.response.invoice.InvoiceRes;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.nghiangong.entity.room.Invoice;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceMapper {
    InvoiceRes toInvoiceRes(Invoice invoice);

    @Mapping(source = "contract.room", target = "room")
    @Mapping(source = "contract.room.house", target = "house")
    @Mapping(source = "contract.repTenant", target = "repTenant")
    InvoiceDetailRes toInvoiceDetailRes(Invoice invoice);
}
