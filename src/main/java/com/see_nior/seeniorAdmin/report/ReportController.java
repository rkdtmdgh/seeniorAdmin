package com.see_nior.seeniorAdmin.report;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.enums.PagePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

	final private ReportService reportService;
	
	// ------------------------------------------------------------------- 신고 카테고리
	
	// 신고 카테고리 등록 양식
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		return PagePath.REPORT_CREATE_CATEGORY_FORM.getValue();
		
	}
	
	// 신고 카테고리명 중복 확인
	@ResponseBody
	@GetMapping("/cate_info/is_report_category")
	public boolean isReportCategory(@RequestParam(value = "brc_name") String brc_name) {
		
		boolean isReportCategory = reportService.isReportCategory(brc_name);
		
		return isReportCategory;
		
	}
	
	// 신고 카테고리 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		return PagePath.REPORT_CATEGORY_LIST_FORM.getValue();
		
	}
	
	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_report_list_select")
	public Object getReportListSelect() {
		log.info("getReportListSelect()");
		
		Map<String, Object> reportCategoryDtos = reportService.getCategoryList();
		
		return reportCategoryDtos;
		
	}
	
	// 모든 신고 카테고리 가져오기 (페이지네이션 => 신고 카테고리 관리용 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "dc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getCategoryList()");
		
		return null;
		
	}
	
	
	// 신고 카테고리 수정 양식
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(@RequestParam(value = "brc_no") int brc_no, Model model) {
		log.info("modifyCategoryForm()");
		
		return PagePath.REPORT_MODIFY_CATEGORY_FORM.getValue();
		
	}
	
	
	
	
	
	// ------------------------------------------------------------------- 신고
	
	// 신고 리스트 양식
	@GetMapping("/info/report_list_form")
	public String reportListForm() {
		log.info("reportListForm()");
		
		return PagePath.REPORT_LIST_FORM.getValue();
		
	}
	
	// 모든 신고 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/get_report_list")
	public Object getReportList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "br_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getReportList()");
		
		// 페이지 번호에 따른 신고 리스트들 가져오기
		Map<String, Object> reportListWithPage = reportService.getReportListWithPage(page, sortValue, order);
		
		// 신고 총 페이지 개수 가져오기
		Map<String, Object> reportListPageNum = reportService.getReportListPageNum(page);
		
		reportListWithPage.put("reportListPageNum", reportListPageNum);
		reportListWithPage.put("sortValue", sortValue);
		reportListWithPage.put("order", order);
		
		return reportListWithPage;
		
	}
	
	
	
	// ------------------------------------------------------------------- 신고 결과
	
	// 신고 상세 양식
	@GetMapping("/info/result_form")
	public String resultForm(@RequestParam(value = "br_no") int br_no, Model model) {
		log.info("resultForm");
		
		return PagePath.REPORT_RESULT_FORM.getValue();
		
	}
	
	
	
}
