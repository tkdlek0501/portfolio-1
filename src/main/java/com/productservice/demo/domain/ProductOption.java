package com.productservice.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class ProductOption {
	
	@Id @GeneratedValue
	@Column(name = "product_option_id")
	private Long id;
	
	private String optionItems; // 옵션 상품 이름들
	
	// 연관 관계 매핑
	
	@OneToMany(mappedBy = "productOption", cascade = CascadeType.ALL)
	private List<Option> option = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	
	// === 연관 관계 메서드
	
	public void addOption(Option option) {
		this.option.add(option);
		option.setProductOption(this);
	}
	
	public void setProduct(Product product) {
		this.product = product;
		product.setProductOption(this);
	}
	
	// === 생성 메서드
	
	public static ProductOption createProductOption(
			String optionItems,
			Product product,
			Option... options
			) {
		ProductOption productOption = new ProductOption();
		productOption.setOptionItems(optionItems);
		productOption.setProduct(product);
		for(Option option : options) {
			productOption.addOption(option);
		}
		
		return productOption;
	}
}
