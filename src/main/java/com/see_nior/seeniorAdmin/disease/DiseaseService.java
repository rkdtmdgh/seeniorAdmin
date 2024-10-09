package com.see_nior.seeniorAdmin.disease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorAdmin.disease.mapper.DiseaseMapper;
import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class DiseaseService {
	
	// 질환 카테고리
	final static public boolean DISEASE_CATEGORY_CREATE_FAIL = false;	// 질환 카테고리 생성 실패
	final static public boolean DISEASE_CATEGORY_CREATE_SUCCESS = true;	// 질환 카테고리 생성 성공
	final static public boolean DISEASE_CATEGORY_MODIFY_FAIL = false;	// 질환 카테고리 수정 실패
	final static public boolean DISEASE_CATEGORY_MODIFY_SUCCESS = true;	// 질환 카테고리 수정 성공
	final static public boolean DISEASE_CATEGORY_DELETE_FAIL = false;	// 질환 카테고리 삭제 실패
	final static public boolean DISEASE_CATEGORY_DELETE_SUCCESS = true;	// 질환 카테고리 삭제 성공
	
	// 질환
	final static public boolean DISEASE_CREATE_FAIL = false;			// 질환 생성 실패
	final static public boolean DISEASE_CREATE_SUCCESS = true;			// 질환 생성 성공
	final static public boolean DISEASE_MODIFY_FAIL = false;			// 질환 수정 실패
	final static public boolean DISEASE_MODIFY_SUCCESS = true;			// 질환 수정 성공
	final static public boolean DISEASE_DELETE_FAIL = false;			// 질환 삭제 실패
	final static public boolean DISEASE_DELETE_SUCCESS = true;			// 질환 삭제 성공
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	final private DiseaseMapper diseaseMapper;
	
	// --------------------------------------------- 질환 카테고리

	// 질환 카테고리명 중복 확인
	public boolean isDiseaseCategory(String dc_name) {
		log.info("isDiseaseCategory()");
		
		boolean isDiseaseCategory = diseaseMapper.isDiseaseCategory(dc_name);
		
		return isDiseaseCategory;
	}
	
	// 질환 카테고리 추가 확인
	public boolean createCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("createCategoryConfirm()");
		
		int createResult = diseaseMapper.insertNewDiseaseCategory(diseaseCategoryDto);
		
		// DB에 입력 실패
		if (createResult <= 0) {
			
			return DISEASE_CATEGORY_CREATE_FAIL;
			
		// DB에 입력 성공
		} else {
			
			return DISEASE_CATEGORY_CREATE_SUCCESS;
			
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
	public Map<String, Object> getCategoryListWithPage(int page, String sortValue, String order) {
		log.info("getCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<DiseaseCategoryDto> diseaseCategoryDtos = diseaseMapper.getDiseaseCategoryListWithPage(pagingParams);
		
		for (DiseaseCategoryDto diseaseCategoryDto : diseaseCategoryDtos) {
			Map<String, Object> itemCntParam = new HashMap<>();
			int dc_no = diseaseCategoryDto.getDc_no();
			int item_cnt = diseaseMapper.getCategoryItemCnt(dc_no);
			itemCntParam.put("dc_no", dc_no);
			itemCntParam.put("item_cnt", item_cnt);
			diseaseMapper.updateDiseaseCategoryItemCnt(itemCntParam);
			diseaseCategoryDto.setDc_item_cnt(item_cnt);
			
		}
		
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
	public boolean modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("modifyCategoryConfirm()");
			
		int modifyResult = diseaseMapper.updateDiseaseCategory(diseaseCategoryDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) {
			
			return DISEASE_CATEGORY_MODIFY_FAIL;
			
		// DB에 입력 성공
		} else {
			
			return DISEASE_CATEGORY_MODIFY_SUCCESS;
			
		}
	
	}
	
	// 질환 카테고리 삭제 확인
	public boolean deleteCategoryConfirm(int dc_no) {
		log.info("deleteCategoryConfirm()");
		
		int deleteResult = diseaseMapper.deleteDiseaseCategory(dc_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) {
			
			return DISEASE_CATEGORY_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			
			return DISEASE_CATEGORY_DELETE_SUCCESS;
			
		}
		
	}
	
	
	// 페이지에 따른 질환 카테고리 가져오기(검색한 질환 카테고리)
		public Map<String, Object> getSearchDiseaseCategoryListWithPage(String searchPart, String searchString, int page) {
			log.info("getSearchDiseaseCategoryListWithPage()");
			
			int pagingStart = (page - 1) * pageLimit;
			
			Map<String, Object> pagingList = new HashMap<>();
			
			Map<String, Object> pagingParams = new HashMap<>();
			pagingParams.put("start", pagingStart);
			pagingParams.put("limit", pageLimit);
			pagingParams.put("searchPart", searchPart);
			pagingParams.put("searchString", searchString);
			
			List<DiseaseCategoryDto> searchDiseaseCategoryDtos = diseaseMapper.getSearchDiseaseCategory(pagingParams);
			
			for (DiseaseCategoryDto diseaseCategoryDto : searchDiseaseCategoryDtos) {
				Map<String, Object> itemCntParam = new HashMap<>();
				int dc_no = diseaseCategoryDto.getDc_no();
				int item_cnt = diseaseMapper.getCategoryItemCnt(dc_no);
				itemCntParam.put("dc_no", dc_no);
				itemCntParam.put("item_cnt", item_cnt);
				diseaseMapper.updateDiseaseCategoryItemCnt(itemCntParam);
				diseaseCategoryDto.setDc_item_cnt(item_cnt);
				
			}
			
			pagingList.put("diseaseCategoryDtos", searchDiseaseCategoryDtos);
			
			return pagingList;
			
		}

		// 질환 카테고리의 총 페이지 개수 구하기(검색한 질환 카테고리)
		public Map<String, Object> getSearchDiseaseCategoryListPageNum(String searchPart, String searchString, int page) {
			log.info("getSearchDiseaseCategoryListPageNum()");
			
			Map<String, Object> searchDiseaseCategoryListPageNum = new HashMap<>();
			
			Map<String, Object> pagingParams = new HashMap<>();
			pagingParams.put("searchPart", searchPart);
			pagingParams.put("searchString", searchString);
			
			// 전체 리스트 개수 조회
			int searchDiseaseCategoryListCnt = diseaseMapper.getSearchDiseaseCategoryListCnt(pagingParams);
			
			// 전체 페이지 개수 계산
			int maxPage = (int) (Math.ceil((double) searchDiseaseCategoryListCnt / pageLimit));
			
			// 시작 페이지 값 계산
			int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
			
			// 마지막 페이지 값 계산
			int endPage = startPage + blockLimit - 1;
			if (endPage > maxPage) endPage = maxPage;
			
			searchDiseaseCategoryListPageNum.put("searchDiseaseCategoryListCnt", searchDiseaseCategoryListCnt);
			searchDiseaseCategoryListPageNum.put("page", page);
			searchDiseaseCategoryListPageNum.put("maxPage", maxPage);
			searchDiseaseCategoryListPageNum.put("startPage", startPage);
			searchDiseaseCategoryListPageNum.put("endPage", endPage);
			searchDiseaseCategoryListPageNum.put("blockLimit", blockLimit);
			searchDiseaseCategoryListPageNum.put("pageLimit", pageLimit);
			
			return searchDiseaseCategoryListPageNum;
			
		}
	
	
	// --------------------------------------------- 질환
	
	// 질환명 중복 확인
	public boolean isDisease(String d_name, int d_no) {
		log.info("isDisease()");
		
		boolean isDisease;
		
		// 질환 등록 시(수정 폼이 아니라서 d_no가 defalutValue인 0으로 넘어 왔을 시) 중복검사 실행
		if (d_no == 0) {
			
			isDisease = diseaseMapper.isDisease(d_name);
			
		// 질환 수정 시
		} else {
			DiseaseDto curDiseaseDto = diseaseMapper.getDiseaseByNo(d_no);
			
			// 질환명을 변경 하지 않았을 경우 => 중복검사 실행하지 않고 false 반환
			if (curDiseaseDto.getD_name().equals(d_name)) {
				
				isDisease = false;
				
			// 질환명을 변경 했을 경우 중복검사 실행
			} else {
				
				isDisease = diseaseMapper.isDisease(d_name);
				
			}
		
		}
				
		return isDisease;
			
		}
	
	// 질환 추가 확인
	public boolean createConfirm(DiseaseDto diseaseDto) {
		log.info("createConfirm()");
			
		int createResult = diseaseMapper.insertNewDisease(diseaseDto);
		
		// DB에 입력 실패
		if (createResult <= 0) {
			return DISEASE_CREATE_FAIL;
			
		// DB에 입력 성공
		} else {
			
			return DISEASE_CREATE_SUCCESS;
		}
	
		
	}
	
	// 페이지에 따른 질환 가져오기(모든 질환)
	public Map<String, Object> getDiseaseListWithPage(int page, String sortValue, String order) {
		log.info("getDiseaseListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
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
	
	// 질환 한 개 가져오기
	public DiseaseDto getDisease(int d_no) {
		log.info("getDisease()");
		
		DiseaseDto diseaseDto = diseaseMapper.getDiseaseByNo(d_no);
		
		return diseaseDto;
	}
	
	
	// 질환 수정 확인
	public boolean modifyConfirm(DiseaseDto diseaseDto) {
		log.info("modifyConfirm");
			
			int modifyResult = diseaseMapper.updateDisease(diseaseDto);
			
			// DB에 입력 실패
			if (modifyResult <= 0) {
				
				return DISEASE_MODIFY_FAIL;
				
			// DB에 입력 성공
			} else {
				
				return DISEASE_MODIFY_SUCCESS;
				
			}
		
	}

	// 질환 삭제 확인
	@Transactional
	public boolean deleteConfirm(List<Integer> d_nos) {
		log.info("deleteConfirm()");
	   
	   try {
		   
		   for (int d_no : d_nos) {
			   
			   int deleteResult = diseaseMapper.deleteDisease(d_no);
			   
			   if (deleteResult <= 0) {
				   log.info("삭제에 실패하였습니다 : d_no --> {}", d_no);
				   
				   throw new RuntimeException();
				   
			   }
			   
		   }
		
	   } catch (Exception e) {
		   log.error("deleteConfirm Error : {}",e);
		   
		   return DISEASE_DELETE_FAIL;
		   
	   }
	   
	   return DISEASE_DELETE_SUCCESS;
	
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