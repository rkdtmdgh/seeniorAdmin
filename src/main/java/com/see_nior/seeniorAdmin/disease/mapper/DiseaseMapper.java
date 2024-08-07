package com.see_nior.seeniorAdmin.disease.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

@Mapper
public interface DiseaseMapper {
	
	// -----------------------------------질환 카테고리 -----------------------------------------------------
	
	// 질환 카테고리 등록
	public int insertNewDiseaseCategory(DiseaseCategoryDto diseaseCategoryDto);

	// 질환 카테고리 중복체크
	public boolean isDiseaseCategory(String name);
	
	// 모든 질환 카테고리 가져오기
	public List<DiseaseCategoryDto> getDiseaseCategoryList();
	
	// 질환 카테고리 한개 가져오기
	public DiseaseCategoryDto getDiseaseCategory(int no);
	
	// 질환 카테고리 수정
	public int updateDiseaseCategory(DiseaseCategoryDto diseaseCategoryDto);

	// 질환 카테고리 삭제
	public int deleteDiseaseCategory(int no);
	
	// -----------------------------------질환 -------------------------------------------------------------

	// 질환 등록
	public int insertNewDisease(DiseaseDto diseaseDto);
		
	// 질환 중복체크
	public boolean isDisease(String name);
	
	// 모든 질환 가져오기
	public List<DiseaseDto> getAllDiseaseList();
	
	// 카테고리에 따른 질환 가져오기
	public List<DiseaseDto> getDiseaseListByCategory(int no);
	
	// 질환 한개 가져오기
	public DiseaseDto getDiseaseByNo(int no);
	
	// 질환 수정
	public int updateDisease(DiseaseDto diseaseDto);
	
	// 질환 삭제
	public int deleteCategory(int no);

	// 질환 검색
	public List<DiseaseDto> searchDiseaseByName(String name);




	

	



	

	
	
}
