package com.productservice.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.productservice.demo.domain.Product;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
	
	private final EntityManager em;
	
	// 상품 저장
	public void save(Product product) {
		em.persist(product);
	}
	
	// 상품 조회
	public Product findOne(Long productId) {
		return em.find(Product.class, productId);
	}
	
	// 상품 목록
	public List<Product> findAll() {
		return em.createQuery("select p from Product p", Product.class)
				.getResultList();
	}
	
	// TODO: 상품 목록 (검색 포함)
	
	// 상품 삭제
	public void deleteOne(Product product) {
		em.remove(product);
	}
}
