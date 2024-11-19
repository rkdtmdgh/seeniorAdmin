package com.see_nior.seeniorAdmin.disease;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorAdmin.disease.mapper.DiseaseMapper;
import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class DiseaseService {
	
	final private DiseaseMapper diseaseMapper;
	
////////////////////////////////////////////////////////// 질환 카테고리

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
		if (createResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}
	
	// 모든 질환 카테고리 가져오기 (질환 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> diseaseCategoryDtos = new HashMap<>();
		
		List<DiseaseCategoryDto> diseaseCategoryDto = (List<DiseaseCategoryDto>) diseaseMapper.getDiseaseCategoryList();
		
		diseaseCategoryDtos.put("diseaseCategoryDtos", diseaseCategoryDto);
		
		return diseaseCategoryDtos;
		
	}
	
	// 페이지에 따른 질환 카테고리 리스트 가져오기
	public Map<String, Object> getDiseaseCategoryListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getDiseaseCategoryListWithPage()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<DiseaseCategoryDto> diseaseCategoryDtos = diseaseMapper.getDiseaseCategoryListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		
		pagingCategoryList.put("diseaseCategoryDtos", diseaseCategoryDtos);
		
		return pagingCategoryList;
		
	}
	
	// 질환 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getDiseaseCategoryListPageNum(int page_limit, int block_limit, int page) {
		log.info("getDiseaseCategoryListPageNum()");
		
		// 전체 리스트 개수 조회
		int diseaseCategoryListCnt = diseaseMapper.getAllDiseaseCategoryCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "diseaseCategoryListCnt", diseaseCategoryListCnt, page);
		
	}
	
	// 질환 카테고리 한개 가져오기
	public DiseaseCategoryDto getCategory(int dc_no) {
		log.info("getCategory()");
		
		DiseaseCategoryDto diseaseCategoryDto = diseaseMapper.getDiseaseCategory(dc_no);
		if (diseaseCategoryDto == null) throw new RuntimeException("diseaseCategoryDto is null!!");
		
		return diseaseCategoryDto;
	}

	
	// 질환 카테고리 수정 확인
	public boolean modifyCategoryConfirm(DiseaseCategoryDto diseaseCategoryDto) {
		log.info("modifyCategoryConfirm()");
			
		int modifyResult = diseaseMapper.updateDiseaseCategory(diseaseCategoryDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
	
	}
	
	// 질환 카테고리 삭제 확인
	public boolean deleteCategoryConfirm(int dc_no) {
		log.info("deleteCategoryConfirm()");
		
		int deleteResult = diseaseMapper.deleteDiseaseCategory(dc_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
	}
	
	
	// 페이지에 따른 질환 카테고리 가져오기(검색한 질환 카테고리)
	public Map<String, Object> getSearchDiseaseCategoryListWithPage(int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("getSearchDiseaseCategoryListWithPage()");
		
		Map<String, Object> pagingSearchCategoryList = new HashMap<>();
		
		List<DiseaseCategoryDto> searchDiseaseCategoryDtos = diseaseMapper.getSearchDiseaseCategory(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		
		pagingSearchCategoryList.put("diseaseCategoryDtos", searchDiseaseCategoryDtos);
		
		return pagingSearchCategoryList;
		
	}

	// 질환 카테고리의 총 페이지 개수 구하기(검색한 질환 카테고리)
	public Map<String, Object> getSearchDiseaseCategoryListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchDiseaseCategoryListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchDiseaseCategoryListCnt = diseaseMapper.getSearchDiseaseCategoryListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchDiseaseCategoryListCnt", searchDiseaseCategoryListCnt, page);
		
	}
	
	
////////////////////////////////////////////////////////// 질환
	
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
		if (createResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
	}
	
	// 페이지에 따른 질환 가져오기(모든 질환)
	public Map<String, Object> getDiseaseListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getDiseaseListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<DiseaseDto> diseaseDtos = diseaseMapper.getDiseaseListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		pagingList.put("diseaseDtos", diseaseDtos);
		
		return pagingList;
		
	}
	
	// 질환의 총 페이지 개수 구하기(모든 질환)
	public Map<String, Object> getDiseaseListPageNum(int page_limit, int block_limit, int page) {
		log.info("getDiseaseListPageNum()");
		
		// 전체 리스트 개수 조회
		int diseaseListCnt = diseaseMapper.getAllDiseaseCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "diseaseListCnt", diseaseListCnt, page);
		
	}
	
	// 페이지에 따른 질환 가져오기(카테고리별 질환)
	public Map<String, Object> getDiseaseListByCategoryWithPage(int page_limit, int page, String sortValue, String order, int dc_no) {
		log.info("getDiseaseListByCategoryWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<DiseaseDto> diseaseDtos = diseaseMapper.getDiseaseListByCategoryWithPage(PagingUtil.pagingParamsForSelectBox(page_limit, sortValue, order, page, dc_no));
		
		pagingList.put("diseaseDtos", diseaseDtos);
		
		return pagingList;
		
	}
	
	// 질환의 총 페이지 개수 구하기(카테고리별 질환)
	public Map<String, Object> getDiseaseListByCategoryPageNum(int page_limit, int block_limit, int page, int dc_no) {
		log.info("getDiseaseListByCategoryPageNum()");
		
		// 전체 리스트 개수 조회
		int diseaseListByCategoryCnt = diseaseMapper.getDiseaseByCategoryCnt(dc_no);
		
		return PagingUtil.pageNum(page_limit, block_limit, "diseaseListByCategoryCnt", diseaseListByCategoryCnt, page);
	
	}
	
	// 질환 한 개 가져오기
	public DiseaseDto getDisease(int d_no) {
		log.info("getDisease()");
		
		DiseaseDto diseaseDto = diseaseMapper.getDiseaseByNo(d_no);
		if (diseaseDto == null) throw new RuntimeException("diseaseDto is null!!");
		
		return diseaseDto;
	}
	
	
	// 질환 수정 확인
	public boolean modifyConfirm(DiseaseDto diseaseDto) {
		log.info("modifyConfirm");
			
		int modifyResult = diseaseMapper.updateDisease(diseaseDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
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
		   
		   return SqlResult.FAIL.getValue();
		   
	   }
	   
	   return SqlResult.SUCCESS.getValue();
	
	}

	// 페이지에 따른 질환 가져오기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListWithPage(int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("getSearchDiseaseListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<DiseaseDto> searchDiseaseDtos = diseaseMapper.getSearchDisease(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		pagingList.put("diseaseDtos", searchDiseaseDtos);
		
		return pagingList;
		
	}

	// 질환의 총 페이지 개수 구하기(검색한 질환)
	public Map<String, Object> getSearchDiseaseListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchDiseaseListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchDiseaseListCnt = diseaseMapper.getSearchDiseaseListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchDiseaseListCnt", searchDiseaseListCnt, page);
		
	}
	
}