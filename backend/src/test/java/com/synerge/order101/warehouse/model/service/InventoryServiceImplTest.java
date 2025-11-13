package com.synerge.order101.warehouse.model.service;

import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.entity.ProductSupplier;
import com.synerge.order101.warehouse.model.entity.WarehouseInventory;
import com.synerge.order101.warehouse.model.repository.WarehouseInventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @Mock
    private WarehouseInventoryRepository inventoryRepository;

    @Mock
    private StoreOrderDetailRepository storeOrderDetailRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    private WarehouseInventory inv;
    private Product product;
    private ProductSupplier productSupplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // ProductSupplier Mock
        productSupplier = mock(ProductSupplier.class);
        when(productSupplier.getLeadTimeDays()).thenReturn(3);

        // Product Mock
        product = mock(Product.class);
        when(product.getProductId()).thenReturn(100L);
        List<ProductSupplier> supplierList = List.of(productSupplier);
        when(product.getProductSupplier()).thenReturn(supplierList);

        // Inventory Entity (실제 객체)
        inv = WarehouseInventory.builder()
                .inventoryId(1L)
                .product(product)
                .onHandQuantity(50)
                .safetyQuantity(0)
                .build();

    }

    @Test
    void updateDailySafetyStock() {

        // GIVEN
        when(inventoryRepository.findAllWithProductAndSupplier())
                .thenReturn(List.of(inv));

        // 판매량 더미데이터: 5, 10, 8, 12 → max = 12, avg=8.75
        when(storeOrderDetailRepository.findDailySalesQtySince(
                eq(100L),
                any(LocalDateTime.class)
        )).thenReturn(List.of(5, 10, 8, 12));

        // WHEN
        inventoryService.updateDailySafetyStock();

        // THEN
        // 안전재고 공식: (Dmax - Davg) × LT
        double avg = (5 + 10 + 8 + 12) / 4.0; // 8.75
        int expectedSafety = (int) Math.ceil((12 - avg) * 3); // (12-8.75)*3 = 9.75 → 10

        assertThat(inv.getSafetyQuantity()).isEqualTo(expectedSafety);
        System.out.println(expectedSafety);
    }
}
