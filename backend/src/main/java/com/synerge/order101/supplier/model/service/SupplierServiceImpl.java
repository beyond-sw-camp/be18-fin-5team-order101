package com.synerge.order101.supplier.model.service;

import com.synerge.order101.common.dto.ItemsResponseDto;
import com.synerge.order101.supplier.model.dto.SupplierListRes;
import com.synerge.order101.supplier.model.entity.Supplier;
import com.synerge.order101.supplier.model.repository.SupplierRepository;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;

    @Override
    @Transactional(readOnly = true)
    public ItemsResponseDto<SupplierListRes> getSuppliers(int page, int numOfRows, String keyword) {
        int pageIndex = Math.max(0, page - 1);

        Pageable pageable = PageRequest.of(pageIndex, numOfRows, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Supplier> resultPage;
        if(keyword != null && !keyword.isBlank()) {
            resultPage = supplierRepository.findBySupplierNameContainingIgnoreCase(keyword, pageable);
        }else {
            resultPage = supplierRepository.findAll(pageable);
        }

        List<SupplierListRes> items = resultPage.getContent().stream()
                .map(s -> SupplierListRes.builder()
                        .supplierId(s.getSupplierId())
                        .supplierCode(s.getSupplierCode())
                        .supplierName(s.getSupplierName())
                        .address(s.getAddress())
                        .contactName(s.getContactName())
                        .contactNumber(s.getContactNumber())
                        .createdDate(s.getCreatedAt())
                        .build())
                .toList();
        int totalCount = (int) resultPage.getTotalElements();

        return new ItemsResponseDto<>(HttpStatus.OK, items, page, totalCount);
    }


}
