package com.see_nior.seeniorAdmin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.see_nior.seeniorAdmin.account.AccountService;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;
import com.see_nior.seeniorAdmin.dto.ReportDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.report.mapper.ReportMapper;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReportService {
	
	final private ReportMapper reportMapper;
	
	final private AccountService accountService;
	
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
		
		reportCategoryDtos.put("reportCategoryDtos", reportCategoryDto);
		
		return reportCategoryDtos;
		
	}

	// 페이지에 따른 신고 카테고리 리스트 가져오기
	public Map<String, Object> getReportCategoryListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getReportCategoryListWithPage()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<ReportCategoryDto> reportCategoryDtos = reportMapper.getReportCategoryListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		
		pagingCategoryList.put("reportCategoryDtos", reportCategoryDtos);
		
		return pagingCategoryList;
		
	}

	// 신고 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getReportCategoryListPageNum(int page_limit, int block_limit, int page) {
		log.info("getReportCategoryListPageNum()");
		
		// 전체 리스트 개수 조회
		int reportCategoryListCnt = reportMapper.getAllReportCategoryCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "reportCategoryListCnt", reportCategoryListCnt, page);
		
	}

	// 신고 카테고리 한개 가져오기
	public ReportCategoryDto getCategory(int brc_no) {
		log.info("getCategory()");
		
		ReportCategoryDto reportCategoryDto = reportMapper.getReportCategory(brc_no);
		if (reportCategoryDto == null) throw new RuntimeException("reportCategoryDto is null!!");

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
	public Map<String, Object> getSearchReportCategoryListWithPage(int page_limit, String searchPart, String searchString,
			String sortValue, String order, int page) {
		log.info("getSearchReportCategoryListWithPage()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<ReportCategoryDto> searchReportCategoryDtos = reportMapper.getSearchReportCategory(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		
		pagingCategoryList.put("reportCategoryDtos", searchReportCategoryDtos);
		
		return pagingCategoryList;
		
	}

	// 신고 카테고리의 총 페이지 개수 구하기(검색한 신고 카테고리)
	public Map<String, Object> getSearchReportCategoryListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchReportCategoryListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchReportCategoryListCnt = reportMapper.getSearchReportCategoryListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchReportCategoryListCnt", searchReportCategoryListCnt, page);
		
	}

////////////////////////////////////////////////////////// 신고

	// 홈 화면에서 보여질 신고 가져오기
	public Object getReportListForMain(int page_limit) {
		log.info("getReportListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		List<ReportDto> reportDtos = reportMapper.getReportListForMain(page_limit);
		responseMap.put("reportDtos", reportDtos);
		
		return responseMap;
		
	}
	
	// 페이지 번호에 따른 신고 리스트들 가져오기(모든 신고)
	public Map<String, Object> getReportListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getReportListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<ReportDto> reportDtos = reportMapper.getReportListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		pagingList.put("reportDtos", reportDtos);
		
		return pagingList;
		
	}

	// 신고의 총 페이지 개수 구하기 (모든 신고)
	public Map<String, Object> getReportListPageNum(int page_limit, int block_limit, int page) {
		log.info("getReportListPageNum()");
				
		// 전체 리스트 개수 조회
		int reportListCnt = reportMapper.getAllReportCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "reportListCnt", reportListCnt, page);
		
	}

	// 처리 되지 않은 신고의 개수 (모든 신고)
	public int getUnresultedReportCnt() {
		log.info("getUnresultedReportCnt()");
		
		return reportMapper.getUnresultedReportCnt();
		
	}

	// 신고 한개 가져오기
	public ReportDto getReport(int br_no, int br_post_no) {
		log.info("getReport()");
		
		ReportDto reportDto = reportMapper.getReport(br_no);
		if (reportDto == null) throw new RuntimeException("reportDto is null!!");
		else {
			
			if (reportDto.getReportResultDto().isBrr_is_deleted() == false) reportDto.setReportResultDto(null);
			
		}
		
		return reportDto;
		
	}

	// 페이지 번호에 따른 카테고리별 신고 리스트들 가져오기
	public Map<String, Object> getReportListByCategoryWithPage(int page_limit, int page, String sortValue, String order, int brc_no) {
		log.info("getReportListByCategoryWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<ReportDto> reportDtos = reportMapper.getReportListByCategoryWithPage(PagingUtil.pagingParamsForSelectBox(page_limit, sortValue, order, page, brc_no));
		pagingList.put("reportDtos", reportDtos);
		
		return pagingList;
		
	}

	// 신고의 총 페이지 개수 구하기 (카테고리별 신고)
	public Map<String, Object> getReportListByCategoryPageNum(int page_limit, int block_limit, int page, int brc_no) {
		log.info("getReportListPageNum()");
				
		// 전체 리스트 개수 조회
		int reportListCnt = reportMapper.getReportCntByCategory(brc_no);
		
		return PagingUtil.pageNum(page_limit, block_limit, "reportListCnt", reportListCnt, page);
		
	}

	// 처리 되지 않은 신고의 개수 (카테고리별 신고)
	public int getUnresultedReportCntByCategory(int brc_no) {
		log.info("getUnresultedReportCntByCategory()");
		
		return reportMapper.getUnresultedReportCntByCategory(brc_no);
		
	}

	// 페이지에 따른 신고 가져오기 (검색한 신고)
	public Map<String, Object> getSearchReportListWithPage(int page_limit, String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("getSearchReportListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<ReportDto> searchReportDtos = reportMapper.getSearchReport(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		pagingList.put("reportDtos", searchReportDtos);
		
		return pagingList;
		
	}

	// 신고의 총 페이지 개수 구하기 (검색한 신고)
	public Map<String, Object> getSearchReportListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchReportListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchReportListCnt = reportMapper.getSearchReportListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchReportListCnt", searchReportListCnt, page);
		
	}

	// 처리 되지 않은 신고의 개수 (카테고리별 신고)
	public int getUnresultedReportCntBySearch(String searchPart, String searchString) {
		log.info("getUnresultedReportCntByCategory()");
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		return reportMapper.getUnresultedReportCntBySearch(pagingParams);
		
	}
	
////////////////////////////////////////////////////////// 신고 처리

	// 신고 처리 등록 확인
	@Transactional
	public boolean createResultConfirm(int br_no, String br_post_no, int bp_report_state, String brr_result, String a_id) {
		log.info("createResultConfirm()");
		
		Map<String, Object> insertParams = new HashMap<>();
		
		// a_id값으로 a_no 가져오기
		AdminAccountDto loginedAdminDto = accountService.getAdminAccountById(a_id);
		
		insertParams.put("br_no", br_no);
		insertParams.put("brr_result", brr_result);
		insertParams.put("a_no", loginedAdminDto.getA_no());
		
		try {
			
			// 신고 처리 결과 테이블에 신고 처리 결과 저장
			int createReportResult = reportMapper.insertNewReportResult(insertParams);
			
			if (createReportResult <= 0) throw new RuntimeException("신고 처리 결과 테이블에 신고 처리 결과 저장 실패!!");
				
			// BOARD_REPORT_RESULT 테이블에 마지막으로 insert된 컬럼의 NO 가져오기
			int brr_no = reportMapper.getReportResultLastNo();
			
			Map<String, Object> updateParams = new HashMap<>();
			updateParams.put("br_no", br_no);
			updateParams.put("brr_no", brr_no);
			
			// 신고 테이블에 신고 처리 결과 no와 처리 상태 업데이트
			int updateBoardReportWithResult = reportMapper.updateBoardReportWithResult(updateParams);
			
			if (updateBoardReportWithResult <= 0) throw new RuntimeException("신고 테이블에 신고 처리 결과 no와 처리 상태 업데이트 실패!!");
				
			Map<String, Object> updateBoardPostsParams = new HashMap<>();
			updateBoardPostsParams.put("br_post_no", br_post_no);
			updateBoardPostsParams.put("bp_report_state", bp_report_state);
			
			// 게시물 숨김처리 결과(BP_REPORT_STATE)를 게시물 테이블에 업데이트
			int updateBoardPostsWithResult = reportMapper.updateBoardPostsWithResult(updateBoardPostsParams);
			
			if (updateBoardPostsWithResult <= 0) throw new RuntimeException("게시물 테이블에 게시물 블락 처리 결과 업데이트 실패!");
			
			return SqlResult.SUCCESS.getValue();
			
		} catch (Exception e) {
			log.error("에러 발생!!", e);
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}

	// 신고 처리 수정 확인
	@Transactional
	public boolean modifyResultConfirm(int brr_no, String br_post_no, int bp_report_state, String brr_result,
			String a_id) {
		log.info("modifyResultConfirm()");

		Map<String, Object> modifyParams = new HashMap<>();
		
		// a_id값으로 a_no 가져오기
		AdminAccountDto loginedAdminDto = accountService.getAdminAccountById(a_id);
		
		modifyParams.put("brr_no", brr_no);
		modifyParams.put("brr_result", brr_result);
		modifyParams.put("a_no", loginedAdminDto.getA_no());
		
		try {
			// 신고 처리 결과 테이블에 신고 처리 결과 업데이트
			int updateReportResult = reportMapper.updateReportResult(modifyParams);
			
			if (updateReportResult <= 0) throw new RuntimeException("신고 처리 결과 테이블에 신고 처리 결과 저장 실패!!");
				
			Map<String, Object> updateBoardPostsParams = new HashMap<>();
			updateBoardPostsParams.put("br_post_no", br_post_no);
			updateBoardPostsParams.put("bp_report_state", bp_report_state);
			
			// 게시물 숨김처리 결과(BP_REPORT_STATE)를 게시물 테이블에 업데이트
			int updateBoardPostsWithResult = reportMapper.updateBoardPostsWithResult(updateBoardPostsParams);
			
			if (updateBoardPostsWithResult <= 0) throw new RuntimeException("게시물 테이블에 게시물 블락 처리 결과 업데이트 실패!");
			
			return SqlResult.SUCCESS.getValue();
			
		} catch (Exception e) {
			log.error("에러 발생!!", e);
			
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			
			return SqlResult.FAIL.getValue();
		
		}
		
	}

	// 신고 처리 결과 삭제
	public boolean deleteResultConfirm(int brr_no) {
		log.info("deleteResultConfirm()");
		
		int deleteResult = reportMapper.deleteReportResult(brr_no);
		
		if (deleteResult <= 0) return SqlResult.FAIL.getValue();
		
		else return SqlResult.SUCCESS.getValue();
		
	}

}
