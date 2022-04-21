package com.productservice.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 주문 리스트에 쓸 DTO

@Getter
@Setter
@ToString
public class OrderListDto {
	
	// order
	private int orderId;
	private int memberId;
	private int deliveryId;
	private LocalDateTime orderDate;
	
	// delivery
}
