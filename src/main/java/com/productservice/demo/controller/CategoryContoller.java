package com.productservice.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.productservice.demo.controller.form.CreateCategoryForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class CategoryContoller {
	
	private final CategoryService categoryService;
	
	// 카테고리 목록
	@GetMapping("")
	public String categories(Model model) {
		
		List<Category> category = categoryService.findCategories();
		
		model.addAttribute("category", category);
		return "/category/categories"; 
	}
	
	// 카테고리 등록 폼
	@GetMapping("/new")
	public String createCategoryForm(Model model) {
		model.addAttribute("category", new CreateCategoryForm());
		return "/category/newCategory";
	}
	
	// 카테고리 등록
	
	// 카테고리 조회(수정 폼)
	
	// 카테고리 수정
	
	// 카테고리 삭제
	
}
