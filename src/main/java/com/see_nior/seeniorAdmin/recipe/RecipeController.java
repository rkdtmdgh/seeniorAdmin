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
	public String refreshApiRecipeData() throws Exception {
		log.info("refreshApiRecipeData()");
		
		try {
			recipeService.refreshApiRecipeData();
			return "레시피 API를 최신 정보로 업데이트 하는데 성공하였습니다.";
			
			
		} catch (Exception e) {
			e.printStackTrace();
			return "레시피 API를 최신 정보로 업데이트 하는데 실패하였습니다.";
		}
		
	}
	
	// 식단 리스트 양식
	@GetMapping("info/recipe_list_form")
	public String recipeListForm() {
		log.info("recipeList()");
		
		String nextPage = "recipe/recipe_list_form";
		
		return nextPage;
		
	}
	
	// 모든 식단 가져오기 (페이지네이션)
	@ResponseBody
	@GetMapping("info/get_all_recipe_list_with_page")
	public Object getAllRecipeListWithPage(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sort", required = false, defaultValue = "all") String sort) {
		log.info("getAllRecipeListWithPage()");
		
		// 페이지 번호에 따른 식단 리스트들 가져오기
		Map<String, Object> recipeListWithPage = recipeService.getRecipeListWithPage(page, sort);
		
		// 식단 총 페이지 개수 가져오기
		Map<String, Object> recipeListPageNum = recipeService.getRecipeListPageNum(page);
		
		recipeListWithPage.put("recipeListPageNum", recipeListPageNum);
		recipeListWithPage.put("sort", sort);
		
		return recipeListWithPage;
		
	}
	
	// 요리 종류에 따른 식단 가져오기(페이지네이션)
	@ResponseBody
	@GetMapping("info/get_recipe_list_by_part_with_page")
	public String getRecipeListByPartWithPage(@RequestParam String param) {
		
		
		return null;
	}
	
	

}
