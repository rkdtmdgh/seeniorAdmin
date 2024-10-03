package com.see_nior.seeniorAdmin.recipe;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.RecipeDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class RecipeController {

	final private RecipeService recipeService;
	
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
	@GetMapping("cate_info/get_category_list_select")
	public Object getCategorylistSelect() {
		log.info("getCategorylistSelect()");
		
		Map<String, Object> recipeCategoryDtos = recipeService.getCategoryList();
		
		return recipeCategoryDtos;
	}
	
	// 모든 식단 가져오기 (페이지네이션)
	@ResponseBody
	@GetMapping("info/get_recipe_list")
	public Object getRecipeList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "rcp_seq") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getRecipeList()");
		
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
	@GetMapping("info/get_recipe_list_by_category")
	public Object getRecipeListByCategory(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "all") String sortValue,
			@RequestParam(value = "order") String rcp_pat2) {
		log.info("getRecipeListByCategory()");
		
		// 페이지 번호에 따른 카테고리별 식단 리스트들 가져오기
		Map<String, Object> recipeListByCategoryWithPage = recipeService.getRecipeListByCategoryWithPage(page, rcp_pat2);
		
		// 카테고리별 식단 총 페이지 개수 가져오기
		Map<String, Object> recipeListByCategoryPageNum = recipeService.getRecipeListByCategoryPageNum(page, rcp_pat2);
		
		recipeListByCategoryWithPage.put("recipeListByCategoryPageNum", recipeListByCategoryPageNum);
		recipeListByCategoryWithPage.put("sortValue", sortValue);
		recipeListByCategoryWithPage.put("order", rcp_pat2);
		
		return recipeListByCategoryWithPage;
		
	}
	
	// 식단 검색(페이지네이션)
	@ResponseBody
	@GetMapping("info/search_recipe_list")
	public Object searchRecipeList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchRecipeList()");
		
		// 페이지 번호에 따른 검색 식단 리스트들 가져오기
		Map<String, Object> searchRecipeListWithPage = recipeService.getSearchRecipeListWithPage(searchPart, searchString, page);

		// 검색 식단 총 페이지 개수 가져오기
		Map<String, Object> searchRecipeListPageNum = recipeService.getSearchRecipeListPageNum(searchPart, searchString, page);
		
		searchRecipeListWithPage.put("searchRecipeListPageNum", searchRecipeListPageNum);
		searchRecipeListWithPage.put("searchPart", searchPart);
		searchRecipeListWithPage.put("searchString", searchString);
		
		return searchRecipeListWithPage;
	
	}
	
	// 식단 디테일 뷰 양식
	@GetMapping("info/detail_form")
	public String detailForm(@RequestParam(value = "rcp_seq") int rcp_seq, Model model) {
		log.info("detailForm()");
		
		String nextPage = "recipe/detail_form";
		
		RecipeDto recipeDto = recipeService.getRecipe(rcp_seq);
		
		model.addAttribute("recipeDto", recipeDto);
		
		return nextPage;
		
	}
	
	// 식단 한개 가져오기
	@ResponseBody
	@GetMapping("info/get_recipe")
	public Object getRecipe(@RequestParam(value = "rcp_seq") int rcp_seq) {
		log.info("getRecipe()");
		
		RecipeDto recipeDto = recipeService.getRecipe(rcp_seq);		
		
		return recipeDto;
		
	}

}
