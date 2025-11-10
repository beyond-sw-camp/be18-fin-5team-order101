package com.synerge.order101.order.model.service;

import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.order.exception.errorcode.OrderErrorCode;
import com.synerge.order101.order.model.dto.*;
import com.synerge.order101.order.model.entity.StoreOrder;
import com.synerge.order101.order.model.entity.StoreOrderDetail;
import com.synerge.order101.order.model.entity.StoreOrderStatusLog;
import com.synerge.order101.order.model.repository.StoreOrderDetailRepository;
import com.synerge.order101.order.model.repository.StoreOrderRepository;
import com.synerge.order101.order.model.repository.StoreOrderStatusLogRepository;
import com.synerge.order101.product.model.entity.Product;
import com.synerge.order101.product.model.repository.ProductRepository;
import com.synerge.order101.store.model.entity.Store;
import com.synerge.order101.store.model.repository.StoreRepository;
import com.synerge.order101.user.model.entity.User;
import com.synerge.order101.user.model.repository.UserRepository;
import com.synerge.order101.warehouse.model.entity.Warehouse;
import com.synerge.order101.warehouse.model.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreOrderServiceImpl implements StoreOrderService {

    private final StoreOrderRepository storeOrderRepository;
    private final StoreOrderDetailRepository storeOrderDetailRepository;
    private final StoreOrderStatusLogRepository storeOrderStatusLogRepository;

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final WarehouseRepository warehouseRepository;

    /**
     * 주문 목록을 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StoreOrderSummaryResponseDto> findOrders(OrderStatus status, Integer page) {

        int pageNum = (page == null || page < 0) ? 0 : page;
        int pageSize = 10; // 기본 페이지 크기 설정
        Pageable pageable = PageRequest.of(pageNum, pageSize,Sort.by(Sort.Direction.DESC, "orderDatetime"));
        Page<StoreOrder> pageResult;
        if(status == null){
            pageResult = storeOrderRepository.findOrderAllStatus(pageable);
        }
        else{
            pageResult = storeOrderRepository.findByOrderStatus(status, pageable);
        }

        return pageResult.stream()
                .map(order -> StoreOrderSummaryResponseDto.builder()
                        .storeOrderId(order.getStoreOrderId())
                        .orderNo(order.getOrderNo())
                        .storeName(order.getStore() == null ? null : order.getStore().getStoreName())
                        .orderDate(order.getOrderDatetime())
                        .orderStatus(order.getOrderStatus() == null ? null : order.getOrderStatus().name())
                        .itemCount(order.getStoreOrderDetails() == null ? 0 : order.getStoreOrderDetails().size())
                        .totalQTY(order.getStoreOrderDetails()== null ? 0 : order.getStoreOrderDetails().stream()
                                .map(detail -> detail.getOrderQty() == null ? BigDecimal.ZERO : detail.getOrderQty())
                                .reduce(BigDecimal.ZERO, BigDecimal::add).intValue())
                        .build())
                .toList();
    }

    /**
     * 새로운 주문을 생성합니다.
     */
    @Transactional
    @Override
    public StoreOrderCreateResponseDto createOrder(StoreOrderCreateRequest request) {

        // store 조회
        Store store = null;
        store = storeRepository.findById(request.getStoreId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.STORE_NOT_FOUND));

        // warehouse 조회
        Warehouse warehouse = null;
        warehouse = warehouseRepository.findById(request.getWarehouseId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.WAREHOUSE_NOT_FOUND));

        // user 조회
        User user = null;
        user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        StoreOrder order = new StoreOrder(store, warehouse, user, request.getRemark());
        StoreOrder savedOrder = storeOrderRepository.save(order);

        // create and persist details
        if (request.getItems() != null) {
            for (StoreOrderCreateRequest.Item item : request.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

                BigDecimal unitPrice = product.getPrice();
                BigDecimal amount = unitPrice == null ? null : unitPrice.multiply(item.getOrderQty());

                StoreOrderDetail detail = new StoreOrderDetail(savedOrder, product, item.getOrderQty(), unitPrice, amount);
                storeOrderDetailRepository.save(detail);
            }
        }

        return StoreOrderCreateResponseDto.builder()
                .storeOrderId(savedOrder.getStoreOrderId())
                .orderNo(savedOrder.getOrderNo())
                .build();
    }

    /**
     * 특정 ID의 주문 상세 정보를 조회합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public StoreOrderDetailResponseDto findStoreOrderDetails(Long storeOrderId) {

        StoreOrder order = storeOrderRepository.findById(storeOrderId).orElseThrow(() ->
                new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        // ensure details loaded (could use repository to fetch details separately)
        List<StoreOrderDetail> details = storeOrderDetailRepository.findByStoreOrder_StoreOrderId(storeOrderId);

        // Build DTO manually using details and order
        StoreOrderDetailResponseDto.OrderItemDto[] items = details.stream()
                .map(StoreOrderDetailResponseDto.OrderItemDto::fromEntity)
                .toArray(StoreOrderDetailResponseDto.OrderItemDto[]::new);

        StoreOrderDetailResponseDto responseDto = StoreOrderDetailResponseDto.builder()
                .storeOrderId(order.getStoreOrderId())
                .storeName(order.getStore()==null?null:order.getStore().getStoreName())
                .status(order.getOrderStatus()==null?null:order.getOrderStatus().name())
                .orderDate(order.getOrderDatetime())
                .orderItems(List.of(items))
                .build();

        return responseDto;

    }

    /**
     * 주문을 승인합니다.
     */
    @Override
    @Transactional
    public StoreOrderUpdateStatusResponseDto approveOrder(Long storeOrderId) {

        StoreOrder order = storeOrderRepository.findById(storeOrderId).orElseThrow(() ->
                new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        OrderStatus prev = order.getOrderStatus();

        order.approve();

        OrderStatus curStatus = order.getOrderStatus();

        StoreOrderStatusLog log = StoreOrderStatusLog.builder()
                .storeOrder(order)
                .prevStatus(prev)
                .curStatus(curStatus)
                .build();
        storeOrderStatusLogRepository.save(log);

        return StoreOrderUpdateStatusResponseDto.builder()
                .storeOrderId(order.getStoreOrderId())
                .status(order.getOrderStatus().name())
                .build();
    }

    /**
     * 주문을 거부합니다.
     */
    @Override
    @Transactional
    public StoreOrderUpdateStatusResponseDto rejectOrder(Long storeOrderId) {
        // 실제 구현 로직: ID로 주문 조회 -> 상태 업데이트 -> 저장 -> ResponseDto 반환
        // 현재는 컴파일 가능하도록 더미 객체를 반환합니다.
        StoreOrder order = storeOrderRepository.findById(storeOrderId).orElseThrow(() ->
                new CustomException(OrderErrorCode.ORDER_NOT_FOUND));

        OrderStatus prev = order.getOrderStatus();

        order.reject();

        OrderStatus curStatus = order.getOrderStatus();

        StoreOrderStatusLog log = StoreOrderStatusLog.builder()
                .storeOrder(order)
                .prevStatus(prev)
                .curStatus(curStatus)
                .build();

        storeOrderStatusLogRepository.save(log);


        return StoreOrderUpdateStatusResponseDto.builder()
                .storeOrderId(order.getStoreOrderId())
                .status(order.getOrderStatus().name())
                .build();
    }
}