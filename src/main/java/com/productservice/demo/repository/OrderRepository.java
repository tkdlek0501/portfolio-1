package com.productservice.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.productservice.demo.domain.Order;
import com.productservice.demo.dto.OrderListDto;
import com.productservice.demo.dto.OrderSearch;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	
	private final EntityManager em;
	
	// 주문 저장
	public void save(Order order) {
		em.persist(order);
	}
	
	// 주문 조회
	public Order findOne(Long orderId) {
		return em.find(Order.class, orderId);
	}
	
	// 주문 목록 (전체)
	public List<Order> findAll(){
		return em.createQuery("select o from Order o", Order.class)
				.getResultList();
	}
	
	// 내 주문 목록
	public List<Order> myAll(Long memberId) {
		return em.createQuery("select o from Order o where o.member.id = :memberId", Order.class)
				.setParameter("memberId", memberId)
				.getResultList();
		
//		String jpql = "select o, opt, p from Order o left join o.member m"
//				+ "left join o.orderProduct op"
//				+ "left join op.option opt"
//				+ "left join opt.productOption po"
//				+ "left join po.product p"
//				+ "where o.member.id = :memberId";	 
//		
//		TypeQuery<OrderListDto> query = em.createQuery(jpql, OrderListDto.class)
//						.setFirstResult(0)
//						.setMaxResults(1000);
//		
//		// 파라미터 세팅
//		query = query.setParameter("memberId", memberId);
//		
//		return query.getResultList();
	}
	
	// TODO: 주문 목록 (검색 포함)
//	public List<Order> findAll(OrderSearch orderSearch){
//		
//		String jpql = "select o, opt, p from Order o left join o.member m"
//				+ "left join o.orderProduct op"
//				+ "left join op.option opt"
//				+ "left join opt.productOption po"
//				+ "left join po.product p";
//		boolean isFirstCondition = true;
//		
//		// 상품 주문 조건들
//		
//		if (orderSearch.getRandom() != null) {
//			if(isFirstCondition ) {
//				jpql += " where";
//				isFirstCondition = false;
//			}else {
//				jpql += " and";
//			}
//			jpql += " o.status = :status";
//		}
//		
//		if(StringUtils.hasText(orderSearch.getRandom())) {
//			if(isFirstCondition ) {
//				jpql += " where";
//				isFirstCondition = false;
//			}else {
//				jpql += " and";
//			}
//			jpql += " o.name like :name";
//		}
//		
//		TypeQuery<OrderListDto> query = em.createQuery(jpql, OrderListDto.class)
//										.setFirstResult(0)
//										.setMaxResults(1000);
//		
//		// 파라미터 세팅
//		if(orderSearch.getRandom() != null) {
//			query = query.setParameter("status", orderSearch.getRandom());
//		}
//		
//		return query.getResultList();
//	}
	
}
