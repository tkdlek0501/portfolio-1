package com.productservice.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import com.productservice.demo.controller.form.CreateOptionForm;
import com.productservice.demo.controller.form.CreateProductForm;
import com.productservice.demo.controller.form.UpdateOptionForm;
import com.productservice.demo.controller.form.UpdateProductForm;
import com.productservice.demo.domain.Category;
import com.productservice.demo.domain.Option;
import com.productservice.demo.domain.Product;
import com.productservice.demo.domain.ProductImage;
import com.productservice.demo.repository.OptionRepository;
import com.productservice.demo.repository.ProductImageRepository;
import com.productservice.demo.service.CategoryService;
import com.productservice.demo.service.OptionService;
import com.productservice.demo.service.ProductService;
import com.productservice.demo.util.upload.FileStore;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/products")
@Slf4j
public class ProductController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final OptionService optionService;
	
	// ?????? ??????
	@GetMapping("")
	public String products(Model model) {
		
		List<Product> products = productService.findProducts();
		model.addAttribute("products", products);
		return "product/products";
	}
	
	// ?????? ?????? ???
	@GetMapping("/new")
	public String createProductForm(Model model) {
		
		// category ?????????
		List<Category> categories = categoryService.findCategories();
		
		model.addAttribute("categories", categories);
		model.addAttribute("form", new CreateProductForm());
		return "product/newProduct";
	}
	
	// ?????? ??????
	@PostMapping("/new")
	public String createProduct(
			@Validated @ModelAttribute("form") CreateProductForm form,
			BindingResult bindingResult,
			Model model
			) throws IllegalStateException, IOException {
		
		log.info("?????? ?????? form : {}", form);
		
		// category ?????????
		List<Category> categories = categoryService.findCategories();
		model.addAttribute("categories", categories);
		
		// ????????? validation
		for(MultipartFile image : form.getImage()) {
			if(image.isEmpty()) { 
				bindingResult.rejectValue("image", null, "???????????? ??????????????????");
				log.info("????????? ?????? ??????");
			}
		}
		
		// option validation
		if(form.getOption().get(0).getNames() == null || 
				form.getOption().get(0).getNames().isEmpty() || 
				form.getOption().get(0).getStockQuantity() == null) {
			bindingResult.rejectValue("option", null, "????????? ????????? ????????? ??????????????????.");
		}
		
		if(bindingResult.hasErrors()) {
			log.info("?????? ?????? ?????? : {}", bindingResult);
			bindingResult.reject("createProductError", null, "???????????? ?????? ????????? ????????????.");
			return "product/newProduct";
		}
		
		// ?????? ??????
		Long id = productService.create(form);
		
		if(id == null) {
			log.info("?????? ?????? ??????");
			bindingResult.reject("createProductError", null, "?????? ????????? ??????????????????.");
			return "product/newProduct";
		}
	
		return "redirect:/admin/products";
	}
	
	// ?????? ?????? (?????? ???)
	@GetMapping("{productId}")
	public String product(Model model, @PathVariable("productId") Long productId) {
		
		Product product = productService.findProduct(productId);
		log.info("????????? ?????? : {}", product);
		
		String status = String.valueOf(product.getStatus());
		
		List<Option> options = product.getProductOption().getOption();
		List<UpdateOptionForm> optionForms = new ArrayList<>();
		for (Option option : options) {
			UpdateOptionForm updateOptionForm = UpdateOptionForm.createOptionForm(option.getId(), option.getNames(), option.getStockQuantity());
			optionForms.add(updateOptionForm);
		}
		
		UpdateProductForm getProduct = UpdateProductForm.createUpdateProductForm(product.getId(), product.getName(), product.getPrice(), status, product.getProductImage(), product.getProductOption().getOptionItems(), optionForms, product.getCategory().getId());
		
		// category ?????????
		List<Category> categories = categoryService.findCategories();
		model.addAttribute("categories", categories);
		
		model.addAttribute("product", getProduct);
		return "product/product";
	}
	
	// ?????? ??????
	@PostMapping("{productId}")
	public String updateProduct(
			@Validated @ModelAttribute("product") UpdateProductForm form,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			Model model
			) throws IllegalStateException, IOException {
		
		// ?????? ??????
		for(int i = 0; i < form.getDeleteOption().size(); i++) {
			if(form.getDeleteOption().get(i) != null) {
				
				form.getOption().remove(i); // ????????? form?????? i?????? ?????? - validation ?????? ????????????
				
				Long pk = Long.valueOf(form.getDeleteOption().get(i));
				log.info("??????????????? ?????? pk : {}", pk);
				optionService.deleteOne(pk); // ?????? ???????????? ??????
			}
		}
		
		// category ?????????
		List<Category> categories = categoryService.findCategories();
		model.addAttribute("categories", categories);
		
		// ??????
		Long id = productService.modifyProduct(form);
		
		// ?????????
		if(id == null) {
			bindingResult.reject("modifyProductFail", null, "?????? ????????? ??????????????????.");
		}
		
		// ????????? validation
		if(form.getProductImage().isEmpty() || form.getProductImage() == null){ // ?????? ????????? ????????? ?????????
			log.info("?????? ????????? ???????????? ????????? validation ???");
			for(MultipartFile image : form.getImage()) { // ????????? ????????? ?????????
				if(image.isEmpty()) { 
					bindingResult.rejectValue("image", null, "???????????? ??????????????????");
					break;
				}
			}
		}
		
		// option validation
		if(form.getOption().get(0).getNames().isEmpty() || form.getOption().get(0).getStockQuantity() == null) {
			log.info("?????? ?????? ????????? ??????");
			bindingResult.rejectValue("option", null, "????????? ??????????????????.");
		}
		
		if(bindingResult.hasErrors()) {
			bindingResult.reject("createProductError", null, "???????????? ?????? ????????? ????????????.");
			
			// ????????? ?????? DB?????? ??????
			Product product = productService.findProduct(form.getId());
			form.setProductImage(product.getProductImage());
			
			// ?????? ????????????
			List<Option> options = product.getProductOption().getOption();
			List<UpdateOptionForm> optionForms = new ArrayList<>();
			for (Option option : options) {
				UpdateOptionForm updateOptionForm = UpdateOptionForm.createOptionForm(option.getId(), option.getNames(), option.getStockQuantity());
				optionForms.add(updateOptionForm);
			}
			form.setOption(optionForms);
			
			return "product/product";
		}
		
		// ?????????
		redirectAttributes.addAttribute("productId", form.getId());
		return "redirect:/admin/products/{productId}";
	}
	
	// ?????? ??????
	@GetMapping("/{id}/remove")
	public String deleteProduct(@PathVariable("id") Long id) {
		
		productService.deleteProduct(id);
		return "redirect:/admin/products";
	}
	
	
}
