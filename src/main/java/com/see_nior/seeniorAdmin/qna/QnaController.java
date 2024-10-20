package com.see_nior.seeniorAdmin.qna;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

	final private QnaService qnaService;
	
	// qna 리스트 양식 
	@GetMapping("/info/qna_list_form")
	public String qnaListForm() {
		log.info("qnaListForm()");
		
		String nextPage = "qna/qna_list_form";
		
		return nextPage;
	}
	
	// qna 리스트 가져오기 (비동기)
	@GetMapping("/info/get_qna_list")
	@ResponseBody
	public Object getQnaList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "bq_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getQnaList()");
	
		Map<String, Object> qnaList = qnaService.getQnaPagingList(sortValue, order, page);
		
		Map<String, Object> qnaListPage = qnaService.getQnaListPageNum(page);
		qnaList.put("qnaListPage", qnaListPage);
		qnaList.put("sortValue", sortValue);
		qnaList.put("order", order);
		
		return qnaList;
		
	}
	
	// qna 상세보기 양식
	
	
}
