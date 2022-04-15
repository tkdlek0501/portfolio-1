package com.productservice.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.productservice.demo.controller.form.CreateMemberForm;
import com.productservice.demo.domain.Address;
import com.productservice.demo.domain.Grade;
import com.productservice.demo.domain.Member;
import com.productservice.demo.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
@Slf4j
public class MemberController {
	
	private final MemberService memberService;
	
	// 회원 목록
	@GetMapping("")
	public String members(Model model) {
		List<Member> members = memberService.findMembers();
		
		model.addAttribute("members", members);
		return "/member/members";
	}
	
	// 회원 등록 폼
	@GetMapping("/new")
	public String createMemberForm(Model model) {
		model.addAttribute("form", new CreateMemberForm());
		return "/member/newMember";
	}
	
	// 회원 등록  // TODO: ※ModelAttribute 쓸 때 set까지 자동으로 같이 해주는 것이므로 setter 가 필요하다
	@PostMapping("")
	public String createMember(
			@Validated @ModelAttribute("form") CreateMemberForm form, 
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes
			) {
		
		// 에러 처리
		if(bindingResult.hasErrors()) {
			log.info("회원 등록 잘못된 값 바인딩 error={}", bindingResult);
			return "/member/newMember"; // TODO: ※회원 등록 폼으로 redirect 사용 하면 th:errors 이용 안됨
			//return "redirect:/members/new";
		}
		
		// converting
		int zipcode = Integer.valueOf(form.getZipcode());
		Grade grade = Grade.valueOf(form.getGrade());
		
		Address address = Address.createAddress(form.getCity(), form.getStreet(), zipcode);
		Member member = Member.createMember(form.getUsername(), form.getPassword(), form.getName(), form.getAge(), grade, address);
		Long id = memberService.join(member);
		
		// 회원 가입 실패시
		if(id == null) {
			log.info("회원 가입 실패");
			bindingResult.reject("duplicationError", null, "이미 사용하고 있는 아이디 입니다. 다른 아이디를 사용해주세요.");
			return "/member/newMember";
		}
		
		// redirect 설정
		redirectAttributes.addAttribute("memberId", id);
		//redirectAttributes.addAttribute("status", true); ?뒤 쿼리스트링
		model.addAttribute("form", form);
		return "redirect:/members/{memberId}"; // 성공시 회원 목록으로
	}
	
	// 회원 조회 (수정 폼)
	@GetMapping("/{memberId}")
	public String member(Model model, @PathVariable("memberId") Long memberId) {
		
		Member member = memberService.findMember(memberId);
		log.info("조회한 회원 : {}", member);
		
		model.addAttribute("member", member);
		return "/member/member";
	}
	
	// 회원 수정
	// "/{id}"
	
	// 회원 삭제
	// "/{id}/remove"
}
