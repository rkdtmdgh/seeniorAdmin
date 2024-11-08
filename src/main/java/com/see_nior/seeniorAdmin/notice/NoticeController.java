package com.see_nior.seeniorAdmin.notice;

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
import org.springframework.web.multipart.MultipartFile;

import com.see_nior.seeniorAdmin.account.AccountService;
import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.enums.PagePath;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

	private final NoticeService noticeService;
	private final AccountService accountService;
	
	// 전체 공지사항 양식
	@GetMapping("/info/notice_list_form")
	public String noticeListForm() {
		log.info("noticeListForm()");
		
		return PagePath.NOTICE_LIST_FORM.getValue();
		
	}
	
	// 전체 공지사항 리스트 가져오기
	@GetMapping("/info/get_notice_list")
	@ResponseBody
	public Object getNoticeList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "n_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getNoticeList()");
		
		Map<String, Object> noticeList = 
				noticeService.getNoticePagingList(sortValue, order, page);
		
		Map<String, Object> noticeListPageNum = noticeService.getNoticeListPageNum(page);
		
		noticeList.put("noticeListPageNum", noticeListPageNum);
		noticeList.put("sortValue", sortValue);
		noticeList.put("order", order);
		
		return noticeList;
	}
	
	// 전체 공지사항 검색 리스트 가져오기 
	@GetMapping("/info/search_notice_list")
	@ResponseBody
	public Object searchNoticeList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "n_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchNoticeList()");
		
		Map<String, Object> searchNoticeList = 
				noticeService.searchNoticePagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchNoticeListPageNum = 
				noticeService.searchNoticeListPageNum(searchPart, searchString, page);
		
		searchNoticeList.put("searchNoticeListPageNum", searchNoticeListPageNum);
		searchNoticeList.put("sortValue", sortValue);
		searchNoticeList.put("order", order);
		searchNoticeList.put("searchPart", searchPart);
		searchNoticeList.put("searchString", searchString);
		
		return searchNoticeList;
		
	}
	
	// 전체 공지사항 리스트 가져오기 for main 
	@GetMapping("/main/get_notice_list")
	@ResponseBody
	public Object getNoticeListForMain(@RequestParam("page_limit") int page_limit) {
		log.info("getNoticeListForMain()");
		
		return noticeService.getNoticeListForMain(page_limit);
	}
	
	
	// 전체 공지사항 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		return PagePath.NOTICE_CREATE_FORM.getValue();
		
	}
	
	// 전체 공지사항 등록 확인 
	@PostMapping("/info/create_confirm")
	@ResponseBody
	public boolean createConfirm(
			@RequestParam(value = "files" , required = false) List<MultipartFile> files,
			NoticeDto noticeDto) {
		log.info("createConfirm()");
		
		return noticeService.createConfirm(files, noticeDto);
		
	}
	
	// 전체 공지사항 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam("n_no") int n_no, Model model) {
		log.info("modifyForm()");
		
		NoticeDto noticeDto = 
				noticeService.getNoticeInfoByNo(n_no);
		
		model.addAttribute("noticeDto", noticeDto);
		
		return PagePath.NOTICE_MODIFY_FORM.getValue();
		
	}
	
	// 전체 공지사항 수정 확인
	@PostMapping("/info/modify_confirm")
	@ResponseBody
	public boolean modifyConfirm(
			@RequestParam(value = "files" , required = false) List<MultipartFile> files, 
			@RequestParam(value = "deleteFileNames", required = false) List<String> deleteFileNames,
			NoticeDto noticeDto, 
			Principal principal) {
		log.info("modifyConfirm()");
		
		boolean comparedResult = 
				accountService.compareId(principal.getName(), noticeDto.getN_writer_no());
		
		if (comparedResult) {
			
			if (files != null && files.size() != 0 && files.get(0).getSize() != 0) 
				return noticeService.modifyConfirm(files, deleteFileNames, noticeDto);
			 else 
				return noticeService.modifyConfirm(null, deleteFileNames, noticeDto);
			
		} else {
			
			return false;
			
		}
		
	}
	
	
	// 전체 공지사항 삭제 확인
	
	
	////////////////////////////////
	@GetMapping("/test")
	@ResponseBody
	public Object test() {
		log.info("test()");
		
		Map<String, Object> noticeList = 
				noticeService.getNoticePagingList("n_no", "desc", 1);
		
		Map<String, Object> noticeListPageNum = noticeService.getNoticeListPageNum(1);
		
		noticeList.put("noticeListPageNum", noticeListPageNum);
		noticeList.put("n_no", "m_no");
		noticeList.put("desc", "desc");
		
		return noticeList;
		
	}
	
	
	
}
