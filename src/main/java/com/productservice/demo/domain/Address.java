package com.productservice.demo.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
	
	@Id @GeneratedValue
	@Column(name = "address_id")
	private Long id;
	
	private String city;
	
	private String street;
	
	private String zipcode;
	
	// 연관관계 매핑
	
	@OneToOne(mappedBy="address", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Member member;
}
