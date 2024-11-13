package com.see_nior.seeniorAdmin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;
import com.see_nior.seeniorAdmin.dto.ReportDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.report.mapper.ReportMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportService {
	
	// 신고 카테고리
	final static public boolean REPORT_CATEGORY_CREATE_FAIL = false;	// 신고 카테고리 생성 실패
	final static public boolean REPORT_CATEGORY_CREATE_SUCCESS = true;	// 신고 카테고리 생성 성공
	final static public boolean REPORT_CATEGORY_MODIFY_FAIL = false;	// 신고 카테고리 수정 실패
	final static public boolean REPORT_CATEGORY_MODIFY_SUCCESS = true;	// 신고 카테고리 수정 성공
	final static public boolean REPORT_CATEGORY_DELETE_FAIL = false;	// 신고 카테고리 삭제 실패
	final static public boolean REPORT_CATEGORY_DELETE_SUCCESS = true;	// 신고 카테고리 삭제 성공
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	final private ReportMapper reportMapper;
	
////////////////////////////////////////////////////////// 신고 카테고리
	
	// 신고 카테고리명 중복 확인
	public boolean isReportCategory(String brc_name) {
		log.info("isReportCategory()");
		
		boolean isReportCategory = reportMapper.isReportCategory(brc_name);
		
		return isReportCategory;
		
	}
	
	// 신고 카테고리 추가 확인
	public boolean createCategoryConfirm(ReportCategoryDto reportCategoryDto) {
		log.info("createCategoryConfirm()");
		
		int createResult = reportMapper.insertNewReportCategory(reportCategoryDto);
		
		// DB에 입력 실패
		if (createResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
			
	}

	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> reportCategoryDtos = new HashMap<>();
		
		List<ReportCategoryDto> reportCategoryDto = (List<ReportCategoryDto>) reportMapper.getReportCategoryList();
		
		reportCategoryDtos.put("reportCategoryDto", reportCategoryDto);
		
		return reportCategoryDtos;
		
	}

	// 페이지에 따른 신고 카테고리 리스트 가져오기
	public Map<String, Object> getReportCategoryListWithPage(int page, String sortValue, String order) {
		log.info("getReportCategoryListWithPage()");

		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<ReportCategoryDto> reportCategoryDtos = reportMapper.getReportCategoryListWithPage(pagingParams);
		
		pagingList.put("reportCategoryDtos", reportCategoryDtos);
		
		return pagingList;
		
	}

	// 신고 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getReportCategoryListPageNum(int page) {
		log.info("getReportCategoryListPageNum()");
		
		Map<String, Object> reportCategoryListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int reportCategoryListCnt = reportMapper.getAllReportCategoryCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) reportCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산 
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		reportCategoryListPageNum.put("reportCategoryListCnt", reportCategoryListCnt);
		reportCategoryListPageNum.put("page", page);
		reportCategoryListPageNum.put("maxPage", maxPage);
		reportCategoryListPageNum.put("startPage", startPage);
		reportCategoryListPageNum.put("endPage", endPage);
		reportCategoryListPageNum.put("blockLimit", blockLimit);
		reportCategoryListPageNum.put("pageLimit", pageLimit);
		
		return reportCategoryListPageNum;
		
	}

	// 신고 카테고리 한개 가져오기
	public ReportCategoryDto getCategory(int brc_no) {
		log.info("getCategory()");
		
		ReportCategoryDto reportCategoryDto = reportMapper.getReportCategory(brc_no);

		return reportCategoryDto;
		
	}

	// 신고 카테고리 수정 확인
	public boolean modifyCategoryConfirm(ReportCategoryDto reportCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		int modifyResult = reportMapper.updateReportCategory(reportCategoryDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		return SqlResult.SUCCESS.getValue();
		
	}

	// 신고 카테고리 삭제 확인
	public boolean deleteCategoryConrifm(int brc_no) {
		log.info("deleteCategoryConrifm()");
		
		int deleteResult = reportMapper.deleteReportCategory(brc_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();
		
	}

	// 페이지에 따른 신고 카테고리 가져오기(검색한 신고 카테고리)
	public Map<String, Object> getSearchReportCategoryListWithPage(String searchPart, String searchString,
			String sortValue, String order, int page) {
		log.info("getSearchReportCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<ReportCategoryDto> searchReportCategoryDtos = reportMapper.getSearchReportCategory(pagingParams);
		
		pagingList.put("reportCategoryDtos", searchReportCategoryDtos);
		
		return pagingList;
		
	}

	// 신고 카테고리의 총 페이지 개수 구하기(검색한 신고 카테고리)
	public Map<String, Object> getSearchReportCategoryListPageNum(String searchPart, String searchString, int page) {
		log.info("getSearchReportCategoryListPageNum()");
		
		Map<String, Object> searchReportCategoryListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchReportCategoryListCnt = reportMapper.getSearchReportCategoryListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchReportCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchReportCategoryListPageNum.put("searchReportCategoryListCnt", searchReportCategoryListCnt);
		searchReportCategoryListPageNum.put("page", page);
		searchReportCategoryListPageNum.put("maxPage", maxPage);
		searchReportCategoryListPageNum.put("startPage", startPage);
		searchReportCategoryListPageNum.put("endPage", endPage);
		searchReportCategoryListPageNum.put("blockLimit", blockLimit);
		searchReportCategoryListPageNum.put("pageLimit", pageLimit);
		
		return searchReportCategoryListPageNum;
		
	}

////////////////////////////////////////////////////////// 신고

	// 페이지 번호에 따른 신고 리스트들 가져오기(모든 신고)
	public Map<String, Object> getReportListWithPage(int page, String sortValue, String order) {
		log.info("getReportListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<ReportDto> reportDtos = reportMapper.getReportListWithPage(pagingParams);
		pagingList.put("reportDtos", reportDtos);
		
		return pagingList;
		
	}
	
	

}
