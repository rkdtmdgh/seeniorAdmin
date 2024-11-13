package com.see_nior.seeniorAdmin.report.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;

@Mapper
public interface ReportMapper {

	// 질환 카테고리 중복체크
	public boolean isReportCategory(String brc_name);

	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	public List<ReportCategoryDto> getReportCategoryList();

}
