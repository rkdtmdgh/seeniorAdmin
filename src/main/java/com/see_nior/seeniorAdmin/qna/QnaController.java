package com.see_nior.seeniorAdmin.qna;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.QnaAnswerDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;

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
		qnaList.put("qnaListPageNum", qnaListPage);
		qnaList.put("sortValue", sortValue);
		qnaList.put("order", order);
		
		return qnaList;
		
	}
	
	// qna 검색 리스트 가져오기 
	@GetMapping("/info/search_qna_list")
	@ResponseBody
	public Object searchQnaList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "bq_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchQnaList()");
	
		Map<String, Object> searchQnaList = 
				qnaService.searchQnaPagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchQnaListPageNum = 
				qnaService.searchQnaListPageNum(searchPart, searchString, page);
		
		searchQnaList.put("searchQnaListPageNum", searchQnaListPageNum);
		searchQnaList.put("sortValue", sortValue);
		searchQnaList.put("order", order);
		searchQnaList.put("searchPart", searchPart);
		searchQnaList.put("searchString", searchString);
		
		return searchQnaList;
	}
	
	// qna 상세보기 양식
	@GetMapping("/info/qna_detail_form")
	public String qnaDetailForm(@RequestParam("bq_no") int bq_no, Model model) {
		log.info("qnaDetailForm()");
	
		String nextPage = "qna/qna_detail_form";
		
		QnaDto qnaDto = qnaService.getQnaInfoByNo(bq_no);
		List<QnaAnswerDto> QnaAnswerDtos = qnaService.getQnaAnswerInfosByBqNo(bq_no);
		model.addAttribute("qnaDto", qnaDto);
		model.addAttribute("QnaAnswerDtos", QnaAnswerDtos);
		
		log.info("qnaDto ---- {}", qnaDto);
		log.info("QnaAnswerDtos ---- {}", QnaAnswerDtos);
		
		return nextPage;
		
	}
	
	// qna 답변 확인
	@PostMapping("/info/qna_answer_confirm")
	@ResponseBody
	public Object qnaAnswerConfirm(
			@RequestParam("bq_no") int bq_no, 
			@RequestParam("bqa_answer") String bqa_answer, 
			Principal principal) {
		log.info("qnaAnswerConfirm()");
		
		return qnaService.qnaAnswerConfirm(bq_no, bqa_answer, principal.getName());
	}
	
	
	
}
