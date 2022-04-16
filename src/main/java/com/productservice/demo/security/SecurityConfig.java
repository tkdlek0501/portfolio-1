package com.productservice.demo.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import com.productservice.demo.service.MemberService;

import lombok.RequiredArgsConstructor;

@Configuration 
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private final CustomAuthenticationProvider provider;
	private final CustomAuthenticationSuccessHandler successHandler;
	private final CustomAuthenticationFailureHandler failureHandler;
	
	private final MemberService memberService;
	
	// passwordEncoder bean 등록
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// thymeleaf에서 security 사용
	@Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// 페이지 접근
		http
			.authorizeRequests()
		        .antMatchers("/login/**").anonymous()
		        .antMatchers("/admin/**").hasRole("ADMIN")
				.anyRequest().permitAll();
		
		// 로그인 
		http
			.formLogin()
		        .loginPage("/login")
		        .loginProcessingUrl("/doLogin")
		        .successHandler((AuthenticationSuccessHandler) successHandler) 
				.failureHandler((AuthenticationFailureHandler) failureHandler);
		
		// 로그아웃
		http
			.logout()
				.logoutUrl("/doLogout")
		        .logoutSuccessUrl("/login");
		
	}
	
	// 사용자 인증 담당 설정
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
		auth.authenticationProvider((AuthenticationProvider) provider);
	}
	
	// ignore 설정
	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().antMatchers("/static/**");
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	
}
