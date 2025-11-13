package com.synerge.order101.purchase.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.entity.ProductSupplier;
import com.synerge.order101.product.model.repository.ProductRepository;
import com.synerge.order101.product.model.repository.ProductSupplierRepository;
import com.synerge.order101.purchase.exception.PurchaseErrorCode;
import com.synerge.order101.purchase.model.dto.PurchaseCreateRequest;
import com.synerge.order101.purchase.model.dto.PurchaseDetailResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseSummaryResponseDto;
import com.synerge.order101.purchase.model.dto.PurchaseUpdateStatusResponseDto;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.purchase.model.entity.PurchaseDetail;
import com.synerge.order101.purchase.model.repository.PurchaseDetailRepository;
import com.synerge.order101.purchase.model.repository.PurchaseRepository;
import com.synerge.order101.supplier.model.entity.Supplier;
import com.synerge.order101.supplier.model.repository.SupplierRepository;
import com.synerge.order101.user.model.entity.User;
import com.synerge.order101.user.model.repository.UserRepository;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import com.synerge.order101.warehouse.model.repository.WarehouseRepository;
import com.synerge.order101.warehouse.model.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 *  TODO : 발주 목록 조회에 공급가 적용 필요.
 *  TODO : 발주 목록 조회 페이지 동적 쿼리 변경
 *  TODO : 발주 생성 시 공급가 가져오는 로직 필요.
 *  TODO : 발주 목록 조회 최적화 필요
 *  TODO : 주 변경 로그 필요 유무 스펙 확인 필요.
 *  # 박진우
 *
 */
@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductSupplierRepository productSupplierRepository;

    private final InventoryService inventoryService;

    // 발주 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<PurchaseSummaryResponseDto> findPurchases(OrderStatus status, Integer page, Integer size) {

        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size > 0) ? size : 10;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PurchaseSummaryResponseDto> pageResult;

        if(status == null){
            pageResult = purchaseRepository.findOrderAllStatus(pageable);
        }
        else{
            pageResult = purchaseRepository.findByOrderStatus(status, pageable);
        }

        return pageResult.getContent();
    }

    // 발주 상세 조회
    @Override
    @Transactional(readOnly = true)
    public PurchaseDetailResponseDto findPurchaseDetailsById(Long purchaseId) {

        Purchase purchase = purchaseRepository.findById(purchaseId).orElseThrow(
                ()-> new CustomException(PurchaseErrorCode.PURCHASE_NOT_FOUND
        ));

        List<PurchaseDetail> details = purchaseDetailRepository.findByPurchase_PurchaseId(purchaseId);

        // DTO 변환
        PurchaseDetailResponseDto.PurchaseItemDto[] items = details.stream()
                .map(PurchaseDetailResponseDto.PurchaseItemDto::fromEntity)
                .toArray(PurchaseDetailResponseDto.PurchaseItemDto[]::new);

        return PurchaseDetailResponseDto.builder()
                .purchaseId(purchase.getPurchaseId())
                .poNo(purchase.getPoNo())
                .supplierName(purchase.getSupplier().getSupplierName())
                .requesterName(purchase.getUser().getName())
                .purchaseItems(List.of(items))
                .build();

    }

    // 발주 생성
    @Override
    @Transactional
    public void createPurchase(PurchaseCreateRequest request) {

        // 유저 GET
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED)
        );

        // 창고 GET
        Warehouse warehouse = warehouseRepository.findById(request.getWarehouseId()).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED)
        );

        // 공급사 GET
        Supplier supplier = supplierRepository.findById(request.getSupplierId()).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED)
        );

        Purchase purchase = Purchase.builder()
                .supplier(supplier)
                .user(user)
                .warehouse(warehouse)
                .orderType(request.getOrderType())
                .build();

        purchaseRepository.save(purchase);

        List<PurchaseDetail> detailsToSave = new ArrayList<>();
        for (PurchaseCreateRequest.Item item : request.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElseThrow(
                    () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED));

            ProductSupplier productSupplier = productSupplierRepository
                    .findByProductAndSupplier(product, supplier) // supplier 객체는 이미 상위에서 조회됨
                    .orElseThrow(() -> new CustomException(PurchaseErrorCode.PURCHASE_NOT_FOUND));

            PurchaseDetail detail = PurchaseDetail.builder()
                    .product(product)
                    .purchase(purchase)
                    .orderQty(item.getOrderQty())
                    .deadline(request.getDeadline())
                    .unitPrice(productSupplier.getPurchasePrice())
                    .build();

            detailsToSave.add(detail);
        }

        purchaseDetailRepository.saveAll(detailsToSave);

    }



    @Override
    @Transactional
    public PurchaseUpdateStatusResponseDto updatePurchaseStatus(Long purchaseOrderId, OrderStatus newStatus) {
        Purchase purchase = purchaseRepository.findById(purchaseOrderId).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_NOT_FOUND)
        );

        OrderStatus orderStatus = purchase.getOrderStatus();

        purchase.updateOrderStatus(newStatus);

        OrderStatus curOrderStatus = purchase.getOrderStatus();

        // 입고 반영
        if (newStatus == OrderStatus.CONFIRMED) {
            inventoryService.increaseInventory(purchase);
        }

        return PurchaseUpdateStatusResponseDto.builder()
                .purchaseId(purchase.getPurchaseId())
                .status(purchase.getOrderStatus().name())
                .build();
    }
}
