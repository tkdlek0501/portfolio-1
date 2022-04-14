package com.productservice.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.controller.form.UpdateMemberForm;
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
	
	Address address = Address.createAddress("경기", "남로", 12345);
	Address address2 = Address.createAddress("경기", "남로", 67890);
	
	// 회원 가입
	@Test
	public void join() throws Exception{
		// given
		Member member = Member.createMember("HJ", "김현준", "1234", 29, Grade.ADMIN, address);
		
		// when
		Long savedId = memberService.join(member);
		
		// then
		// em.flush(); // 쿼리 동작 강제
		assertEquals(member, memberRepository.findOne(savedId));
	}
	
	// 중복 검증
	@Test(expected = IllegalStateException.class) // 기대값 설정
	public void validation() throws Exception {
		// given
		Member member1 = Member.createMember("HJ", "김현준", "1234", 29, Grade.ADMIN, address);
		Member member2 = Member.createMember("HJ", "김현준", "1234", 29, Grade.ADMIN, address2); 
		// TODO: ※같은 address 인스턴스를 쓰면 안된다 - 인스턴스가 같으면 같은 엔티티로 판단함
		
		// when
		memberService.join(member1);
		memberService.join(member2); // 여기서 예외가 발생해야 한다
		
		// then
		fail("중복 검증 테스트 실패(예외가 발생해야 합니다.)");
	}
	
	// 회원 수정
	@Test
	//@Rollback(false)
	public void updateMember() throws Exception{
		// given
			// 등록 
			Member member = Member.createMember("HJ", "김현준", "1234", 29, Grade.ADMIN, address);
			
			log.info("등록할 member: {}", member);
			
			Long memberId = memberService.join(member);
			
			// 등록한  member 찾아오기 (영속성 컨텍스트 등록)
			Member findMember = memberRepository.findOne(memberId);
			
			// 변경할 data
			UpdateMemberForm form = UpdateMemberForm.createMemberForm(findMember.getId(), "KHJ", "12345", "현준김", 0, null);
			
			// member 기대값 (비교를 위해 생성)
			Member expectedMember = Member.updateMember(findMember.getId(), "KHJ", "12345", "현준김", 29, findMember.getAddress());
		
		// when
			memberService.modifyMember(form);
		
		// then
		//em.flush(); // 쿼리 동작 강제
		Member resultMember = memberRepository.findOne(findMember.getId());
		log.info("result : {}", resultMember);
		assertEquals(expectedMember.getUsername(), resultMember.getUsername()); // TODO: expected KHJ but HJ -> findOne이 제대로 안된다 
		assertEquals(expectedMember.getPassword(), resultMember.getPassword());
		assertEquals(expectedMember.getName(), resultMember.getName());
		assertEquals(expectedMember.getAge(), resultMember.getAge()); // set 안 해준 것은 그대로 유지
		
	}
	
	// 회원 삭제
}

// 테스트시 주의점
// DB를 사용하는 경우 properties에 ddl 설정에 따라 exception 발생하는 경우 생김
// save the transient instance before flushing 에러
// Member 저장시에 address 도 같이 저장해야 되는데 객체가 아직 DB에 저장되지 않았기 때문에 발생(address FK가 없기 때문)
// 1. cascade 양쪽에 다 해야 된다  TODO: 왜 cascade를 양쪽에 다 해야 하는지? 이러면 순환참조 문제 발생하는 것 같음
// 2. 또는 persist 를 따로 (Address를 FK가 되는 id를 가진 상태로 넣어주면)
