package com.synerge.order101.order;

import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.order.model.dto.*;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.order.model.entity.StoreOrderDetail;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.order.model.service.StoreOrderServiceImpl;
import com.synerge.order101.product.model.repository.ProductRepository;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.store.model.repository.StoreRepository;
import com.synerge.order101.user.model.entity.User;
import com.synerge.order101.user.model.repository.UserRepository;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import com.synerge.order101.warehouse.model.repository.WarehouseRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class StoreOrderServiceImplTest {

    @Mock
    private StoreOrderRepository storeOrderRepository;

    @Mock
    private StoreOrderDetailRepository storeOrderDetailRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private StoreOrderServiceImpl storeOrderService;

    @Test
    void findOrders_ReturnsOrders_WhenStatusIsNull() {
        Page<StoreOrder> mockPage = new PageImpl<>(List.of(StoreOrder.builder().build()));
        Mockito.when(storeOrderRepository.findOrderAllStatus(Mockito.any(Pageable.class))).thenReturn(mockPage);

        List<StoreOrderSummaryResponseDto> result = storeOrderService.findOrders(null, 0);

        Assertions.assertThat(result).isNotEmpty();
        Mockito.verify(storeOrderRepository).findOrderAllStatus(Mockito.any(Pageable.class));
    }

    @Test
    void findOrders_ReturnsOrders_WhenStatusIsProvided() {
        Page<StoreOrder> mockPage = new PageImpl<>(List.of(StoreOrder.builder().build()));
        Mockito.when(storeOrderRepository.findByOrderStatus(Mockito.eq(OrderStatus.CONFIRMED), Mockito.any(Pageable.class))).thenReturn(mockPage);

        List<StoreOrderSummaryResponseDto> result = storeOrderService.findOrders(OrderStatus.CONFIRMED, 0);

        Assertions.assertThat(result).isNotEmpty();
        Mockito.verify(storeOrderRepository).findByOrderStatus(Mockito.eq(OrderStatus.CONFIRMED), Mockito.any(Pageable.class));
    }

    @Test
    void createOrder_ThrowsException_WhenStoreNotFound() {
        StoreOrderCreateRequest request = StoreOrderCreateRequest.builder()
                .storeId(1L)
                .build();

        Mockito.when(storeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> storeOrderService.createOrder(request));
        Mockito.verify(storeRepository).findById(1L);
    }

    @Test
    void createOrder_SavesOrder_WhenValidRequest() {
        StoreOrderCreateRequest request = StoreOrderCreateRequest.builder()
                .storeId(1L)
                .warehouseId(2L)
                .userId(3L)
                .items(Collections.emptyList())
                .build();

        Store store = Store.builder().build();
        Warehouse warehouse = Warehouse.builder().build();
        User user = User.builder().build();
        StoreOrder savedOrder = StoreOrder.builder().build();

        Mockito.when(storeRepository.findById(1L)).thenReturn(Optional.of(store));
        Mockito.when(warehouseRepository.findById(2L)).thenReturn(Optional.of(warehouse));
        Mockito.when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        Mockito.when(storeOrderRepository.save(Mockito.any(StoreOrder.class))).thenReturn(savedOrder);

        StoreOrderCreateResponseDto result = storeOrderService.createOrder(request);

        Assertions.assertThat(result).isNotNull();
        Mockito.verify(storeOrderRepository).save(Mockito.any(StoreOrder.class));
    }

    @Test
    void findStoreOrderDetails_ThrowsException_WhenOrderNotFound() {
        Mockito.when(storeOrderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> storeOrderService.findStoreOrderDetails(1L));
        Mockito.verify(storeOrderRepository).findById(1L);
    }

    @Test
    void findStoreOrderDetails_ReturnsDetails_WhenOrderExists() {
        StoreOrder order = StoreOrder.builder().storeOrderId(1L).build();
        List<StoreOrderDetail> details = List.of(new StoreOrderDetail());

        Mockito.when(storeOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(storeOrderDetailRepository.findByStoreOrder_StoreOrderId(1L)).thenReturn(details);

        StoreOrderDetailResponseDto result = storeOrderService.findStoreOrderDetails(1L);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getOrderItems()).isNotEmpty();
        Mockito.verify(storeOrderRepository).findById(1L);
        Mockito.verify(storeOrderDetailRepository).findByStoreOrder_StoreOrderId(1L);
    }

    @Test
    void approveOrder_UpdatesStatus_WhenOrderExists() {
        StoreOrder order = StoreOrder.builder().storeOrderId(1L).build();

        Mockito.when(storeOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(storeOrderRepository.save(Mockito.any(StoreOrder.class))).thenReturn(order);

        StoreOrderUpdateStatusResponseDto result = storeOrderService.approveOrder(1L);

        Assertions.assertThat(result).isNotNull();
        Mockito.verify(storeOrderRepository).findById(1L);
        Mockito.verify(storeOrderRepository).save(Mockito.any(StoreOrder.class));
    }

    @Test
    void rejectOrder_UpdatesStatus_WhenOrderExists() {
        StoreOrder order = StoreOrder.builder().storeOrderId(1L).build();

        Mockito.when(storeOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(storeOrderRepository.save(Mockito.any(StoreOrder.class))).thenReturn(order);

        StoreOrderUpdateStatusResponseDto result = storeOrderService.rejectOrder(1L);

        Assertions.assertThat(result).isNotNull();
        Mockito.verify(storeOrderRepository).findById(1L);
        Mockito.verify(storeOrderRepository).save(Mockito.any(StoreOrder.class));
    }
}