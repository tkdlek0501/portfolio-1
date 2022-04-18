package com.productservice.demo.controller.form;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// 상품 수정 폼 DTO

@Getter
@Setter
@ToString
public class UpdateProductForm {
	
	@NotNull
	private Long id;
	
	@NotBlank(message = "이름을 입력해주세요")
	private String name;
	
	@NotBlank(message = "가격을 입력해주세요")
	private Integer price;
	
	@NotBlank(message = "노출 여부를 선택해주세요")
	private String status; // ProductStatus (SHOW, HIDE)
	
	@NotBlank(message = "파일을 등록해주세요")
	private List<MultipartFile> image;
	
	// productOption
	@NotBlank(message = "옵션항목을 입력해주세요")
	private String optionItems;
	
	// option
	@NotBlank(message = "옵션명을 입력해주세요")
	private String optionsNames;
	
	// category
	@NotBlank(message = "카테고리를 등록해주세요")
	private Long categoryId;
	
	
}
