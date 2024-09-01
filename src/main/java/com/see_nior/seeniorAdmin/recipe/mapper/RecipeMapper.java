package com.see_nior.seeniorAdmin.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.RecipeDto;

@Mapper
public interface RecipeMapper {

	// 식단 API 데이터 DB에 입력하기
	public void insertApiRecipeData(RecipeDto recipeDto);

}
