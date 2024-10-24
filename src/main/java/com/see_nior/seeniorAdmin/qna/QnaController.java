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
import com.see_nior.seeniorAdmin.enums.PagePath;

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
		
		return PagePath.QNA_LIST_FORM.getValue();
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
	
	// qna 답변하기 양식
	@GetMapping("/info/answer_form")
	public String answerForm(@RequestParam("bq_no") int bq_no, Model model) {
		log.info("answerForm()");
	
		QnaDto qnaDto = qnaService.getQnaInfoByNo(bq_no);
		List<QnaAnswerDto> QnaAnswerDtos = qnaService.getQnaAnswerInfosByBqNo(bq_no);
		model.addAttribute("qnaDto", qnaDto);
		model.addAttribute("QnaAnswerDtos", QnaAnswerDtos);
		
		return PagePath.QNA_ANSWER_FORM.getValue();
		
	}
	
	// qna 답변 확인
	@PostMapping("/info/answer_confirm")
	@ResponseBody
	public Object qnaAnswerConfirm(
			@RequestParam("bq_no") int bq_no, 
			@RequestParam("bqa_answer") String bqa_answer, 
			Principal principal) {
		log.info("qnaAnswerConfirm()");
		
		return qnaService.qnaAnswerConfirm(bq_no, bqa_answer, principal.getName());
		
	}
	
	// qna 답변 수정 확인
	@PostMapping("/info/answer_modify_confirm")
	@ResponseBody
	public Object answerModifyConfirm(
			@RequestParam("bqa_no") int bqa_no, 
			@RequestParam("bqa_answer") String bqa_answer, 
			Principal principal) {
		log.info("answerModifyConfirm()");
		
		return qnaService.answerModifyConfirm(bqa_no, bqa_answer);
		
	}
	
	// qna 공지사항 리스트 양식
	@GetMapping("/info/notice_list_form")
	public String qnaNoticeListForm() {
		log.info("qnaNoticeListForm()");
		
		return PagePath.QNA_NOTICE_LIST_FORM.getValue();
		
	}
	
	// qna 공지사항 리스트 가져오기
	@GetMapping("/info/get_notice_list")
	@ResponseBody
	public Object getNoticeList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "bqn_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getNoticeList()");
		
		Map<String, Object> qnaNoticeList = qnaService.getQnaNoticePagingList(sortValue, order, page);
		
		Map<String, Object> qnaNoticeListPageNum = qnaService.getQnaNoticeListPageNum(page);
		qnaNoticeList.put("qnaNoticeListPageNum", qnaNoticeListPageNum);
		qnaNoticeList.put("sortValue", sortValue);
		qnaNoticeList.put("order", order);
		
		return qnaNoticeList;
		
	}
	
	// qna 공지사항 검색 리스트 가져오기
	
	
	// qna 공지사항 등록 양식
	@GetMapping("/info/notice_create_form")
	public String noticeCreateForm() {
		log.info("noticeCreateForm()");
		
		return PagePath.QNA_NOTICE_CREATE_FORM.getValue();
		
	}
	
	// qna 공지사항 수정 양식
	
	// qna 공지사항 수정 확인
	
	// qna 공지사항 삭제 확인
	
	
	
}
