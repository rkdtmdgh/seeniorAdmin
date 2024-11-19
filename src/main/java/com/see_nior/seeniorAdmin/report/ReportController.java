package com.see_nior.seeniorAdmin.report;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.ReportCategoryDto;
import com.see_nior.seeniorAdmin.dto.ReportDto;
import com.see_nior.seeniorAdmin.enums.PagePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

	final private ReportService reportService;
	
///////////////////////////////////////////////////////////////////////// 신고 카테고리
	
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
	
	// 신고 카테고리 등록 확인
	@ResponseBody
	@PostMapping("/cate_info/create_category_confirm")
	public boolean createCategoryConfirm(ReportCategoryDto reportCategoryDto) {
		log.info("createCategoryConfirm()");
		
		boolean createCategoryResult = reportService.createCategoryConfirm(reportCategoryDto);
		
		return createCategoryResult;
		
	}
	
	// 신고 카테고리 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		return PagePath.REPORT_CATEGORY_LIST_FORM.getValue();
		
	}
	
	// 모든 신고 카테고리 가져오기 (신고 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list_select")
	public Object getReportListSelect() {
		log.info("getReportListSelect()");
		
		Map<String, Object> reportCategoryDtos = reportService.getCategoryList();
		
		return reportCategoryDtos;
		
	}
	
	// 모든 신고 카테고리 가져오기 (페이지네이션 => 신고 카테고리 관리용 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "brc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getCategoryList()");
		
		// 페이지 번호에 따른 신고 카테고리 리스트들 가져오기
		Map<String, Object> reportCategoryListWithPage = reportService.getReportCategoryListWithPage(page_limit, sortValue, order, page);
		
		// 질환 카테고리 총 페이지 개수 가져오기
		Map<String, Object> reportCategoryListPageNum = reportService.getReportCategoryListPageNum(page_limit, block_limit, page);
		
		reportCategoryListWithPage.put("reportCategoryListPageNum", reportCategoryListPageNum);
		reportCategoryListWithPage.put("sortValue", sortValue);
		reportCategoryListWithPage.put("order", order);
		
		return reportCategoryListWithPage;
		
	}
	
	// 신고 카테고리 수정 양식
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(@RequestParam(value = "brc_no") int brc_no, Model model) {
		log.info("modifyCategoryForm()");
		
		ReportCategoryDto reportCategoryDto = reportService.getCategory(brc_no);
		
		model.addAttribute("reportCategoryDto" ,reportCategoryDto);
		
		return PagePath.REPORT_MODIFY_CATEGORY_FORM.getValue();
		
	}
	
	// 신고 카테고리 수정 확인 (비동기)
	@ResponseBody
	@PostMapping("/cate_info/modify_category_confirm")
	public boolean modifyCategoryConfirm(ReportCategoryDto reportCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		boolean modifyCategoryResult = reportService.modifyCategoryConfirm(reportCategoryDto);
		
		return modifyCategoryResult;
		
	}
	
	// 신고 카테고리 삭제 확인(비동기)
	@ResponseBody
	@PostMapping("/cate_info/delete_category_confirm")
	public boolean deleteCategoryConfirm(@RequestParam(value = "brc_no") int brc_no) {
		log.info("deleteCategoryConfirm()");
		
		boolean deleteCategoryResult = reportService.deleteCategoryConrifm(brc_no);
		
		return deleteCategoryResult;
		
	}
	
	// 신고 카테고리 검색(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/search_category_list")
	public Object searchReportCategoryList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "brc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchReportCategoryList()");
		
		// 페이지 번호에 따른 검색 신고 카테고리 리스트들 가져오기
		Map<String, Object> searchReportCategoryListWithPage = reportService.getSearchReportCategoryListWithPage(page_limit, searchPart, searchString, sortValue, order, page);
		
		// 검색 신고 카테고리 총 페이지 개수 가져오기
		Map<String, Object> searchReportCategoryListPageNum = reportService.getSearchReportCategoryListPageNum(page_limit, block_limit, searchPart, searchString, page);
		
		searchReportCategoryListWithPage.put("searchReportCategoryListPageNum", searchReportCategoryListPageNum);
		searchReportCategoryListWithPage.put("searchPart", searchPart);
		searchReportCategoryListWithPage.put("searchString", searchString);
		searchReportCategoryListWithPage.put("sortValue", sortValue);
		searchReportCategoryListWithPage.put("order", order);
		
		return searchReportCategoryListWithPage;
		
	}
	
///////////////////////////////////////////////////////////////////////// 신고
	
	// 신고 리스트 양식
	@GetMapping("/info/report_list_form")
	public String reportListForm() {
		log.info("reportListForm()");
		
		return PagePath.REPORT_LIST_FORM.getValue();
		
	}
	
	// 홈 화면에서 보여질 신고 가져오기(비동기)
	@ResponseBody
	@GetMapping("/main/get_report_list")
	public Object getReportListForMain(@RequestParam(value = "page_limit") int page_limit) {
		log.info("getReportListForMain()");
		
		return reportService.getReportListForMain(page_limit);
		
	}
	
	// 모든 신고 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/get_report_list")
	public Object getReportList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "br_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getReportList()");
		
		// 페이지 번호에 따른 신고 리스트들 가져오기
		Map<String, Object> reportListWithPage = reportService.getReportListWithPage(page_limit, sortValue, order, page);
		
		// 처리 안된 신고의 개수 가져오기
		int unresultedReportCnt = reportService.getUnresultedReportCnt();
		
		// 신고 총 페이지 개수 가져오기
		Map<String, Object> reportListPageNum = reportService.getReportListPageNum(page_limit, block_limit, page);
		
		reportListWithPage.put("reportListPageNum", reportListPageNum);
		reportListWithPage.put("sortValue", sortValue);
		reportListWithPage.put("order", order);
		reportListWithPage.put("unresultedReportCnt", unresultedReportCnt);
		
		return reportListWithPage;
		
	}
	
	// 카테고리에 따른 신고 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/get_report_list_by_category")
	public Object getReportListByCategory(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "br_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "infoNo") int brc_no) {
		log.info("getReportListByCategory()");
		
		// 페이지 번호에 따른 카테고리별 질환 리스트들 가져오기
		Map<String, Object> reportListByCategoryWithPage = reportService.getReportListByCategoryWithPage(page_limit, page, sortValue, order, brc_no);
		
		// 처리 안된 신고의 개수 가져오기(카테고리별)
		int unresultedReportCntByCategory = reportService.getUnresultedReportCntByCategory(brc_no);
		
		// 카테고리별 질환 총 페이지 개수 가져오기
		Map<String, Object> reportListByCategoryPageNum = reportService.getReportListByCategoryPageNum(page_limit, block_limit, page, brc_no);
		
		reportListByCategoryWithPage.put("reportListByCategoryPageNum", reportListByCategoryPageNum);
		reportListByCategoryWithPage.put("sortValue", sortValue);
		reportListByCategoryWithPage.put("order", order);
		reportListByCategoryWithPage.put("infoNo", brc_no);
		reportListByCategoryWithPage.put("unresultedReportCntByCategory", unresultedReportCntByCategory);
		
		return reportListByCategoryWithPage;
		
	} 
	
	// 신고 검색 (페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/info/search_report_list")
	public Object searchReportList(
			@RequestParam(value = "page_limit") int page_limit,
			@RequestParam(value = "block_limit") int block_limit,
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "br_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchReportList()");
		
		// 페이지 번호에 따른 검색 신고 리스트들 가져오기
		Map<String, Object> searchReportListWithPage = reportService.getSearchReportListWithPage(page_limit, searchPart, searchString, sortValue, order, page);
		
		// 처리 안된 신고의 개수 가져오기(검색한 신고)
		int unresultedReportCntBySearch = reportService.getUnresultedReportCntBySearch(searchPart, searchString);
		
		// 검색한 질환 총 페이지 개수 가져오기
		Map<String, Object> searchReportListPageNum = reportService.getSearchReportListPageNum(page_limit, block_limit, searchPart, searchString, page);
		
		searchReportListWithPage.put("searchReportListPageNum", searchReportListPageNum);
		searchReportListWithPage.put("searchPart", searchPart);
		searchReportListWithPage.put("searchString", searchString);
		searchReportListWithPage.put("sortValue", sortValue);
		searchReportListWithPage.put("order", order);
		searchReportListWithPage.put("unresultedReportCntBySearch", unresultedReportCntBySearch);
		
		return searchReportListWithPage;
		
	}
	
////////////////////////////////////////////////////////// 신고 처리
	
	// 신고 처리 확인
	@ResponseBody
	@GetMapping("/info/result_confirm")
	public boolean reportResultConfirm(
			@RequestParam(value = "br_no") int br_no,
			@RequestParam(value = "br_post_no") String br_post_no,
			@RequestParam(value = "bp_report_state") int bp_report_state,
			@RequestParam(value = "brr_result") String brr_result,
			Principal principal) {
		log.info("reportResultConfirm()");
		
		boolean reportResultConfirm = reportService.reportResultConfirm(br_no, br_post_no, brr_result, principal.getName());
		
		return reportResultConfirm;
		
	}
	
	// 신고 처리하기 양식
	@GetMapping("/info/result_form")
	public String resultForm(
			@RequestParam(value = "br_no") int br_no, 
			@RequestParam(value = "br_post_no") int br_post_no, 
			Model model) {
		log.info("resultForm");
		
		ReportDto reportDto = reportService.getReport(br_no, br_post_no);
		model.addAttribute("reportDto", reportDto);
		
		return PagePath.REPORT_RESULT_FORM.getValue();
		
	}
	
	// 신고 처리 수정하기
	
	
	// 신고 처리 삭제하기

	
	
	
}
