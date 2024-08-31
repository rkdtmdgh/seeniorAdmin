package com.see_nior.seeniorAdmin.mealProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/mealProvider")
public class MealProviderController {
	
	/*
	@Autowired
	private ApiExplorer apiExplorer;
	*/
	
	@Autowired
	private MealProviderService mealProviderService;
	
	// 레시피 API 불러오기 (json) => 시험용
	/*
	@ResponseBody
	@GetMapping("/get_recipe_data")
	public String getRecipeData() {
		log.info("getRecipeData()");
		
		try {
			
			return apiExplorer.getRecipe();
			
		} catch (Exception e) {
			
			return "{\"error\":\"Failed to fetch data\"}";
		}
		
	}
	*/
	
	// 레시피 API 불러온 후 json 으로 넘어온 API Data DB에 저장하기
	@ResponseBody
	@GetMapping("/save_recipe_data")
	public String saveRecipeData() throws Exception {
		log.info("saveRecipeData()");
		
		try {
			mealProviderService.saveRecipeData();
			return "{\"status\":\"Data successfully saved to the database\"}";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"error\":\"Failed to fetch and save data\"}";
		}
		
	}
	
	
	

}
