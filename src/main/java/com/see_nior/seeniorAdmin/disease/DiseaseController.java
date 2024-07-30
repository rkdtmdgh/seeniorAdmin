package com.see_nior.seeniorAdmin.disease;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	
	// 질환 추가 양식
	@GetMapping("/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "disease/create_form";
		
		return nextPage;
		
	}
	
	// 질환 추가 확인
	@PostMapping("/create_confirm")
	public String createConfirm(DiseaseDto diseaseDto, Model model) {
		log.info("createConfirm()");
		
		int result = diseaseService.createConfirm(diseaseDto);
		
		model.addAttribute("createResult", result);
		
		String nextPage = "disease/create_result";
		
		return nextPage;
		
	}
	
	// 질환 카테고리 가져오기
	@GetMapping("/get_category_list")
	public Object getCategoryList() {
		log.info("getCategoryList()");
		
		DiseaseCategoryDto diseaseCategoryDtos = diseaseService.getCategoryList();
		
		return diseaseCategoryDtos;
		
	}
	
	// 질환 리스트 양식
	@GetMapping("/disease_list")
	public String diseaseList() {
		log.info("diseaseList()");
		
		String nextPage = "disease/disease_list";
		
		return nextPage;
		
	}
	
	// 질환 카테고리 가져오기
//	@GetMapping("/disease_list")
//	public String diseaseList(Model model) {
//		log.info("diseaseList()");
//		
//		String nextPage = "disease/disease_list";
//		
//		ArrayList<DiseaseDto> diseaseDtos = diseaseService.getDiseaseList();
//		
//		model.addAttribute("diseaseDtos", diseaseDtos);
//		
//		return nextPage;
//		
//	}
	
	// 질환 카테고리 수정 양식
	
	
	
	
	// 질환 카테고리 수정 확인
	
	
	
	
	// 질환 카테고리 삭제

	
	
}
