package com.see_nior.seeniorAdmin.disease;

import java.util.HashMap;
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
	
	// 페이지네이션 관련
	private int pageLimit = 5;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
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
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> diseaseCategoryDtos = new HashMap<>();
		
		List<DiseaseCategoryDto> diseaseCategoryDto = (List<DiseaseCategoryDto>) diseaseMapper.getDiseaseCategoryList();
		
		diseaseCategoryDtos.put("diseaseCategoryDto", diseaseCategoryDto);
		
		return diseaseCategoryDtos;
		
	}
	
	// 모든 질환 카테고리 가져오기(페이지네이션 => 질환 카테고리 관리용 => 비동기)
	public Map<String, Object> getCategoryListWithPage(int page) {
		log.info("getCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		
		List<DiseaseCategoryDto> diseaseCategoryDtos = diseaseMapper.getDiseaseCategoryListWithPage(pagingParams);
		pagingList.put("diseaseCategoryDtos", diseaseCategoryDtos);
		
		return pagingList;
	}
	
	// 질환 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getDiseaseCategoryListPageNum(int page) {
		log.info("getDiseaseCategoryListPageNum()");
		
		Map<String, Object> diseaseCategoryListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int diseaseCategoryListCnt = diseaseMapper.getAllDiseaseCategoryCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) diseaseCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산 
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		diseaseCategoryListPageNum.put("diseaseCategoryListCnt", diseaseCategoryListCnt);
		diseaseCategoryListPageNum.put("maxPage", maxPage);
		diseaseCategoryListPageNum.put("startPage", startPage);
		diseaseCategoryListPageNum.put("endPage", endPage);
		
		return diseaseCategoryListPageNum;
	}
	
	// 질환 카테고리 한개 가져오기
	public DiseaseCategoryDto getCategory(int no) {
		log.info("getCategory()");
		
		DiseaseCategoryDto diseaseCategoryDto = diseaseMapper.getDiseaseCategory(no);
		
		return diseaseCategoryDto;
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
		
		int result = diseaseMapper.deleteDiseaseCategory(no);
		
		// DB에 입력 실패
		if (result <= 0) {
			
			return DISEASE_CATEGORY_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			
			return DISEASE_CATEGORY_DELETE_SUCCESS;
			
		}
		
	}
	
	
	// --------------------------------------------- 질환
	
	// 질환 추가 확인
	public int createConfirm(DiseaseDto diseaseDto) {
		log.info("createConfirm()");
		
		// 질환 중복 여부
		boolean isDisease = diseaseMapper.isDisease(diseaseDto.getName());
		
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
	
	// 모든 질환 가져오기 -> 페이지네이션 안 한 기존
	public Map<String, Object> getAllDiseaseList() {
		log.info("getAllDiseaseList()");
		
		Map<String, Object> allDiseaseDtos = new HashMap<>();
		
		List<DiseaseDto> allDiseaseDto = (List<DiseaseDto>) diseaseMapper.getAllDiseaseList();
		
		allDiseaseDtos.put("allDiseaseDto", allDiseaseDto);
		
		return allDiseaseDtos;
	}
	
	// 카테고리에 따른 질환 가져오기
	public Map<String, Object> getDiseaseListByCategory(int no) {
		log.info("getDiseaseListByCategory()");
		
		Map<String, Object> diseaseDtosByCategory = new HashMap<>();
		
		List<DiseaseDto> diseaseDtoByCategory = (List<DiseaseDto>) diseaseMapper.getDiseaseListByCategory(no);
		
		diseaseDtosByCategory.put("diseaseDtoByCategory", diseaseDtoByCategory);
		
		return diseaseDtosByCategory;
	}
	
	// 질환 한 개 가져오기
	public DiseaseDto getDisease(int no) {
		log.info("getDisease()");
		
		DiseaseDto diseaseDto = diseaseMapper.getDiseaseByNo(no);
		
		return diseaseDto;
	}
	
	
	// 질환 수정 확인
	public int modifyConfirm(DiseaseDto diseaseDto) {
		log.info("modifyConfirm");
		
		// 질환 중복 여부
		boolean isDisease = diseaseMapper.isDisease(diseaseDto.getName());

		// 질환이 없다면
		if (!isDisease) {
			
		int result = diseaseMapper.updateDisease(diseaseDto);
		
			// DB에 입력 실패
			if (result <= 0) {
				
				return DISEASE_MODIFY_FAIL;
				
			// DB에 입력 실패
			} else {
				
				return DISEASE_MODIFY_SUCCESS;
				
			}
			
		// 질환이 이미 있다면	
		} else {
			
			return DISEASE_ALREADY;
			
		}
		
	}

	// 질환 삭제 확인
	public int deleteConfirm(int no) {
		log.info("deleteConfirm()");
		
		int result = diseaseMapper.deleteCategory(no);
		
		// DB에 입력 실패
		if (result <= 0) {
			
			return DISEASE_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			
			return DISEASE_DELETE_SUCCESS;
			
		}
		
	}

	// 질환 검색
	public Map<String, Object> searchList(String name) {
		log.info("searchList()");
		
		Map<String, Object> searchDiseaseDtos = new HashMap<>();
		
		List<DiseaseDto> searchDiseaseDto = (List<DiseaseDto>) diseaseMapper.searchDiseaseByName(name);
		
		searchDiseaseDtos.put("searchDiseaseDto", searchDiseaseDto);
		
		return searchDiseaseDtos;
		
	}

	

	



	

	

	
		
	
		
		
		

	
	

}