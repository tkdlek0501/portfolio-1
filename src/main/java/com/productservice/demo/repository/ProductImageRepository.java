package com.productservice.demo.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.productservice.demo.domain.ProductImage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductImageRepository {
	
	private final EntityManager em;
	
	// 이미지 저장
	public void save(ProductImage productImage) {
		log.info("이미지 저장 : {}", productImage);
		em.persist(productImage);
	}
	
	// 이미지 삭제
	public void deleteOne(ProductImage productImage) {
		em.remove(productImage);
	}
	
}