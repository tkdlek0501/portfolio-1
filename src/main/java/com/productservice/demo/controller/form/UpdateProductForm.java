package com.productservice.demo.controller.form;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.productservice.demo.domain.Option;
import com.productservice.demo.domain.ProductImage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// 상품 수정 폼 DTO

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductForm {
	
	@NotNull
	private Long id;
	
	// 상품
	@NotBlank(message = "이름을 입력해주세요")
	private String name;
	
	@NotNull(message = "가격을 입력해주세요")
	private Integer price;
	
	@NotBlank(message = "노출 여부를 선택해주세요")
	private String status; // ProductStatus (SHOW, HIDE)
	
	//@NotBlank(message = "파일을 등록해주세요")
	private List<MultipartFile> image = new ArrayList<>(); // 받아오는 이미지
	
	private List<ProductImage> productImage = new ArrayList<>();// 이미 등록된 이미지
	
	// productOption
	@NotBlank(message = "옵션항목을 입력해주세요")
	private String optionItems;
	
	// option
	private List<CreateOptionForm> option = new ArrayList<>();
	
	// category
	@NotNull(message = "카테고리를 등록해주세요")
	private Long categoryId;

	// 생성 메서드
	// 조회시
	public static UpdateProductForm createUpdateProductForm(
			Long id,
			String name,
			Integer price,
			String status,
			List<ProductImage> productImage,
			String optionItems,
			List<CreateOptionForm> option,
			Long categoryId
			) {
		UpdateProductForm form = new UpdateProductForm();
		
		form.setId(id);
		form.setName(name);
		form.setPrice(price);
		form.setStatus(status);
		form.setProductImage(productImage);
		form.setOptionItems(optionItems);
		form.setOption(option);
		form.setCategoryId(categoryId);
		
		return form;
	}
	
	// 수정시 생성자
//	public static UpdateProductForm updateproductForm(
//		String name,
//		Integer price,
//		String status,
//		List<MultipartFile> image,
//		String optionItems,
//		List<CreateOptionForm> option,
//		Long categoryId
//			) {
//		UpdateProductForm form = new UpdateProductForm();
//		
//		form.setName(name);
//		form.setPrice(price);
//		form.setStatus(status);
//		form.setImage(image);
//		form.setOptionItems(optionItems);
//		form.setOption(option);
//		form.setCategoryId(categoryId);
//		
//		return form;
//	}


	
	
	
	
}
