package com.productservice.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery {
	
	@Id @GeneratedValue
	@Column(name = "delivery_id")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	private DeliveryStatus status;
	
	private int zipcode;
	
	private String city;
	
	private String street;
	
	// 연관 관계 매핑
	@OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Order order;
}
