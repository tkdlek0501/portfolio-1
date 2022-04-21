package com.productservice.demo.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.productservice.demo.domain.OrderProduct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderProductRepository {
	
	private final EntityManager em;
	
	// 저장
	public void save (OrderProduct orderProduct) {
		em.persist(orderProduct);
	} 
	
}
