package com.productservice.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.controller.form.UpdateMemberForm;
import com.productservice.demo.domain.Member;
import com.productservice.demo.repository.AddressRepository;
import com.productservice.demo.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true) // 보통 조회 메서드가 더 많기 때문에 기본값을 readOnly로 설정
@RequiredArgsConstructor // final 로 선언한 객체들 자동으로 생성자 만들어서 의존관계 주입
@Slf4j
public class MemberService {
	
	private final MemberRepository memberRepository;
	private final AddressRepository addressRepository;
	
	// 회원 가입(생성)
	@Transactional
	public Long join(Member member) {
		
		// 중복 검증
		validateDuplicateMember(member.getUsername());
		
		memberRepository.save(member);
		addressRepository.save(member.getAddress());
		
		return member.getId();
	}
	
	// 중복 검증 (아이디 중복 막음)
	private void validateDuplicateMember(String username) {
		
		log.info("중복 검증 실행 username : {}", username);
		
		Optional<Member> findMember = memberRepository.findOneByUsername(username); // TODO: 멀티 쓰레드 환경이므로 DB에서도 중복을 막아야 한다.(유니크하게 컬럼 관리) 
		
		if(findMember.isPresent()) { // 값이 존재 하는지 boolean
			throw new IllegalStateException("이미 사용하고 있는 아이디입니다.");
		}
	}
	
	// 회원 목록
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	// 회원 조회
	public Member findMember(Long memberId) {
		return memberRepository.findOne(memberId);
	}
	
	// 회원 수정
	@Transactional
	public Long modifyMember(UpdateMemberForm form) {
		
		// 받아온 username 있으면 
		if(form.getUsername() != null && !form.getUsername().isEmpty()) {
			// member 찾기
			Member member = memberRepository.findOne(form.getId());
			
			// 자신의 기존 아이디와 다르면 검증
			if(!member.getUsername().equals(form.getUsername())) {
				log.info("기존 아이디와 달라 검증 중입니다.");
				validateDuplicateMember(form.getUsername());
			}
		}	
		
		// 기존 멤버 (이 객체의 필드가 변경되면 감지한다)
		Member findMember = memberRepository.findOne(form.getId()); // 영속성 컨텍스트 등록
		
		// 수정 (변경 감지)
		findMember.modify(form); // TODO: setter 사용하지 않고 메서드로 변경하기!
//		findMember.setUsername(form.getUsername());
//		findMember.setName(form.getName());
//		findMember.setPassword(form.getPassword());
		
		return findMember.getId();
	}
	
	// 회원 삭제
	@Transactional
	public void deleteMember(Long memberId) {
		
		Member findMember = memberRepository.findOne(memberId);
		
		memberRepository.deleteOne(findMember);
	}
}
