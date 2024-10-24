package com.see_nior.seeniorAdmin.disease;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;



@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/disease")
public class DiseaseController {

	final private DiseaseService diseaseService;
	
	// ----------------------------------------------------------------질환 카테고리
	
	// 질환 카테고리 등록 양식
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		String nextPage = "disease/create_category_form";
		
		return nextPage;
		
	}
	
	// 질환 카테고리명 중복 확인
	@ResponseBody
	@GetMapping("/cate_info/is_disease_category")
	public boolean isDiseaseCategory(@RequestParam(value = "dc_name") String dc_name) {
		log.info("isDiseaseCategory()");
		
		boolean isDiseaseCategory = diseaseService.isDiseaseCategory(dc_name);
		
		return isDiseaseCategory;
		
	}
	
	// 질환 카테고리 등록 확인
	@ResponseBody
	@PostMapping("/cate_info/create_category_confirm")
	public boolean createCategoryConfirmW(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("createCategoryConfirm()");
		
		boolean createCategoryResult = diseaseService.createCategoryConfirm(diseaseCategoryDto);
		
		return createCategoryResult;
		
	}
	
	// 질환 카테고리 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		String nextPage = "disease/category_list_form";
		
		return nextPage;
		
	}
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list_select")
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> diseaseCategoryDtos = diseaseService.getCategoryList();
		
		return diseaseCategoryDtos;
		
	}
	
	// 모든 질환 카테고리 가져오기(페이지네이션 => 질환 카테고리 관리용 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "dc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getCategoryList()");
		
		// 페이지 번호에 따른 질환 카테고리 리스트들 가져오기
		Map<String, Object> diseaseCategoryListWithPage = diseaseService.getCategoryListWithPage(page, sortValue, order);
		
		// 질환 카테고리 총 페이지 개수 가져오기
		Map<String, Object> diseaseCategoryListPageNum = diseaseService.getDiseaseCategoryListPageNum(page);
		
		diseaseCategoryListWithPage.put("diseaseCategoryListPageNum", diseaseCategoryListPageNum);
		diseaseCategoryListWithPage.put("sortValue", sortValue);
		diseaseCategoryListWithPage.put("order", order);
		
		return diseaseCategoryListWithPage;
		
	}
	
	// 질환 카테고리 수정 양식
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(@RequestParam(value = "dc_no") int dc_no, Model model) {
		log.info("modifyCategoryForm()");
		
		String nextPage = "disease/modify_category_form";
		
		DiseaseCategoryDto diseaseCategoryDto = diseaseService.getCategory(dc_no);
		
		model.addAttribute("diseaseCategoryDto", diseaseCategoryDto);
		
		return nextPage;
		
	}
	
	// 질환 카테고리 수정 확인 (비동기)
	@ResponseBody
	@PostMapping("/cate_info/modify_category_confirm")
	public boolean modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		boolean modifyCategoryResult = diseaseService.modifyCategoryConfirm(diseaseCategoryDto);
		
		return modifyCategoryResult;
		
	}
	
	// 질환 카테고리 삭제 확인(비동기)
	@ResponseBody
	@PostMapping("/cate_info/delete_category_confirm")
	public boolean deleteCategoryConfirm(@RequestParam(value = "dc_no") int dc_no) {
		log.info("deleteCategoryConfirm()");
		
		boolean deleteCategoryResult = diseaseService.deleteCategoryConfirm(dc_no);
		
		return deleteCategoryResult;
		
	}
	
	// 질환 카테고리 검색(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/search_disease_category_list")
	public Object searchDiseaseCategoryList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchDiseaseCategoryList()");
		
		// 페이지 번호에 따른 검색 질환 카테고리 리스트들 가져오기
		Map<String, Object> searchDiseaseCategoryListWithPage = diseaseService.getSearchDiseaseCategoryListWithPage(searchPart, searchString, page);
		
		// 검색 질환 카테고리 총 페이지 개수 가져오기
		Map<String, Object> searchDiseaseCategoryListPageNum = diseaseService.getSearchDiseaseCategoryListPageNum(searchPart, searchString, page);
		
		searchDiseaseCategoryListWithPage.put("searchDiseaseCategoryListPageNum", searchDiseaseCategoryListPageNum);
		searchDiseaseCategoryListWithPage.put("searchPart", searchPart);
		searchDiseaseCategoryListWithPage.put("searchString", searchString);
		
		return searchDiseaseCategoryListWithPage;
		
	}
	
	// ----------------------------------------------------------------질환
	
	// 질환 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "disease/create_form";
		
		return nextPage;
		
	}
	
	// 질환명 중복 확인
	@ResponseBody
	@GetMapping("/info/is_disease")
	public boolean isDisease(@RequestParam(value = "d_name") String d_name,
							@RequestParam(value = "d_no", required = false, defaultValue = "0") int d_no) {
		log.info("isDisease()");
		
		boolean isDisease = diseaseService.isDisease(d_name, d_no);
		
		return isDisease;
		
	}
		
	// 질환 등록 확인
	@ResponseBody
	@PostMapping("/info/create_confirm")
	public boolean createConfirm(DiseaseDto diseaseDto) {
		log.info("createConfirm()");
		
		boolean createresult = diseaseService.createConfirm(diseaseDto);
		
		return createresult;
		
	}
		
	// 질환 리스트 양식
	@GetMapping("/info/disease_list_form")
	public String diseaseListForm() {
		log.info("diseaseListForm()");
		
		String nextPage = "disease/disease_list_form";
		
		return nextPage;
		
	}
	
	// 모든 질환 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/get_disease_list")
	public Object getDiseaseList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "d_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getDiseaseList()");
		
		// 페이지 번호에 따른 질환 리스트들 가져오기
		Map<String, Object> diseaseListWithPage = diseaseService.getDiseaseListWithPage(page, sortValue, order);
		
		// 질환 총 페이지 개수 가져오기
		Map<String, Object> diseaseListPageNum = diseaseService.getDiseaseListPageNum(page);
		
		diseaseListWithPage.put("diseaseListPageNum", diseaseListPageNum);
		diseaseListWithPage.put("sortValue", sortValue);
		diseaseListWithPage.put("order", order);
		
		return diseaseListWithPage;
		
	}
	
	// 카테고리에 따른 질환 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/get_disease_list_by_category")
	public Object getDiseaseListByCategory(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "d_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "infoNo") int dc_no) {
		log.info("getDiseaseListByCategory()");
		
		// 페이지 번호에 따른 카테고리별 질환 리스트들 가져오기
		Map<String, Object> diseaseListByCategoryWithPage = diseaseService.getDiseaseListByCategoryWithPage(page, sortValue, order, dc_no);
		
		// 카테고리별 질환 총 페이지 개수 가져오기
		Map<String, Object> diseaseListByCategoryPageNum = diseaseService.getDiseaseListByCategoryPageNum(page, dc_no);
		
		diseaseListByCategoryWithPage.put("diseaseListByCategoryPageNum", diseaseListByCategoryPageNum);
		diseaseListByCategoryWithPage.put("sortValue", sortValue);
		diseaseListByCategoryWithPage.put("order", order);
		diseaseListByCategoryWithPage.put("infoNo", dc_no);
		
		return diseaseListByCategoryWithPage;
		
	}
	
	// 질환 한개 가져오기
	@ResponseBody
	@GetMapping("/info/get_disease")
	public Object getDisease(@RequestParam(value = "d_no") int d_no) {
		log.info("getDisease()");
		
		DiseaseDto diseaseDto = diseaseService.getDisease(d_no);
		
		return diseaseDto;
		
	}
	
	// 질환 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam(value = "d_no") int d_no, Model model) {
		log.info("modifyForm()");
		
		String nextPage = "disease/modify_form";
		
		DiseaseDto diseaseDto = diseaseService.getDisease(d_no);
		
		model.addAttribute("diseaseDto", diseaseDto);
		
		return nextPage;
		
	}
	
	// 질환 수정 확인
	@ResponseBody
	@PostMapping("/info/modify_confirm")
	public boolean modifyConfirm(DiseaseDto diseaseDto) {
		log.info("modifyConfirm()");
		
		boolean modifyResult = diseaseService.modifyConfirm(diseaseDto);
		
		return modifyResult;
	}
	
	// 질환 삭제 확인
	@ResponseBody
	@PostMapping("/info/delete_confirm")
	public boolean deleteConfirm(@RequestParam(value = "d_nos") List<Integer> d_nos) {
		log.info("deleteConfirm()");
		
		boolean deleteResult = diseaseService.deleteConfirm(d_nos);
		
		return deleteResult;
	}
	
	// 질환 검색(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/search_disease_list")
	public Object searchDiseaseList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchDiseaseList()");
		
		// 페이지 번호에 따른 검색 질환 리스트들 가져오기
		Map<String, Object> searchDiseaseListWithPage = diseaseService.getSearchDiseaseListWithPage(searchPart, searchString, page);
		
		// 검색 질환 총 페이지 개수 가져오기
		Map<String, Object> searchDiseaseListPageNum = diseaseService.getSearchDiseaseListPageNum(searchPart, searchString, page);
		
		searchDiseaseListWithPage.put("searchDiseaseListPageNum", searchDiseaseListPageNum);
		searchDiseaseListWithPage.put("searchPart", searchPart);
		searchDiseaseListWithPage.put("searchString", searchString);
		
		return searchDiseaseListWithPage;
		
	}
	
	
}