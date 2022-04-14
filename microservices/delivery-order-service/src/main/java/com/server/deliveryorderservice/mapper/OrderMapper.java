package com.server.deliveryorderservice.mapper;

import com.server.deliveryorderservice.model.dto.request.OrderRequest;
import com.server.deliveryorderservice.model.dto.response.OrderResponse;
import com.server.deliveryorderservice.model.entity.Order;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderMapper {

    public static OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .closedAt(order.getClosedAt())
                .restaurant(order.getRestaurant())
                .delivery(order.getDelivery())
                .items(order.getItems())
                .lastStatus(order.getLastStatus())
                .reviewed(order.getReviewed())
                .subTotal(order.getSubtotal())
                .subTotalWithDiscount(order.getSubtotalWithDiscount())
                .totalValue(order.getTotalValue())
                .totalValueWithDiscount(order.getTotalValueWithDiscount())
                .build();
    }

    public static Order mapToModel(OrderRequest order) {
        return Order.builder()
                .customer(CustomerMapper.mapToModel(order.getCustomer()))
                .delivery(DeliveryMapper.mapToModel(order.getDelivery()))
                .restaurant(RestaurantMapper.mapToModel(order.getRestaurant()))
                .items(ItemMapper.mapToModel(order.getItems()))
                .subtotal(order.getSubTotal())
                .subtotalWithDiscount(order.getSubTotalWithDiscount())
                .totalValue(order.getTotalValue())
                .totalValueWithDiscount(order.getTotalValueWithDiscount())
                .build();
    }
}
