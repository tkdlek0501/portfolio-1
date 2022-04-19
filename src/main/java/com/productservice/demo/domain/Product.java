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

import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.dto.UploadFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
	private ProductOption productOption;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
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
			List<ProductImage> productImages,
			ProductOption productOption,
			Category category
			) {
		Product product = new Product();
		product.setName(name);
		product.setPrice(price);
		product.setStatus(ProductStatus.SHOW);
		product.setRegisteredDate(LocalDateTime.now());
		
		for(ProductImage productImage : productImages) {
			product.addProductImage(productImage);
		}
		product.setProductOption(productOption);
		product.setCategory(category);
		
		return product;
	}
	
	
	// 수정
	public void modify(UpdateProductForm form) {
		
		if(form.getName() != null && !form.getName().isEmpty()) this.setName(form.getName());
		if(form.getPrice() != null) this.setPrice(form.getPrice());
		
		ProductStatus status = ProductStatus.valueOf(form.getStatus());
		if(form.getStatus() != null && form.getStatus().isEmpty()) this.setStatus(status);
		// if(form.getImage() != null) this.setImage(form.getImage()); TODO: 이미지 저장 처리
		if(form.getOptionItems() != null && !form.getOptionItems().isEmpty()) this.getProductOption().setOptionItems(form.getOptionItems());
		if(form.getCategoryId() != null) this.getCategory().setId(form.getCategoryId());
		//if(form.getOptionsNames() != null && !form.getOptionsNames().isEmpty()) this.getProductOption().getOption().get(0).setNames(form.getOptionsNames());
	}
}
