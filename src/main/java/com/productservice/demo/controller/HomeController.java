package com.productservice.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
	
	// index 페이지
	@GetMapping("")
	public String home() {
		return "/index";
	}
}
