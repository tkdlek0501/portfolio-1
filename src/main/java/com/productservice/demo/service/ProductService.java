package com.productservice.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.productservice.demo.controller.form.CreateOptionForm;
import com.productservice.demo.controller.form.CreateProductForm;
import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.domain.Option;
import com.productservice.demo.domain.Product;
import com.productservice.demo.domain.ProductImage;
import com.productservice.demo.domain.ProductOption;
import com.productservice.demo.dto.UploadFile;
import com.productservice.demo.repository.OptionRepository;
import com.productservice.demo.repository.ProductImageRepository;
import com.productservice.demo.repository.ProductRepository;
import com.productservice.demo.repository.spec.CategoryRepository;
import com.productservice.demo.util.upload.FileStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	private final FileStore fileStore;
	private final ProductImageRepository productImageRepository;
	private final OptionRepository optionRepository;
	
	// 상품 생성
	@Transactional
	public Long create(CreateProductForm form) throws IllegalStateException, IOException {
		
		// productOption 생성
		ProductOption productOption = ProductOption.createProductOption(form.getOptionItems(), null);
		
		// 카테고리 엔티티 조회
		Category category = categoryRepository.findOne(form.getCategoryId());
		
		// 생성 메서드
		Product product = Product.createProduct(form.getName(), form.getPrice(), productOption, category);
		
		// 상품 저장
		productRepository.save(product);
		
		// 옵션 저장
		List<Option> options = controlOption(form.getOption());
		for(int i = 0; i < options.size(); i++) {
			Option option = Option.addOption(options.get(i).getNames(), options.get(i).getStockQuantity(), product.getProductOption());
			optionRepository.save(option);
		}
		
		// 이미지 생성
		if(!form.getImage().get(0).isEmpty()) {
			List<ProductImage> productImages = null;
			if(form.getImage() != null) {
				productImages = controlImage(form.getImage());
			}
			
			for(int i = 0;i<form.getImage().size();i++) {
				ProductImage productImage = ProductImage.addProductImage(productImages.get(i).getOriginalName(), productImages.get(i).getStoreName(), product);
				productImageRepository.save(productImage);
			}
		}
		
		return product.getId();
	}

	// 상품 목록
	public List<Product> findProducts(){
		return productRepository.findAll();
	}
	
	// 상품 조회
	public Product findProduct(Long productId) {
		return productRepository.findOne(productId);
	}
	
	// 상품 수정
	@Transactional
	public Long modifyProduct(UpdateProductForm form) throws IllegalStateException, IOException {
		
		// option 생성
		List<Option> options = controlOption(form.getOption());
		
		// productOption 생성
		ProductOption productOption = ProductOption.createProductOption(form.getOptionItems(), options);
		
		// 카테고리 엔티티 조회
		Category category = categoryRepository.findOne(form.getCategoryId());
		
		// 기존
		Product findProduct = productRepository.findOne(form.getId());
		log.info("기존 product : {}", findProduct);
		
		Product product = Product.createProduct(form.getName(), form.getPrice(), productOption, category);
		
		// 수정
		findProduct.modify(product);
		
		// 이미지 추가 등록 
//		int orgImageSize = findProduct.getProductImage().size();
//		int newImageSize = productImages.size();
//		if(newImageSize > orgImageSize) {
//			for(int i = findProduct.getProductImage().size();i<form.getImage().size();i++) {
//				ProductImage productImage = ProductImage.addProductImage(productImages.get(i).getOriginalName(), productImages.get(i).getStoreName(), findProduct);
//				productImageRepository.save(productImage);
//			}
//		}
		// 이미지 처리
		// 삭제
		if(form.getProductImage().size() > 0) {
			for(int i = 0; i < form.getProductImage().size();i++) {
				ProductImage productImage = productImageRepository.findOne(form.getProductImage().get(i).getId());
				productImageRepository.deleteOne(productImage);
			}
		}
		
		// 추가
		if(!form.getImage().get(0).isEmpty()) {
			List<ProductImage> productImages = null;
			if(form.getImage() != null) {
				productImages = controlImage(form.getImage());
			}
			
			for(int i = 0;i<form.getImage().size();i++) {
				ProductImage productImage = ProductImage.addProductImage(productImages.get(i).getOriginalName(), productImages.get(i).getStoreName(), findProduct);
				productImageRepository.save(productImage);
			}
		}
		
		return findProduct.getId();
	}

	// 상품 삭제
	@Transactional
	public void deleteProduct(Long productId) {
		
		Product findProduct = productRepository.findOne(productId);
		productRepository.deleteOne(findProduct);
	}
	
	
	// 상품 이미지 처리
	private List<ProductImage> controlImage(List<MultipartFile> image) throws IOException {
		List<UploadFile> storeImageFiles = fileStore.storeFiles(image);
		List<ProductImage> productImages = new ArrayList<>();
		for (UploadFile storeImageFile : storeImageFiles) {
			ProductImage productImage = ProductImage.createProductImage(storeImageFile.getUploadFileName(), storeImageFile.getStoreFileName());
			productImages.add(productImage);
		}
		return productImages;
	}
	
	// 옵션 처리
	private List<Option> controlOption(List<CreateOptionForm> formOptions) {
		List<Option> options = new ArrayList<>();
		for(CreateOptionForm formOption : formOptions) {
			if(formOption.getNames() != null && !formOption.getNames().isEmpty() && formOption.getStockQuantity() != null) {
				Option option = Option.createOption(formOption.getNames(), formOption.getStockQuantity());
				options.add(option);
			}
		}
		return options;
	}
}
