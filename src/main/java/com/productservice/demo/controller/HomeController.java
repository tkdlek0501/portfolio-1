package com.productservice.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	// admin index 페이지
	@GetMapping("/admin")
	public String adminHome() {
		return "/index";
	}
	
	// user index 페이지
	@GetMapping("/user")
	public String userHome() {
		return "/index";
	}
	
	// login 페이지
	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	// 일반 회원가입 페이지
	
	
	
	
}
