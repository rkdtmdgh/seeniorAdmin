package com.see_nior.seeniorAdmin.disease.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

@Mapper
public interface DiseaseMapper {
	
	public int insertNewDiseaseCategory(DiseaseCategoryDto diseaseCategoryDto);

	public boolean isDiseaseCategory(String name);

	public boolean isDisease(DiseaseDto diseaseDto);

	public int insertNewDisease(DiseaseDto diseaseDto);

	public DiseaseCategoryDto getCategoryList();

	
	
}
