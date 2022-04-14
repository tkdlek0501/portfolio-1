package com.productservice.demo.service;

import javax.persistence.EntityManager;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.domain.Address;
import com.productservice.demo.domain.Grade;
import com.productservice.demo.domain.Member;
import com.productservice.demo.repository.MemberRepository;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class MemberServiceTest {
	
	@Autowired MemberService memberService;
	@Autowired MemberRepository memberRepository;
	@Autowired EntityManager em;
	
	// 회원 가입
	@Test
	public void join() throws Exception{
		// given
		Address address = Address.createAddress("경기", "남로", 12345);
		Member member = Member.createMember("HJ", "김현준", "1234", 29, Grade.ADMIN, address);
		
		// when
		Long savedId = memberService.join(member);
		
		// then
		//em.flush(); // 쿼리 동작 강제
		assertEquals(member, memberRepository.findOne(savedId));
	}
	
	
	// 중복 검증
	
	// 회원 수정
	
	// 회원 삭제
	
}
