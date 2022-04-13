package com.productservice.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
	
	@Id @GeneratedValue
	@Column(name = "order_id")
	private Long id;
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	// === 연관관계 매핑
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	@OneToMany(mappedBy = "order")
	private List<OrderProduct> orderProduct = new ArrayList<>();
	
	// === 연관 관계 메서드
	
	public void setMember(Member member) {
		this.member = member;
		member.addOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	
	public void addOrderProduct(OrderProduct orderProduct) {
		this.orderProduct.add(orderProduct);
		orderProduct.setOrder(this);
	}
	
	// === 생성 메서드
	
	public static Order createOrder(
		Member member,
		Delivery delivery,
		OrderProduct... orderProducts
			) {
		Order order = new Order();
		order.setOrderDate(LocalDateTime.now());
		order.setStatus(OrderStatus.ORDER);
		order.setMember(member);
		order.setDelivery(delivery);
		for(OrderProduct orderProduct : orderProducts) {
			order.addOrderProduct(orderProduct);
		}
		
		return order;
	}
	
}
