package com.productservice.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.domain.Product;
import com.productservice.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	
	// 상품 생성
	@Transactional
	public Long create(Product product) {
		
		if(product.getId() != null) log.info("상품 생성으로 들어온 product : {}", product.getId());
		
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
