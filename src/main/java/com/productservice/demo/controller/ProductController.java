package com.productservice.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.productservice.demo.controller.form.CreateProductForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.domain.Product;
import com.productservice.demo.service.CategoryService;
import com.productservice.demo.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
@Slf4j
public class ProductController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	
	// 상품 목록
	@GetMapping("")
	public String products(Model model) {
		
		List<Product> products = productService.findProducts();
		model.addAttribute("products", products);
		return "product/products";
	}
	
	// 상품 등록 폼
	@GetMapping("/new")
	public String createProductForm(Model model) {
		
		// category 리스트
		List<Category> categories = categoryService.findCategories();
		
		model.addAttribute("categories", categories);
		model.addAttribute("form", new CreateProductForm());
		return "product/newProduct";
	}
	
	// 상품 등록
	@PostMapping("/new")
	public String createProduct(
			@Validated @ModelAttribute("form") CreateProductForm form,
			BindingResult bindingResult,
			Model model
			) throws IllegalStateException, IOException {
		
		log.info("상품 등록 form : {}", form);
		
		// category 리스트
		List<Category> categories = categoryService.findCategories();
		model.addAttribute("categories", categories);
		
		for(MultipartFile image : form.getImage()) {
			if(image.isEmpty()) { 
				bindingResult.rejectValue("image", null, "이미지를 등록해주세요");
				log.info("이미지 등록 안함");
			}
		}
		
		if(bindingResult.hasErrors()) {
			log.info("상품 등록 에러 : {}", bindingResult);
			bindingResult.reject("createProductError", null, "입력받은 값에 오류가 있습니다.");
			return "product/newProduct";
		}
		
		// 상품 등록
		Long id = productService.create(form);
		
		if(id == null) {
			log.info("상품 등록 실패");
			bindingResult.reject("createProductError", null, "상품 등록에 실패했습니다.");
			return "product/newProduct";
		}
	
		return "redirect:/admin/products";
	}
}
