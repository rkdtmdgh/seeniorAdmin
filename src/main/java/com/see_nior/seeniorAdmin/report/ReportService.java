package com.see_nior.seeniorAdmin.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;
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
	
	// --------------------------------------------------- 신고 카테고리
	
	// 신고 카테고리명 중복 확인
	public boolean isReportCategory(String brc_name) {
		log.info("isReportCategory()");
		
		boolean isReportCategory = reportMapper.isReportCategory(brc_name);
		
		return isReportCategory;
		
	}

	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> reportCategoryDtos = new HashMap<>();
		
		List<ReportCategoryDto> reportCategoryDto = (List<ReportCategoryDto>) reportMapper.getReportCategoryList();
		
		reportCategoryDtos.put("reportCategoryDto", reportCategoryDto);
		
		return reportCategoryDtos;
		
	}

	// 모든 신고 카테고리 가져오기(페이지네이션 => 신고 카테고리 관리용 => 비동기)
	public Map<String, Object> getReportListWithPage(int page, String sortValue, String order) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

}
