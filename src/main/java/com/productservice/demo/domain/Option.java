package com.productservice.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "options")
@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option {
	
	@Id @GeneratedValue
	@Column(name = "option_id")
	private Long id;
	
	private String names;
	
	private int stockQuantity;
	
	// 연관 관계 매핑
	@OneToMany(mappedBy = "option")
	private List<OrderProduct> orderProduct = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_option_id")
	private ProductOption productOption;

}
