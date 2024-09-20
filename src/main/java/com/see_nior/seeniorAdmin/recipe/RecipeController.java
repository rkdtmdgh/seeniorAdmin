package com.see_nior.seeniorAdmin.recipe;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/recipe")
public class RecipeController {

	final private RecipeService recipeService;
	
	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
		
	}
	
	// 기존 레시피 테이블 삭제 후 테이블 생성 후 API 데이터 DB에 저장하기
	@ResponseBody
	@GetMapping("info/refresh_api_recipe_data")
	public boolean refreshApiRecipeData() throws Exception {
		log.info("refreshApiRecipeData()");
		
		boolean refreshApiRecipeData = recipeService.refreshApiRecipeData();
		
		return refreshApiRecipeData;	
			
	}
	
	// 식단 리스트 양식
	@GetMapping("info/recipe_list_form")
	public String recipeListForm() {
		log.info("recipeList()");
		
		String nextPage = "recipe/recipe_list_form";
		
		return nextPage;
		
	}
	
	// 모든 식단 분류 가져오기 (식단 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("cate_info/get_category_list")
	public Object getCategorylist() {
		log.info("getCategoryList()");
		
		Map<String, Object> recipeCategoryDtos = recipeService.getCategoryList();
		
		return recipeCategoryDtos;
	}
	
	// 모든 식단 가져오기 (페이지네이션)
	@ResponseBody
	@GetMapping("info/get_all_recipe_list_with_page")
	public Object getAllRecipeListWithPage(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "all") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "all") String order) {
		log.info("getAllRecipeListWithPage()");
		
		// 페이지 번호에 따른 식단 리스트들 가져오기
		Map<String, Object> recipeListWithPage = recipeService.getRecipeListWithPage(page, sortValue, order);
		
		// 식단 총 페이지 개수 가져오기
		Map<String, Object> recipeListPageNum = recipeService.getRecipeListPageNum(page);
		
		recipeListWithPage.put("recipeListPageNum", recipeListPageNum);
		recipeListWithPage.put("sortValue", sortValue);
		recipeListWithPage.put("order", order);
		
		return recipeListWithPage;
		
	}
	
	// 카테고리에 따른 식단 가져오기(페이지네이션)
	@ResponseBody
	@GetMapping("info/get_recipe_list_by_category_with_page")
	public String getRecipeListByCategoryWithPage(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "all") String SortValue,
			@RequestParam(value = "order") int rcp_pat2) {
		log.info("getRecipeListByCategoryWithPage()");
		
		return null;
		
	}
	
	

}
