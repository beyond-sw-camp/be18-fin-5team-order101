package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.entity.ProductSupplier;
import com.synerge.order101.product.model.repository.ProductRepository;
import com.synerge.order101.purchase.model.entity.Purchase;
import com.synerge.order101.warehouse.model.dto.response.InventoryResponseDto;
import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import com.synerge.order101.warehouse.model.repository.WarehouseInventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;

    @Override
    @Transactional
    public List<InventoryResponseDto> getInventoryList() {

        return warehouseInventoryRepository.findAllWithProduct()
                .stream()
                .map(InventoryResponseDto::fromEntity)
                .toList();
    }

    // 출고 반영
    @Override
    @Transactional
    public void decreaseInventory(Long productId, int quantity) {
        WarehouseInventory inventory = warehouseInventoryRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new IllegalStateException("해당 상품의 재고를 찾을 수 없습니다."));

        inventory.decrease(quantity);
    }

    // 입고 반영
    @Override
    @Transactional
    public void increaseInventory(Purchase purchase) {
        purchase.getPurchaseDetails().forEach(detail -> {
            WarehouseInventory inventory = warehouseInventoryRepository.findByProduct_ProductId(detail.getProduct().getProductId())
                    .orElseThrow(() -> new IllegalStateException("해당 상품의 재고를 찾을 수 없습니다: " + detail.getProduct().getProductId()));

            inventory.increase(detail.getOrderQty().intValue());
        });
    }

    // 안전재고 업데이트
    @Override
    @Transactional
    public void updateDailySafetyStock() {
        List<WarehouseInventory> inventoryList =
                warehouseInventoryRepository.findAllWithProductAndSupplier();

        for (WarehouseInventory inv : inventoryList) {

            Long productId = inv.getProduct().getProductId();

            List<Integer> sales = storeOrderDetailRepository.findDailySalesQtySince(
                    productId,
                    LocalDateTime.now().minusDays(30)
            );

            if (sales.isEmpty()) continue;

            // 최고판매량
            int dMax = sales.stream().mapToInt(i -> i).max().orElse(0);
            // 평균판매량
            double dAvg = sales.stream().mapToInt(i -> i).average().orElse(0);

            // 리드타임
            List<ProductSupplier> psList = inv.getProduct().getProductSupplier();

            ProductSupplier ps = psList.getFirst();
            int lt = ps.getLeadTimeDays();

            // 안전재고 계산
            int safety = (int) Math.max(0,
                    Math.ceil((dMax - dAvg) * lt)
            );

            // 재고 업데이트
            inv.updateSafetyQty(safety);
        }
    }

    // 자동 발주 생성
    @Override
    public void triggerAutoPurchase() {

    }
}
