package com.see_nior.seeniorAdmin.recipe.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.RecipeDto;

@Mapper
public interface RecipeMapper {

	// 식단 API 데이터 DB에 입력하기
	public void insertApiRecipeData(RecipeDto recipeDto);

	// 페이지에 따른 식단 가져오기 (모든 식단)
	public List<RecipeDto> getRecipeListWithPage(Map<String, Object> pagingParams);

	// 식단의 총 리스트 개수 구하기 (모든 식단)
	public int getAllRecipeCnt();
	
	

}
