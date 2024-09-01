package com.see_nior.seeniorAdmin.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/recipe")
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	
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

}
