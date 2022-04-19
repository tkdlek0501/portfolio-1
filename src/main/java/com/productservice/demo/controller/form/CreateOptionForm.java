package com.productservice.demo.controller.form;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateOptionForm {
	
	@NotBlank(message = "해당 옵션 이름을 입력해주세요") 
	private String names;
	
	@NotBlank(message = "해당 옵션 재고를 입력해주세요")
	private Integer stockQuantity;
	
}
