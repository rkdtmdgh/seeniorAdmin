package com.see_nior.seeniorAdmin.disease;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/disease")
public class DiseaseController {

	final private DiseaseService diseaseService;
	
	public DiseaseController(DiseaseService diseaseService) {
		this.diseaseService = diseaseService;
		
	}
	
	// ----------------------------------------------------------------질환 카테고리
	
	// 질환 카테고리 추가 양식
	@GetMapping("/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		String nextPage = "disease/create_category_form";
		
		return nextPage;
		
	}
	
	// 질환 카테고리 추가 확인
		@PostMapping("/create_category_confirm")
		public String createCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto, Model model) {
			log.info("createCategoryConfirm()");
			
			int result = diseaseService.createCategoryConfirm(diseaseCategoryDto);
			
			model.addAttribute("createCategoryResult", result);
			
			String nextPage = "disease/create_category_result";
			
			return nextPage;
			
		}
	
	// 질환 카테고리 가져오기
	@GetMapping("/get_category_list")
	public Object getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> diseaseCategoryDtos = diseaseService.getCategoryList();
		
		return diseaseCategoryDtos;
		
	}
	
	// 질환 카테고리 수정 양식
	@GetMapping("/modify_category_form")
	public String modifyCategoryForm() {
		log.info("modifyCategoryForm()");
		
		String nextPage = "disease/modify_category_form";
		
		return nextPage;
		
	}
	
	// 질환 카테고리 수정 확인
	@PostMapping("/modify_category_confirm")
	public String modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto, Model model) {
		log.info("modifyCategoryConfirm()");
		
		int result = diseaseService.modifyCategoryConfirm(diseaseCategoryDto);
		
		model.addAttribute("modifyCategoryResult" + result);
		
		String nextPage = "disease/modify_category_result";
		
		return nextPage;
		
	}
	
	// 질환 카테고리 삭제 확인
	@GetMapping("/delete_category_confirm")
	public String deleteCategoryConfirm(@RequestParam int no, Model model) {
		log.info("deleteCategoryConfirm()");
		
		int result = diseaseService.deleteCategoryConfirm(no);
		
		model.addAttribute("deleteCategoryResult", result);
		
		String nextPage = "disease/delete_category_result";
		
		return nextPage;
		
	}
	
	
	// ----------------------------------------------------------------질환
	
	// 질환 추가 양식
	@GetMapping("/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "disease/create_form";
		
		return nextPage;
		
	}
		
		// 질환 추가 확인
//		@PostMapping("/create_confirm")
//		public String createConfirm(DiseaseDto diseaseDto, Model model) {
//			log.info("createConfirm()");
//			
//			int result = diseaseService.createConfirm(diseaseDto);
//			
//			model.addAttribute("createResult", result);
//			
//			String nextPage = "disease/create_result";
//			
//			return nextPage;
//			
//		}
		
	// 질환 리스트 양식
	@GetMapping("/disease_list")
	public String diseaseList() {
		log.info("diseaseList()");
		
		String nextPage = "disease/disease_list";
		
		return nextPage;
		
	}
	
	
}
