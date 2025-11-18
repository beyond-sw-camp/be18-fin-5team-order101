package com.synerge.order101.ai.service;

import com.synerge.order101.ai.exception.AiErrorCode;
import com.synerge.order101.ai.model.dto.request.SmartOrderUpdateRequest;
import com.synerge.order101.ai.model.dto.response.SmartOrderResponseDto;
import com.synerge.order101.ai.model.entity.DemandForecast;
import com.synerge.order101.ai.model.entity.SmartOrder;
import com.synerge.order101.ai.repository.DemandForecastRepository;
import com.synerge.order101.ai.repository.SmartOrderRepository;
import com.synerge.order101.common.enums.OrderStatus;
import com.synerge.order101.common.exception.CustomException;
import com.synerge.order101.product.model.repository.ProductRepository;
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
    // targetWeek을 기준 demand_forecst 조회해서 smart_order DRAFT 생성함
    //TODO
    //실제 추천 수량 로직
    //지금은 예측 수요량 = forecast_qty = recommended_order_qty
    //재고/안전재고 반영해야함

    @Transactional
    public List<SmartOrderResponseDto> generateSmartOrders(LocalDate targetWeek){
        List<DemandForecast> forecasts = demandForecastRepository.findByTargetWeek(targetWeek);
        if (forecasts.isEmpty()){
            throw new CustomException(AiErrorCode.FORECAST_NOT_FOUND);
        }
        List<SmartOrder> saved = forecasts.stream()
                .map(df -> {
                    var ps = productSupplierRepository.findByProduct(df.getProduct())
                            .orElseThrow(() ->
                                    new CustomException(AiErrorCode.SUPPLIER_NOT_FOUND)); // 이 에러코드는 enum에 하나 추가해줘

                    return SmartOrder.builder()
                            .supplier(ps.getSupplier())
                            .product(df.getProduct())
                            .demandForecast(df)
                            .targetWeek(df.getTargetWeek())
                            .forecastQty(df.getYPred())
                            .recommendedOrderQty(df.getYPred())
                            .smartOrderStatus(OrderStatus.DRAFT_AUTO)
                            .build();
                })
                .map(smartOrderRepository::save)
                .toList();

        return saved.stream().map(this::toResponse).toList();
    }


    public List<SmartOrderResponseDto> getSmartOrders(OrderStatus status) {
        List<SmartOrder> list;

        if (status != null) {
            list = smartOrderRepository.findBySmartOrderStatus(status);
        } else {
            list = smartOrderRepository.findAllByOrderByTargetWeekDesc();
        }

        return list.stream().map(this::toResponse).toList();
    }



    public SmartOrderResponseDto getSmartOrder(Long id) {
        SmartOrder entity = smartOrderRepository.findById(id)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));
        return toResponse(entity);
    }

    @Transactional
    public SmartOrderResponseDto updateDraft(Long smartOrderId, SmartOrderUpdateRequest request) {
        SmartOrder entity = smartOrderRepository.findById(smartOrderId)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));

        entity.updateDraft(request.getRecommendedOrderQty());

        return toResponse(entity);
    }

    @Transactional
    public SmartOrderResponseDto submit(Long smartOrderId) {
        SmartOrder entity = smartOrderRepository.findById(smartOrderId)
                .orElseThrow(() -> new CustomException(AiErrorCode.SMART_ORDER_NOT_FOUND));
        entity.submit();

        return toResponse(entity);
    }


    private SmartOrderResponseDto toResponse(SmartOrder so) {
        return SmartOrderResponseDto.builder()
                .id(so.getSmartOrderId())
                .productId(so.getProduct().getProductId())
                .supplierId(so.getSupplier().getSupplierId())
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
