package com.see_nior.seeniorAdmin.mealProvider;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.api.ApiExplorer;
import com.see_nior.seeniorAdmin.mealProvider.mapper.mealProviderMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MealProviderService {
	
	@Autowired
	private ApiExplorer apiExplorer;
	
	@Autowired
	mealProviderMapper mealProviderMapper;

	public void saveRecipeData() {
		log.info("saveRecipeData()");
		
		String jsonRecipeData = null;
		
		try {
			jsonRecipeData = apiExplorer.getRecipe();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		if (jsonRecipeData != null) mealProviderMapper.insertApiRecipeData(jsonRecipeData);
		
	}

}
