package com.see_nior.seeniorAdmin.report.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;
import com.see_nior.seeniorAdmin.dto.ReportDto;

@Mapper
public interface ReportMapper {
	
////////////////////////////////////////////////////////// 신고 카테고리
	
	// 신고 카테고리 등록
	public int insertNewReportCategory(ReportCategoryDto reportCategoryDto);

	// 신고 카테고리 중복체크
	public boolean isReportCategory(String brc_name);

	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	public List<ReportCategoryDto> getReportCategoryList();

	// 모든 신고 카테고리 가져오기 (페이지네이션 => 신고 카테고리 관리용 => 비동기)
	public List<ReportCategoryDto> getReportCategoryListWithPage(Map<String, Object> pagingParams);

	// 신고 카테고리의 총 리스트 개수 구하기
	public int getAllReportCategoryCnt();

	// 신고 카테고리 한개 가져오기
	public ReportCategoryDto getReportCategory(int brc_no);

	// 신고 카테고리 수정
	public int updateReportCategory(ReportCategoryDto reportCategoryDto);

	// 신고 카테고리 삭제
	public int deleteReportCategory(int brc_no);

	// 페이지에 따른 신고 카테고리 가져오기(검색한 신고 카테고리)
	public List<ReportCategoryDto> getSearchReportCategory(Map<String, Object> pagingParams);

	// 신고 카테고리의 총 페이지 개수 구하기(검색한 신고 카테고리)
	public int getSearchReportCategoryListCnt(Map<String, Object> pagingParams);

////////////////////////////////////////////////////////// 신고
	
	// 페이지 번호에 따른 신고 리스트들 가져오기 (모든 신고)
	public List<ReportDto> getReportListWithPage(Map<String, Object> pagingParams);
	

}
