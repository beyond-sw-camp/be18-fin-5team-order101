package com.synerge.order101.ai.service;

import com.synerge.order101.ai.exception.AiErrorCode;
import com.synerge.order101.ai.model.dto.request.SmartOrderUpdateRequestDto;
import com.synerge.order101.ai.model.dto.response.SmartOrderDashboardResponseDto;
import com.synerge.order101.ai.model.dto.response.SmartOrderResponseDto;
import com.synerge.order101.ai.model.entity.DemandForecast;
import com.synerge.order101.ai.model.entity.SmartOrder;
import com.synerge.order101.ai.repository.DemandForecastRepository;
import com.synerge.order101.ai.repository.SmartOrderRepository;
import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.product.model.repository.ProductSupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SmartOrderService {
    private final SmartOrderRepository smartOrderRepository;
    private final DemandForecastRepository demandForecastRepository;
    private final ProductSupplierRepository productSupplierRepository;

    //AI가 스마트 발주 초안 작성
    @Transactional
    public List<SmartOrderResponseDto> generateSmartOrders(LocalDate targetWeek) {

        // 같은 주차에 이미 스마트발주 존재하는 경우
        List<SmartOrder> existing = smartOrderRepository.findByTargetWeek(targetWeek);
        if (!existing.isEmpty()) {
            throw new CustomException(AiErrorCode.SMART_ORDER_ALREADY_EXISTS);
        }

        // 1) 해당 주차의 수요 예측 조회
        List<DemandForecast> forecasts = demandForecastRepository.findByTargetWeek(targetWeek);
        if (forecasts.isEmpty()) {
            throw new CustomException(AiErrorCode.FORECAST_NOT_FOUND);
        }

        // 2) 상품  공급사 매핑 후 스마트 발주 생성
        List<SmartOrder> saved = forecasts.stream()
                .map(df -> {
                    var product = df.getProduct();

                    var mapping = productSupplierRepository.findByProduct(product)
                            .orElseThrow(() ->
                                    new CustomException(AiErrorCode.SUPPLIER_MAPPING_NOT_FOUND)
                            );

                    return SmartOrder.builder()
                            .supplier(mapping.getSupplier())
                            .product(product)
                            .demandForecast(df)
                            .targetWeek(df.getTargetWeek())
                            .forecastQty(df.getYPred())
                            .recommendedOrderQty(df.getYPred())
                            .smartOrderStatus(OrderStatus.DRAFT_AUTO)
                            .build();
                })
                .map(smartOrderRepository::save)
                .toList();

        return saved.stream()
                .map(this::toResponse)
                .toList();
    }


    // 스마트 발주 목록 조회
    public List<SmartOrderResponseDto> getSmartOrders(
            OrderStatus status, LocalDate from, LocalDate to
    ) {
        List<SmartOrder> list;

        boolean hasStatus = (status != null);
        boolean hasRange = (from != null && to != null);

        if (hasStatus && hasRange) {
            list = smartOrderRepository
                    .findBySmartOrderStatusAndTargetWeekBetween(status, from, to);
        } else if (hasStatus) {
            list = smartOrderRepository.findBySmartOrderStatus(status);
        } else if (hasRange) {
            list = smartOrderRepository.findByTargetWeekBetween(from, to);
        } else {
            list = smartOrderRepository.findAllByOrderByTargetWeekDesc();
        }

        return list.stream().map(this::toResponse).toList();
    }


    // 스마트 발주 상세 조회
    public SmartOrderResponseDto getSmartOrder(Long id) {
        SmartOrder entity = smartOrderRepository.findById(id)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));
        return toResponse(entity);
    }

    //스마트 발주 수정
    @Transactional
    public SmartOrderResponseDto updateDraft(Long smartOrderId, SmartOrderUpdateRequestDto request) {
        SmartOrder entity = smartOrderRepository.findById(smartOrderId)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));

        entity.updateDraft(request.getRecommendedOrderQty());

        return toResponse(entity);
    }

    // 제출
    @Transactional
    public SmartOrderResponseDto submit(Long smartOrderId) {
        SmartOrder entity = smartOrderRepository.findById(smartOrderId)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));
        entity.submit();

        return toResponse(entity);
    }

    // 대시보드 상단 요약 카드
    public SmartOrderDashboardResponseDto getSmartOrderSummary(LocalDate targetWeek) {
        List<SmartOrder> list = smartOrderRepository.findByTargetWeek(targetWeek);

        long totalRecommended = list.stream()
                .mapToLong(so -> (long) so.getRecommendedOrderQty())
                .sum();

        long totalForecast = list.stream()
                .mapToLong(so -> (long) so.getForecastQty())
                .sum();

        long draftCount = list.stream()
                .filter(so -> so.getSmartOrderStatus() == OrderStatus.DRAFT_AUTO)
                .count();

        long submittedCount = list.stream()
                .filter(so -> so.getSmartOrderStatus() == OrderStatus.SUBMITTED)
                .count();

        return SmartOrderDashboardResponseDto.builder()
                .targetWeek(targetWeek)
                .totalRecommendedQty(totalRecommended)
                .totalForecastQty(totalForecast)
                .draftCount((int) draftCount)
                .submittedCount((int) submittedCount)
                .build();
    }



    private SmartOrderResponseDto toResponse(SmartOrder so) {
        return SmartOrderResponseDto.builder()
                .id(so.getSmartOrderId())
                .supplierId(so.getSupplier().getSupplierId())
                .productId(so.getProduct().getProductId())
                .demandForecastId(so.getDemandForecast().getDemandForecastId())
                .targetWeek(so.getTargetWeek())
                .recommendedOrderQty(so.getRecommendedOrderQty())
                .forecastQty(so.getForecastQty())
                .smartOrderStatus(so.getSmartOrderStatus())
                .snapshotAt(so.getSnapshotAt())
                .updatedAt(so.getUpdatedAt())
                .build();
    }
}