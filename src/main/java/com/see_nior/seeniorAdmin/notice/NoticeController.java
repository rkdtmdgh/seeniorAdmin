package com.see_nior.seeniorAdmin.notice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	// 전체 공지사항 양식
	@GetMapping("/info/notice_list_form")
	public String noticeListForm() {
		log.info("noticeListForm()");
		
		return PagePath.NOTICE_LIST_FORM.getValue();
		
	}
	
	// 전체 공지사항 리스트 가져오기 for main 
	
	// 전체 공지사항 리스트 가져오기
	
	// 전체 공지사항 검색 리스트 가져오기 
	
	// 전체 공지사항 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		return PagePath.NOTICE_CREATE_FORM.getValue();
		
	}
	
	// 전체 공지사항 등록 확인 
	
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
	
	// 전체 공지사항 삭제 확인
	
}
