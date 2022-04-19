package com.productservice.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.controller.form.CreateOptionForm;
import com.productservice.demo.controller.form.CreateProductForm;
import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.domain.Option;
import com.productservice.demo.domain.Product;
import com.productservice.demo.domain.ProductImage;
import com.productservice.demo.domain.ProductOption;
import com.productservice.demo.dto.UploadFile;
import com.productservice.demo.repository.ProductRepository;
import com.productservice.demo.repository.core.CategoryRepository;
import com.productservice.demo.util.upload.FileStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final FileStore fileStore;
	
	// 상품 생성
	@Transactional
	public Long create(CreateProductForm form) throws IllegalStateException, IOException {
		
		// 이미지 처리 
		List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImage());
		List<ProductImage> productImages = new ArrayList<>();
		for (UploadFile storeImageFile : storeImageFiles) {
			ProductImage productImage = ProductImage.createProductImage(storeImageFile.getUploadFileName(), storeImageFile.getStoreFileName());
			productImages.add(productImage);
		}
		
		// option 생성
		List<Option> options = new ArrayList<>();
		for(CreateOptionForm formOption : form.getOption()) {
			Option option = Option.createOption(formOption.getNames(), formOption.getStockQuantity());
			options.add(option);
		}
		
		// productOption 생성
		ProductOption productOption = ProductOption.createProductOption(form.getOptionItems(), options);
		
		// 카테고리 엔티티 조회
		Category category = categoryRepository.findOne(form.getCategoryId());
		
		// 생성 메서드
		Product product = Product.createProduct(form.getName(), form.getPrice(), productImages, productOption, category);
		
		productRepository.save(product);
		
		return product.getId();
	}
	
	// 상품 목록
	public List<Product> findProducts(){
		return productRepository.findAll();
	}
	
	// 상품 조회
	public Product findProduct(Long productId) {
		return productRepository.findOne(productId);
	}
	
	// 상품 수정
	@Transactional
	public Long modifyProduct(UpdateProductForm form) {
		
		// 기존
		Product findProduct = productRepository.findOne(form.getId());
		
		// 수정
		findProduct.modify(form);
		
		return findProduct.getId();
	}
	
	 // 상품 삭제
	@Transactional
	public void deleteProduct(Long productId) {
		
		Product findProduct = productRepository.findOne(productId);
		
		productRepository.deleteOne(findProduct);
	}
}
