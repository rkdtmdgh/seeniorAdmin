package com.see_nior.seeniorAdmin.report;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	// 신고 카테고리 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		return PagePath.REPORT_CATEGORY_LIST_FORM.getValue();
		
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
	
	
	
	
	// ------------------------------------------------------------------- 신고 결과
	
	// 신고 상세 양식
	@GetMapping("/info/result_form")
	public String resultForm(@RequestParam(value = "br_no") int br_no, Model model) {
		log.info("resultForm");
		
		return PagePath.REPORT_RESULT_FORM.getValue();
		
	}
	
	
	
}
