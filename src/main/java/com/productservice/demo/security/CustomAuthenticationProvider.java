package com.productservice.demo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.productservice.demo.domain.Member;
import com.productservice.demo.service.security.UserService;

import lombok.RequiredArgsConstructor;

// 사용자 인증 체크

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	private final PasswordEncoder passwordEncoder;
	private final UserService userService;
	
	// 사용자 인증 요청시
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		// login id로 member 찾기
		Member member = (Member) userService.loadUserByUsername(authentication.getName());
		
		String password = authentication.getCredentials().toString();
		
		// 비밀번호 일치하는지 검증
		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new BadCredentialsException("아이디 또는 비밀번호가 틀렸습니다."); // failure로 throw
		}
		
		// 인증 성공시 세션(Authentication) 발급; user 정보, password?, 권한
		return new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
	}

	
	
	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}
