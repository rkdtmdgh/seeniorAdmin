package com.see_nior.seeniorAdmin.disease.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

@Mapper
public interface DiseaseMapper {
	
	// 질환 카테고리 등록
	public int insertNewDiseaseCategory(DiseaseCategoryDto diseaseCategoryDto);

	// 질환 카테고리 중복체크
	public boolean isDiseaseCategory(String name);
	
	// 질환 카테고리 가져오기
	public List<DiseaseCategoryDto> getCategoryList();
	
	// 질환 카테고리 수정
	public int updateDiseaseCategory(DiseaseCategoryDto diseaseCategoryDto);

	// 질환 카테고리 삭제
	public int deleteCategoryConfirm(int no);

	// 질환 등록
	public int insertNewDisease(DiseaseDto diseaseDto);
		
	// 질환 중복체크
	public boolean isDisease(DiseaseDto diseaseDto);

	
	
	// 질환 가져오기
	
	// 질환 수정
	
	// 질환 삭제

	



	

	
	
}
