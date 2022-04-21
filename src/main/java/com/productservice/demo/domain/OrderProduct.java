package com.productservice.demo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct {
	
	@Id @GeneratedValue
	@Column(name = "order_product_id")
	private Long id;
	
	private int orderPrice;
	
	private int count;
	
	private int orderTotalPrice;
	
	// 연관 관계 매핑
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id")
	private Option option;
	
	// 생성 메서드 (주문시)
	public static OrderProduct createOrderProduct(
			Option option, 
			int orderPrice, 
			int count,
			Order order
			) {
		OrderProduct orderProduct = new OrderProduct();
		orderProduct.setOption(option);
		orderProduct.setOrderPrice(orderPrice);
		orderProduct.setCount(count);
		orderProduct.setOrderTotalPrice(orderPrice * count);
		orderProduct.setOrder(order);
		
		option.removeStock(count); // 재고 감소
		return orderProduct;
	}
	
	
	// 주문 수량 원복
	public void cancel() {
		getOption().addStock(count);
	}
}
