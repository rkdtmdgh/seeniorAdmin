package com.see_nior.seeniorAdmin.mealProvider.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface mealProviderMapper {

	// 식단 API 데이터 DB에 입력하기
	public void insertApiRecipeData(String jsonRecipeData);

}
