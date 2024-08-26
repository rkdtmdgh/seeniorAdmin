package com.see_nior.seeniorAdmin.disease;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
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
		boolean isDiseaseCategory = diseaseMapper.isDiseaseCategory(diseaseCategoryDto.getDc_name());
		
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
	
	// 페이지에 따른 질환 카테고리 리스트 가져오기
	public Map<String, Object> getCategoryListWithPage(int page, String sort) {
		log.info("getCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("dc_name", sort);
		
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
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		diseaseCategoryListPageNum.put("diseaseCategoryListCnt", diseaseCategoryListCnt);
		diseaseCategoryListPageNum.put("page", page);
		diseaseCategoryListPageNum.put("maxPage", maxPage);
		diseaseCategoryListPageNum.put("startPage", startPage);
		diseaseCategoryListPageNum.put("endPage", endPage);
		diseaseCategoryListPageNum.put("blockLimit", blockLimit);
		diseaseCategoryListPageNum.put("pageLimit", pageLimit);
		
		return diseaseCategoryListPageNum;
	}
	
	// 질환 카테고리 한개 가져오기
	public DiseaseCategoryDto getCategory(int dc_no) {
		log.info("getCategory()");
		
		DiseaseCategoryDto diseaseCategoryDto = diseaseMapper.getDiseaseCategory(dc_no);
		
		return diseaseCategoryDto;
	}

	
	// 질환 카테고리 수정 확인
	public int modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		// 질환 카테고리 중복 여부
		boolean isDiseaseCategory = diseaseMapper.isDiseaseCategory(diseaseCategoryDto.getDc_name());
		
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
	public int deleteCategoryConfirm(int dc_no) {
		log.info("deleteCategoryConfirm()");
		
		int result = diseaseMapper.deleteDiseaseCategory(dc_no);
		
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
		boolean isDisease = diseaseMapper.isDisease(diseaseDto.getD_name());
		
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
	public Map<String, Object> getDiseaseList() {
		log.info("getDiseaseList()");
		
		Map<String, Object> diseaseDtos = new HashMap<>();
		
		List<DiseaseDto> diseaseDto = (List<DiseaseDto>) diseaseMapper.getDiseaseList();
		
		diseaseDtos.put("diseaseDto", diseaseDto);
		
		return diseaseDtos;
	}
	
	// 페이지에 따른 질환 가져오기(모든 질환)
	public Map<String, Object> getDiseaseListWithPage(int page, String sort) {
		log.info("getDiseaseListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sort", sort);
		
		List<DiseaseDto> diseaseDtos = diseaseMapper.getDiseaseListWithPage(pagingParams);
		pagingList.put("diseaseDtos", diseaseDtos);
		
		return pagingList;
		
	}
	
	// 질환의 총 페이지 개수 구하기(모든 질환)
	public Map<String, Object> getDiseaseListPageNum(int page) {
		log.info("getDiseaseListPageNum()");
		
		Map<String, Object> diseaseListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int diseaseListCnt = diseaseMapper.getAllDiseaseCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) diseaseListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		diseaseListPageNum.put("diseaseListCnt", diseaseListCnt);
		diseaseListPageNum.put("page", page);
		diseaseListPageNum.put("maxPage", maxPage);
		diseaseListPageNum.put("startPage", startPage);
		diseaseListPageNum.put("endPage", endPage);
		diseaseListPageNum.put("blockLimit", blockLimit);
		diseaseListPageNum.put("pageLimit", pageLimit);
		
		return diseaseListPageNum;
	}
	
	// 페이지에 따른 질환 가져오기(카테고리별 질환)
	public Map<String, Object> getDiseaseListByCategoryWithPage(int page, int dc_no) {
		log.info("getDiseaseListByCategoryWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("dc_no", dc_no);
		
		List<DiseaseDto> diseaseDtos = diseaseMapper.getDiseaseListByCategoryWithPage(pagingParams);
		pagingList.put("diseaseDtos", diseaseDtos);
		
		return pagingList;
		
	}
	
	// 질환의 총 페이지 개수 구하기(카테고리별 질환)
	public Map<String, Object> getDiseaseListByCategoryPageNum(int page, int dc_no) {
		log.info("getDiseaseListByCategoryPageNum()");
		
		Map<String, Object> diseaseLisByCategoryPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int diseaseListByCategoryCnt = diseaseMapper.getDiseaseByCategoryCnt(dc_no);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) diseaseListByCategoryCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		diseaseLisByCategoryPageNum.put("diseaseListCnt", diseaseListByCategoryCnt);
		diseaseLisByCategoryPageNum.put("page", page);
		diseaseLisByCategoryPageNum.put("maxPage", maxPage);
		diseaseLisByCategoryPageNum.put("startPage", startPage);
		diseaseLisByCategoryPageNum.put("endPage", endPage);
		diseaseLisByCategoryPageNum.put("blockLimit", blockLimit);
		diseaseLisByCategoryPageNum.put("pageLimit", pageLimit);
		
		return diseaseLisByCategoryPageNum;
	}
	
	// 질환 한 개 가져오기(질환 수정 용)
	public DiseaseDto getDisease(int d_no) {
		log.info("getDisease()");
		
		DiseaseDto diseaseDto = diseaseMapper.getDiseaseByNo(d_no);
		
		return diseaseDto;
	}
	
	
	// 질환 수정 확인
	public int modifyConfirm(DiseaseDto diseaseDto) {
		log.info("modifyConfirm");
		
		// 질환명을 변경 하지 않을시에는 질환 중복 여부 검사 하지 않도록 처리
		DiseaseDto curDiseaseDto = diseaseMapper.getDiseaseByNo(diseaseDto.getD_no());
		
		if (curDiseaseDto.getD_name().equals(diseaseDto.getD_name())) {
			
			int result = diseaseMapper.updateDisease(diseaseDto);
			
				// DB에 입력 실패
				if (result <= 0) {
					
					return DISEASE_MODIFY_FAIL;
					
				// DB에 입력 실패
				} else {
					
					return DISEASE_MODIFY_SUCCESS;
					
				}
			
		} else {
			
			// 질환 중복 여부
			boolean isDisease = diseaseMapper.isDisease(diseaseDto.getD_name());

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
		
	}

	// 질환 삭제 확인
	@Transactional
	public boolean deleteConfirm(ArrayList<Integer> d_nos) {
		log.info("deleteConfirm()");
	   
	   try {
		   
		   for (int d_no : d_nos) {
			   diseaseMapper.deleteDisease(d_no);
			   
		   }
		
	   } catch (Exception e) {
		   log.error("Error ==========>",e);
		   
		   return false;
		   
	   }
	   
	   return true;
	
	}

	// 페이지에 따른 질환 가져오기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListWithPage(String searchPart, String searchString, int page) {
		log.info("getSearchDiseaseListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		List<DiseaseDto> searchDiseaseDtos = diseaseMapper.getSearchDisease(pagingParams);
		pagingList.put("diseaseDtos", searchDiseaseDtos);
		
		return pagingList;
		
	}

	// 질환의 총 페이지 개수 구하기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListPageNum(String searchPart, String searchString, int page) {
		log.info("getSearchDiseaseListPageNum()");
		
		Map<String, Object> searchDiseaseListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchDiseaseListCnt = diseaseMapper.getSearchDiseaseListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchDiseaseListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchDiseaseListPageNum.put("searchDiseaseListCnt", searchDiseaseListCnt);
		searchDiseaseListPageNum.put("page", page);
		searchDiseaseListPageNum.put("maxPage", maxPage);
		searchDiseaseListPageNum.put("startPage", startPage);
		searchDiseaseListPageNum.put("endPage", endPage);
		searchDiseaseListPageNum.put("blockLimit", blockLimit);
		searchDiseaseListPageNum.put("pageLimit", pageLimit);
		
		return searchDiseaseListPageNum;
		
	}


	

	

	

	

	



	

	

	
		
	
		
		
		

	
	

}