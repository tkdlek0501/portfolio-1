package com.productservice.demo.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter @Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Product {
	
	@Id @GeneratedValue
	@Column(name = "product_id")
	private Long id;
	
	private String name;
	
	private int price;
	
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	private LocalDateTime registeredDate;
	
	// 연관 관계 매핑
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL) // productOption은 product와 등록 삭제 등 라이프 사이클 동일
	private ProductOption productOption;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE) // product 삭제시에는 하위 모두 삭제 되게
	private List<ProductImage> productImage = new ArrayList<>();
	
	// === 연관 관계 메서드
	
	public void setProductOption(ProductOption productOption) {
		this.productOption = productOption;
		productOption.setProduct(this);
	}
	
	public void setCategory(Category category) {
		this.category = category;
		category.getProduct().add(this);
	}
	
	public void addProductImage(ProductImage productImage) {
		this.productImage.add(productImage);
		productImage.setProduct(this);
	}
	
	// === 생성 메서드
	
	public static Product createProduct(
			String name,
			int price,
//			List<ProductImage> productImages,
			ProductOption productOption,
			Category category
			) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStatus(ProductStatus.SHOW);
		product.setRegisteredDate(LocalDateTime.now());
		
//		if(productImages != null) {
//			for(ProductImage productImage : productImages) {
//				product.addProductImage(productImage);
//			}
//		}
		
		product.setProductOption(productOption);
		product.setCategory(category);
		
		return product;
	}
	
	// 수정
	public void modify(Product product) {
		// 상품
		if(product.getName() != null && !product.getName().isEmpty()) this.setName(product.getName());
		this.setPrice(product.getPrice());
		if(product.getStatus() != null) this.setStatus(status);
		
		// 상품 옵션
//		if(product.getProductOption() != null) this.setProductOption(product.getProductOption()); -> 이러면 수정 대신 row가 추가된다
		this.getProductOption().setOptionItems(product.getProductOption().getOptionItems());
		
		// 옵션
		for(int i = 0; i < this.getProductOption().getOption().size(); i++) {
			this.getProductOption().getOption().get(i).setNames(product.getProductOption().getOption().get(i).getNames());
			this.getProductOption().getOption().get(i).setStockQuantity(product.getProductOption().getOption().get(i).getStockQuantity());
		}
		
		// 카테고리 - 조회해서 가져온 엔티티 넣어줌
		this.setCategory(product.getCategory());
		
		// 이미지
//		int updateImageSize = product.getProductImage().size();
//		int orgImageSize = this.getProductImage().size();
//		if(updateImageSize > orgImageSize) { 
//			for(int i = 0; i < orgImageSize;i++) {
//				this.getProductImage().get(i).setOriginalName(product.getProductImage().get(i).getOriginalName());
//				this.getProductImage().get(i).setStoreName(product.getProductImage().get(i).getStoreName());
//			}
//		}else { // 이 부분은 괜찮은데
//			for(int i = 0; i < updateImageSize; i++) {
//				this.getProductImage().get(i).setOriginalName(product.getProductImage().get(i).getOriginalName());
//				this.getProductImage().get(i).setStoreName(product.getProductImage().get(i).getStoreName());
//			}
//		}
	}
}
