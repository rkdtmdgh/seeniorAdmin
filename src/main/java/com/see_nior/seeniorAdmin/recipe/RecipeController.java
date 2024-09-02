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

	// 레시피 API 불러온 후 json 으로 넘어온 API Data DB에 저장하기
	@ResponseBody
	@GetMapping("/save_api_recipe_data")
	public String saveApiRecipeData() throws Exception {
		log.info("saveApiRecipeData()");
		
		try {
			recipeService.saveApiRecipeData();
			return "{\"status\":\"Data successfully saved to the database\"}";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"Failed to fetch and save data\"}";
		}
		
	}
	
	// 식단 리스트 양식
	@GetMapping("/recipe_list")
	public String recipeList() {
		log.info("recipeList()");
		
		String nextPage = "recipe/recipe_list";
		
		return nextPage;
		
	}
	
	// 모든 식단 가져오기 (페이지네이션)
	@ResponseBody
	@GetMapping("/get_all_recipe_list_with_page")
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
	
	

}
