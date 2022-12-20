package com.example.orderservice.util;

import com.example.orderservice.dto.OrderStatus;
import com.example.orderservice.dto.PurchaseOrderResponseDto;
import com.example.orderservice.dto.RequestContext;
import com.example.orderservice.dto.TransactionStatus;
import com.example.orderservice.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {
	public static PurchaseOrder getPurchaseOrder(RequestContext requestContext) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		purchaseOrder.setUserId(requestContext.getPurchaseOrderRequestDto().getUserId());
		purchaseOrder.setProductId(requestContext.getPurchaseOrderRequestDto().getProductId());
		purchaseOrder.setAmount(requestContext.getProductDto().getPrice());

		TransactionStatus transactionStatus = requestContext.getTransactionResponseDto().getTransactionStatus();
		OrderStatus orderStatus = TransactionStatus.APPROVED.equals(transactionStatus) ? OrderStatus.COMPLETED : OrderStatus.FAILED;
		purchaseOrder.setOrderStatus(orderStatus);
		return purchaseOrder;
	}

	public static PurchaseOrderResponseDto getPurchaseOrderResponseDto(PurchaseOrder purchaseOrder) {
		PurchaseOrderResponseDto purchaseOrderResponseDto = new PurchaseOrderResponseDto();
		purchaseOrderResponseDto.setOrderId(purchaseOrder.getOrderId());
		BeanUtils.copyProperties(purchaseOrder, purchaseOrderResponseDto);
		return purchaseOrderResponseDto;
	}
}
