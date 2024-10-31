package com.see_nior.seeniorAdmin.qna;

import java.security.Principal;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.QnaCategoryDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.dto.QnaNoticeDto;
import com.see_nior.seeniorAdmin.enums.PagePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnaController {

	final private QnaService qnaService;
	
	// QnaDto 컬럼 추가에 따른 로직 수정. 
	
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
		
		int unansweredQnaCnt = qnaService.getUnansweredQnaCnt();
		
		Map<String, Object> qnaListPage = qnaService.getQnaListPageNum(page);
		
		qnaList.put("qnaListPageNum", qnaListPage);
		qnaList.put("sortValue", sortValue);
		qnaList.put("order", order);
		qnaList.put("unansweredQnaCnt", unansweredQnaCnt);
		
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
		
		int unansweredSearchQnaCnt = qnaService.getUnansweredSearchQnaCnt(searchPart, searchString);
		
		Map<String, Object> searchQnaListPageNum = 
				qnaService.searchQnaListPageNum(searchPart, searchString, page);
		
		searchQnaList.put("searchQnaListPageNum", searchQnaListPageNum);
		searchQnaList.put("sortValue", sortValue);
		searchQnaList.put("order", order);
		searchQnaList.put("searchPart", searchPart);
		searchQnaList.put("searchString", searchString);
		searchQnaList.put("unansweredSearchQnaCnt", unansweredSearchQnaCnt);
		
		return searchQnaList;
		
	}
	
	// qna 카테고리에 따른 리스트 가져오기
	@GetMapping("/info/get_qna_list_by_category")
	@ResponseBody
	public Object getQnaListByCategory(
			@RequestParam(value = "sortValue", required = false, defaultValue = "bq_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("infoNo") int bqc_no) {
		log.info("getQnaListByCategory()");
		
		Map<String, Object> qnaListByCategoryWithPage = 
				qnaService.getQnaListByCategoryWithPage(page, sortValue, order, bqc_no);
		
		int unansweredCategoryQnaCnt = qnaService.getUnansweredCategoryQnaCnt(bqc_no);
				
		Map<String, Object> qnaListByCategoryPageNum = 
				qnaService.getQnaByCategoryPageNum(page, bqc_no);
		
		qnaListByCategoryWithPage.put("qnaListByCategoryPageNum", qnaListByCategoryPageNum);
		qnaListByCategoryWithPage.put("sortValue", sortValue);
		qnaListByCategoryWithPage.put("order", order);
		qnaListByCategoryWithPage.put("infoNo", bqc_no);
		qnaListByCategoryWithPage.put("unansweredCategoryQnaCnt", unansweredCategoryQnaCnt);
		
		return qnaListByCategoryWithPage;
		
	}
	
	// qna 답변하기 양식
	@GetMapping("/info/answer_form")
	public String answerForm(@RequestParam("bq_no") int bq_no, Model model) {
		log.info("answerForm()");
	
		QnaDto qnaDto = qnaService.getQnaInfoByNo(bq_no);
		model.addAttribute("qnaDto", qnaDto);
		
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
			@RequestParam("a_id") String a_id, 
			Principal principal) {
		log.info("answerModifyConfirm()");
		
		return qnaService.answerModifyConfirm(a_id, principal.getName(), bqa_no, bqa_answer);
		
	}
	
	// qna 질문 삭제 확인
	@PostMapping("/info/delete_confirm")
	@ResponseBody
	public boolean deleteConfirm(@RequestParam("bq_no") int bq_no) {
		log.info("deleteConfirm()");
		
		return qnaService.deleteConfirm(bq_no);
		
	}
	
	// qna 답변 삭제 확인
	@PostMapping("/info/answer_delete_confirm")
	@ResponseBody
	public boolean answerDeleteConfirm(@RequestParam("bq_no") int bq_no, @RequestParam("bqa_no") int bqa_no) {
		log.info("answerDeleteConfirm");
		
		return qnaService.answerDeleteConfirm(bq_no, bqa_no);
		
	}
	
	// qna 질문 공개/비공개 변경 확인
	@PostMapping("/info/modify_qna_state")
	@ResponseBody
	public boolean modifyQnaState(QnaDto qnaDto) {
		log.info("modifyQnaState()");
		
		return qnaService.modifyQnaState(qnaDto);
		
	}
	
	
	
	
////////////////////////// 카테고리 
	
	// qna 카테고리 등록 양식
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		return PagePath.QNA_CREATE_CATEGORY_FORM.getValue();
		
	}
	
	// qna 카테고리명 중복 확인
	@GetMapping("/cate_info/is_qna_category")
	@ResponseBody
	public boolean isQnaCategory(@RequestParam("bqc_name") String bqc_name) {
		log.info("isQnaCategory()");
		
		return qnaService.isQnaCategory(bqc_name);
		
	}
	
	// qna 카테고리 등록 확인
	@PostMapping("/cate_info/create_category_confirm")
	@ResponseBody
	public boolean createCategoryConfirm(@RequestParam("bqc_name") String bqc_name) {
		log.info("createCategoryConfirm()");
		
		return qnaService.createCategoryConfirm(bqc_name);
		
	}
	
	// qna 카테고리 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		return PagePath.QNA_CATEGORY_LIST_FORM.getValue();
		
	}
	
	// qna 카테고리 리스트 가져오기 (select box 용)
	@GetMapping("/cate_info/get_category_list_select")
	@ResponseBody
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		return qnaService.getCategoryListSelect();
		
	}
	
	// qna 카테고리 모든 리스트 가져오기
	@GetMapping("/cate_info/get_category_list")
	@ResponseBody
	public Object getCategoryList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "bqc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getCategoryList()");
		
		Map<String, Object> qnaCategoryList = qnaService.getQnaCategoryPagingList(sortValue, order, page);
		
		Map<String, Object> qnaCategoryListPage = qnaService.getQnaCategoryListPageNum(page);
		
		qnaCategoryList.put("qnaCategoryListPageNum", qnaCategoryListPage);
		qnaCategoryList.put("sortValue", sortValue);
		qnaCategoryList.put("order", order);
		
		return qnaCategoryList;
		
	}
	
	// qna 카테고리 검색 리스트 가져오기
	@GetMapping("/cate_info/search_category_list")
	@ResponseBody
	public Object searchCategoryList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "bqc_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchCategoryList()");
	
		Map<String, Object> searchQnaCategoryList = 
				qnaService.searchQnaCategoryPagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchQnaCategoryListPageNum = 
				qnaService.searchQnaCategoryListPageNum(searchPart, searchString, page);
		
		searchQnaCategoryList.put("searchQnaCategoryListPageNum", searchQnaCategoryListPageNum);
		searchQnaCategoryList.put("sortValue", sortValue);
		searchQnaCategoryList.put("order", order);
		searchQnaCategoryList.put("searchPart", searchPart);
		searchQnaCategoryList.put("searchString", searchString);
		
		return searchQnaCategoryList;
		
	}
	
	// qna 카테고리 수정 양식
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(@RequestParam("bqc_no") int bqc_no, Model model) {
		log.info("modifyCategoryForm()");
		
		QnaCategoryDto qnaCategoryDto = 
				qnaService.getQnaCategoryDtoByNo(bqc_no);
		
		model.addAttribute("qnaCategoryDto", qnaCategoryDto);
		
		return PagePath.QNA_MODIFY_CATEGORY_FORM.getValue();
		
	}
	
	// qna 카테고리 수정 확인
	@PostMapping("/cate_info/modify_category_confirm")
	@ResponseBody
	public boolean modifyCategoryConfirm(QnaCategoryDto qnaCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		return qnaService.modifyCategoryConfirm(qnaCategoryDto);
		
	}
	
	// qna 카테고리 삭제 확인
	@PostMapping("/cate_info/delete_category_confirm")
	@ResponseBody
	public boolean deleteCategoryConfirm(@RequestParam("bqc_no") int bqc_no) {
		log.info("deleteCategoryConfirm()");
		
		return qnaService.deleteCategoryConfirm(bqc_no);
		
	}
	
	
////////////////////////// 공지사항 	

	// qna 공지사항 리스트 양식
	@GetMapping("/noti_info/notice_list_form")
	public String qnaNoticeListForm() {
		log.info("qnaNoticeListForm()");
		
		return PagePath.QNA_NOTICE_LIST_FORM.getValue();
		
	}
	
	// qna 공지사항 리스트 가져오기
	@GetMapping("/noti_info/get_notice_list")
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
	@GetMapping("/noti_info/search_notice_list")
	@ResponseBody
	public Object searchNoticeList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "bqn_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchNoticeList()");
		
		Map<String, Object> searchQnaNoticeList = 
				qnaService.searchQnaNoticePagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchQnaNoticeListPageNum = 
				qnaService.searchQnaNoticeListPageNum(searchPart, searchString, page);
		
		searchQnaNoticeList.put("searchQnaNoticeListPageNum", searchQnaNoticeListPageNum);
		searchQnaNoticeList.put("sortValue", sortValue);
		searchQnaNoticeList.put("order", order);
		searchQnaNoticeList.put("searchPart", searchPart);
		searchQnaNoticeList.put("searchString", searchString);
		
		return searchQnaNoticeList;
		
	}
	
	// qna 공지사항 등록 양식
	@GetMapping("/noti_info/create_notice_form")
	public String createNoticeForm() {
		log.info("createNoticeForm()");
		
		return PagePath.QNA_CREATE_NOTICE_FORM.getValue();
		
	}
	
	// qna 공지사항 등록 확인
	@PostMapping("/noti_info/create_notice_confirm")
	@ResponseBody
	public boolean createNoticeConfrim(
			@RequestParam("bqn_title") String bqn_title, 
			@RequestParam("bqn_body") String bqn_body, 
			Principal principal) {
		log.info("createNoticeConfrim()");
		
		return qnaService.createNoticeConfrim(bqn_title, bqn_body, principal.getName());
		
	}
	
	// qna 공지사항 수정 양식
	@GetMapping("/noti_info/modify_notice_form")
	public String modifyNoticeForm(@RequestParam("bqn_no") int bqn_no, Model model) {
		log.info("modifyNoticeForm()");
		
		QnaNoticeDto qnaNoticeDto = 
				qnaService.getQnaNoticeInfoByNo(bqn_no);
		
		model.addAttribute("qnaNoticeDto", qnaNoticeDto);
		
		return PagePath.QNA_MODIFY_NOTICE_FORM.getValue();
		
	}
	
	// qna 공지사항 수정 확인
	@PostMapping("/noti_info/modify_notice_confirm")
	@ResponseBody
	public boolean modifyNoticeConfirm(
			@RequestParam("bqn_no") int bqn_no, 
			@RequestParam("bqn_title") String bqn_title, 
			@RequestParam("bqn_body") String bqn_body, 
			@RequestParam("bqn_writer_no") int bqn_writer_no, 
			Principal principal) {
		log.info("modifyNoticeConfirm()");
		
		return qnaService.modifyNoticeConfirm(bqn_no, bqn_title, bqn_body, bqn_writer_no, principal.getName());
		
	}
	
	// qna 공지사항 삭제 확인
	@PostMapping("/noti_info/delete_notice_confirm")
	@ResponseBody
	public boolean deleteNoticeConfrim(@RequestParam("bqn_no") int bqn_no) {
		log.info("deleteNoticeConfrim()");
		
		return qnaService.deleteNoticeConfrim(bqn_no);
		
	}
	
	
	// qna test 
	@GetMapping("/test")
	@ResponseBody
	public Object qnaTest() {
		log.info("test()");
		
		Map<String, Object> searchQnaNoticeList = 
				qnaService.searchQnaNoticePagingList("a_id", "admin", "bqn_no", "desc", 1);
		
		Map<String, Object> searchQnaNoticeListPageNum = 
				qnaService.searchQnaNoticeListPageNum("bqn_no", "desc", 1);
		
		searchQnaNoticeList.put("searchQnaNoticeListPageNum", searchQnaNoticeListPageNum);
		searchQnaNoticeList.put("sortValue", "bqn_no");
		searchQnaNoticeList.put("order", "desc");
		searchQnaNoticeList.put("searchPart", "a_id");
		searchQnaNoticeList.put("searchString", "admin");
		
		return searchQnaNoticeList;
		
	}
	
	
	
}
