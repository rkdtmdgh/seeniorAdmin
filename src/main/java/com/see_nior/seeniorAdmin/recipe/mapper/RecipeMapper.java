package com.see_nior.seeniorAdmin.recipe.mapper;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.RecipeDto;

@Mapper
public interface RecipeMapper {
	
	// 식단 테이블이 있는지 확인하기 (1: 식단 테이블 있음 / 0: 식단 테이블 없음)
	public int checkTableExists();
	
	// 식단 테이블 DROP
	public void dropRecipeTable();
	
	// 식단 테이블 CREATE
	public void createRecipeTable();
	
	// 식단 테이블이 생성된 날짜값 가져오기
	public Date getRecipeTableCreateTime();

	// 식단 API 데이터 DB에 입력하기
	public void insertApiRecipeData(RecipeDto recipeDto);

	// 모든 식단 분류 가져오기(식단 리스트에서 <select>박스 => 비동기)
	public List<RecipeDto> getRecipeTypeList();
	
	// 페이지에 따른 식단 가져오기 (모든 식단)
	public List<RecipeDto> getRecipeListWithPage(Map<String, Object> pagingParams);

	// 식단의 총 리스트 개수 구하기 (모든 식단)
	public int getAllRecipeCnt();

	// 페이지에 따른 식단 가져오기(카테고리별 식단)
	public List<RecipeDto> getRecipeListByTypeWithPage(Map<String, Object> pagingParams);

	// 식단의 총 리스트 개수 구하기(카테고리별 식단)
	public int getRecipeByTypeCnt(String rcp_pat2);

	// 페이지에 따른 식단 가져오기(검색한 식단)
	public List<RecipeDto> getSearchRecipe(Map<String, Object> pagingParams);
	
	// 식단의 총 리스트 개수 구하기(검색한 식단)
	public int getSearchRecipeListCnt(Map<String, Object> pagingParams);
	
	// 식단 한 개 가져오기
	public RecipeDto getRecipe(int rcp_seq);




	
	

}
