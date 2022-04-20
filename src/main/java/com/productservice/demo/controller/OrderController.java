package com.productservice.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.productservice.demo.domain.Product;
import com.productservice.demo.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {
	
	private final ProductService productService;
	
	// 주문 가능 상품 리스트
	@GetMapping("/products")
	public String order(Model model) {
		List<Product> products = productService.findShowProducts();
		model.addAttribute("products", products);
		return "products";
	}
	
}
