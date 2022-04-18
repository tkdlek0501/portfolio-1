package com.productservice.demo.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.domain.Option;
import com.productservice.demo.domain.Product;
import com.productservice.demo.domain.ProductOption;
import com.productservice.demo.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class ProductServiceTest {
	
	@Autowired ProductService productService;
	@Autowired ProductRepository productRepository;
	@Autowired EntityManager em;
	
	Option option = Option.createOption("옵션1", 100); // 옵션명과 재고 수량
	ArrayList<Option> options = new ArrayList<Option> ();
	
	ProductOption productOption = ProductOption.createProductOption("옵션항목1", option);
	Category category = Category.createCategory("카테고리1");
	
	// 등록
	@Test
	public void create() throws Exception{
		// given
		Product product = Product.createProduct("상품1", 10000, "image", productOption, category);
		
		// when
		Long savedId = productService.create(product);
		
		// then
		assertEquals(product, productRepository.findOne(savedId));
	}
	
	// 수정
	@Test
	public void update() throws Exception{
		// given
		// 등록
		Product product = Product.createProduct("상품1", 10000, "image", productOption, category);
		Long productId = productService.create(product);
		
		// 수정할 값
		UpdateProductForm form = new UpdateProductForm();
		form.setId(productId);
		form.setCategoryId(category.getId());
		form.setImage(null);
		form.setName("상품2");
		form.setOptionItems("옵션1");
		form.setPrice(20000);
		form.setStatus("HIDE");
		
		// when
		// 수정
		productService.modifyProduct(form);
		
		// then
		// 결과
		Product resultPrd = productRepository.findOne(productId);
		assertEquals(resultPrd.getName(), "상품2");
	}
	
	// 삭제
	@Test
	public void delete() throws Exception{
		// given
		// 등록
		Product product = Product.createProduct("상품1", 10000, "image", productOption, category);
		Long productId = productService.create(product);
		
		// when
		productService.deleteProduct(productId);
		
		// then
		Product resultPrd = productRepository.findOne(productId);
		assertEquals(resultPrd, null);
	}
	
}
