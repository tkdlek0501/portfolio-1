package com.productservice.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.productservice.demo.domain.Member;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
	
	private final EntityManager em;
	
	// 회원 저장
	public void save(Member member) {
		em.persist(member);
	}
	
	// 회원 조회
	public Member findOne(Long memberId) {
		return em.find(Member.class, memberId);
	}
	
	// 회원 목록
	public List<Member> findAll(){
		return em.createQuery("select m from member m", Member.class)
				.getResultList();
	}
	
	// TODO: 수정은 transaction 변경 감지로 쿼리 작동
	
	// 이름으로 회원 조회
	public List<Member> findByName(String name){
		return em.createQuery("select m form member m where m.name = :name", Member.class)
				.setParameter("name", name)
				.getResultList();
	}
}
