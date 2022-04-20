package com.productservice.demo.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.productservice.demo.domain.Option;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OptionRepository {
	
	private final EntityManager em;
	
	// 옵션 저장
	public void save(Option option) {
		em.persist(option);
	}
	
	// 옵션 삭제
	public void deleteOne(Option option) {
		em.remove(option);
	}
	
	// 옵션 조회
	public Option findOne(Long id) {
		return em.find(Option.class, id);
	}
	
}
