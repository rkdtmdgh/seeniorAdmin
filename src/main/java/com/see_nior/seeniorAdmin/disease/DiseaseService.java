package com.see_nior.seeniorAdmin.disease;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.disease.mapper.DiseaseMapper;
import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DiseaseService {
	
	// 질환 카테고리
	final static public int DISEASE_CATEGORY_ALREADY = -1;
	final static public int DISEASE_CATEGORY_CREATE_FAIL = 0;
	final static public int DISEASE_CATEGORY_CREATE_SUCCESS = 1;
	final static public int DISEASE_CATEGORY_MODIFY_FAIL = 0;
	final static public int DISEASE_CATEGORY_MODIFY_SUCCESS = 1;
	final static public int DISEASE_CATEGORY_DELETE_FAIL = 0;
	final static public int DISEASE_CATEGORY_DELETE_SUCCESS = 1;
	
	// 질환
	final static public int DISEASE_ALREADY = -1;
	final static public int DISEASE_CREATE_FAIL = 0;
	final static public int DISEASE_CREATE_SUCCESS = 1;
	final static public int DISEASE_MODIFY_FAIL = 0;
	final static public int DISEASE_MODIFY_SUCCESS = 1;
	final static public int DISEASE_DELETE_FAIL = 0;
	final static public int DISEASE_DELETE_SUCCESS = 1;
	
	final private DiseaseMapper diseaseMapper;
	
	public DiseaseService(DiseaseMapper diseaseMapper) {
		this.diseaseMapper = diseaseMapper;
		
	}
	
	// --------------------------------------------- 질환 카테고리

	// 질환 카테고리 추가 확인
	public int createCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("createCategoryConfirm()");
		
		// 질환 카테고리 중복 여부
		boolean isDiseaseCategory = diseaseMapper.isDiseaseCategory(diseaseCategoryDto.getName());
		
		// 질환 카테고리가 없다면
		if (!isDiseaseCategory) {
			
			int result = diseaseMapper.insertNewDiseaseCategory(diseaseCategoryDto);
			
			// DB에 입력 실패
			if (result <= 0) {
				
				return DISEASE_CATEGORY_CREATE_FAIL;
				
			// DB에 입력 성공
			} else {
				
				return DISEASE_CATEGORY_CREATE_SUCCESS;
				
			}
			
			
		// 질환 카테고리가 이미 있다면
		} else {
			log.info("DISEASE_CATEGORY_ALREADY");
			return DISEASE_CATEGORY_ALREADY;
			
		}
		
	}
	
	// 질환 카테고리 가져오기
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> diseaseCategoryDtos = new LinkedHashMap<>();
		
		List<DiseaseCategoryDto> diseaseCategoryDto = (List<DiseaseCategoryDto>) diseaseMapper.getCategoryList();
		
		diseaseCategoryDtos.put("diseaseCategoryDto", diseaseCategoryDto);
		
		return diseaseCategoryDtos;
		
	}

	
	// 질환 카테고리 수정 확인
	public int modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		// 질환 카테고리 중복 여부
		boolean isDiseaseCategory = diseaseMapper.isDiseaseCategory(diseaseCategoryDto.getName());
		
		// 질환 카테고리가 없다면
		if (!isDiseaseCategory) {
			
			int result = diseaseMapper.updateDiseaseCategory(diseaseCategoryDto);
			
			// DB에 입력 실패
			if (result <= 0) {
				
				return DISEASE_CATEGORY_MODIFY_FAIL;
				
			// DB에 입력 성공
			} else {
				
				return DISEASE_CATEGORY_MODIFY_SUCCESS;
				
			}
			
		// 질환 카테고리가 이미 있다면
		} else {
			return DISEASE_CATEGORY_ALREADY;
			
		}
	
	}
	
	// 질환 카테고리 삭제 확인
	public int deleteCategoryConfirm(int no) {
		log.info("deleteCategoryConfirm()");
		
		int result = diseaseMapper.deleteCategoryConfirm(no);
		
		// DB에 입력 실패
		if (result <= 0) {
			
			return DISEASE_CATEGORY_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			
			return DISEASE_CATEGORY_CREATE_SUCCESS;
			
		}
		
	}
	
	

	
	
	// --------------------------------------------- 질환
	
	// 질환 추가 확인
		public int createConfirm(DiseaseDto diseaseDto) {
			log.info("createConfirm()");
			
			// 질환 중복 여부
			boolean isDisease = diseaseMapper.isDisease(diseaseDto);
			
			// 질환이 없다면
			if (!isDisease) {
				
				int result = diseaseMapper.insertNewDisease(diseaseDto);
				
				// DB에 입력 실패
				if (result <= 0) {
					return DISEASE_CREATE_FAIL;
					
				// DB에 입력 성공
				} else {
					
					return DISEASE_CREATE_SUCCESS;
				}
				
				
			// 질환이 이미 있다면
			} else {
				return DISEASE_ALREADY;
				
			}
			
		}
		
		

	
	

}
