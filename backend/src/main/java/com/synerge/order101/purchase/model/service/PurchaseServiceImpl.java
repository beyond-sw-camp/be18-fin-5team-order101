package com.synerge.order101.purchase.model.service;

import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.repository.ProductRepository;
import com.synerge.order101.purchase.exception.PurchaseErrorCode;
import com.synerge.order101.purchase.model.dto.PurchaseCreateRequest;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;

    // TODO 해당 코드 맞는지 확인 필요
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final WarehouseRepository warehouseRepository;

    // 발주 생성
    @Override
    @Transactional
    public void createPurchase(PurchaseCreateRequest request) {

        // 유저 GET
        User user = userRepository.findById(request.getUserId()).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED)
        );

        // 창고 GET
        Warehouse warehouse = warehouseRepository.findById(request.getUserId()).orElseThrow(
                () -> new CustomException(PurchaseErrorCode.PURCHASE_CREATION_FAILED)
        );

        // 공급사 GET
        Supplier supplier = supplierRepository.findById(request.getUserId()).orElseThrow(
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

            PurchaseDetail detail = PurchaseDetail.builder()
                    .product(product)
                    .purchase(purchase)
                    .orderQty(item.getOrderQty())
                    .unitPrice(item.getProductUnit())
                    .build();

            detailsToSave.add(detail);
        }


        purchaseDetailRepository.saveAll(detailsToSave);

    }


}
