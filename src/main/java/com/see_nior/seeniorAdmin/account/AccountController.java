package com.see_nior.seeniorAdmin.account;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/account")
public class AccountController {
	
	final private AccountService accountService;
	
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
		
	}

	// 회원 가입 양식
	@GetMapping("/sign_up_form")
	public String signUpForm(Model model) {
		log.info("signUpForm()");
		
		String nextPage = "account/sign_up_form";
		
		return nextPage;
	}
	
	// 회원 가입 확인
	@PostMapping("/sign_up_confirm")
	public String signUpConfirm(AdminAccountDto adminAccountDto, Model model) {
		log.info("signUpConfirm()");
		
		int result = accountService.signUpConfirm(adminAccountDto);
		
		model.addAttribute("signUpResult", result);
		
		String nextPage = "account/sign_up_result";
		
		return nextPage;
	}
	
	// 아이디 중복 여부 확인
	@GetMapping("/is_account")
	@ResponseBody
	public Object isAccount(@RequestParam("a_id") String a_id) {
		log.info("isAccount()");
		
		boolean result = accountService.isAccount(a_id);
		
		return result;
	}
	
	
	// 로그인 양식
	@GetMapping("/sign_in_form")
	public String signInForm(Model model) {
		log.info("signInForm()");
		
		String nextPage = "account/sign_in_form";
		
		return nextPage;
	}
	
	// 로그인 결과 확인
	@GetMapping("/sign_in_ng")
	public String signInNg(
			@RequestParam(value = "errMsg", required = false) String errMsg,
			Model model) {
		log.info("signInNg()");
		
		String nextPage = "account/sign_in_ng";
		
		model.addAttribute("errMsg", errMsg);
		
		return nextPage;
	}
	
	// 내 정보 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(Model model, Principal principal) {
		log.info("modifyForm()");
		
		String nextPage = "account/modify_form";
		
		return nextPage;
	}
	
	// 내 정보 가져오기
	@GetMapping("/info/get_account_info")
	@ResponseBody
	public Object getAccountInfo(Principal principal) {
		
		AdminAccountDto loginedAdminDto = 
				accountService.getAdminAccountById(principal.getName());
		
		return loginedAdminDto;
	}
	
	// 내 정보 수정 양식 가기 전 비밀번호 확인
	@PostMapping("/info/modify_check")
	@ResponseBody
	public Object modifyCheck(
			Principal principal, 
			@RequestParam("a_pw") String a_pw) {
		log.info("modifyCheck()");

		principal.getName();
		
		/*
		 * 로그인 ID 꺼내는 방법
		 * 1. Authentication authentication = 
		 * 			SecurityContextHolder.getContext().getAuthentication();
		 * 		authentication.getName();
		 * 2. principal.getName();
		*/
		
		AdminAccountDto loginedAdminDto = 
				accountService.modifyCheck(principal.getName(), a_pw);
		
		Date now = new Date();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		if (loginedAdminDto == null) {
			return null;
		} else {
			responseMap.put("loginedId", principal.getName());
			responseMap.put("checkDate", dateFormat.format(now));
			return responseMap;
		}
		
	}
	
	// 내 정보 수정 확인
	@PostMapping("/info/modify_confirm")
	public String modifyConfirm(Model model, Principal principal, AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		String nextPage = "account/modify_result_form";
		
		int modifyResult = -1;
		
		if (principal.getName().equals(adminAccountDto.getA_id())) {
			
			modifyResult = accountService.modifyConfirm(adminAccountDto);
			model.addAttribute("modifyResult", modifyResult);
			
		} else {
			
			model.addAttribute("modifyResult", modifyResult);
			
		}
		
		return nextPage;
	}
	
	// SUPER_ADMIN - ADMIN 정보 수정 양식
	@GetMapping("/list/admin_modify_form")
	public String adminModifyForm(@RequestParam("a_no") int a_no, Model model) {
		log.info("adminModifyForm()");
		
		String nextPage = "account/admin_modify_form";
		
		AdminAccountDto selectedAdminDto = 
				accountService.getAdminAccountByNo(a_no);
		
		model.addAttribute("selectedAdminDto", selectedAdminDto);
		
		return nextPage;
	}
	
	// SUPER_ADMIN - ADMIN 정보 수정 확인
	@PostMapping("/list/admin_modify_confirm")
	public String adminModifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("adminModifyConfirm()");
		
		accountService.adminModifyConfirm(adminAccountDto);
		
		return "redirect:/account/admin_modify_form?a_no=" + adminAccountDto.getA_no();
	}
	
	// 회원 탈퇴 확인 
	@GetMapping("/list/delete_confirm")
	public String deleteConfirm(
			@RequestParam("a_no") int a_no,
			Model model) {
		log.info("deleteConfirm()");
		
		String nextPage = "account/delete_result";
		
		int deleteResult = accountService.deleteConfirm(a_no);
		
		model.addAttribute("deleteResult", deleteResult);
		
		return nextPage;
	}
	
	// 관리자 리스트 바로가기 
	@GetMapping("/list/admin_list_form")
	public String adminListForm() {
		log.info("adminListForm()");
		
		String nextPage = "account/admin_list_form";
		
		return nextPage;
	}
	
	// 관리자 리스트 가져오기
	@GetMapping("/list/get_admin_list")
	@ResponseBody
	public Object getAdminList(
			@RequestParam(value = "approval", required = false, defaultValue = "all") String approval,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getAdminList()");
		
		Map<String, Object> adminList = accountService.getAdminPagingList(approval, page);
		
		Map<String, Object> adminListPage = accountService.getAdminListPageNum(page);
		adminList.put("adminListPage", adminListPage);
		adminList.put("approval", approval);
		
		return adminList;
	}
	
	// 관리자 리스트 검색
	@GetMapping("/list/search_admin_list")
	@ResponseBody
	public Object searchAdminList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "approval", required = false, defaultValue = "all") String approval,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchAdminList()");
		
		Map<String, Object> searchAdminList = 
				accountService.searchAdminPagingList(searchPart, searchString, approval, page);
		
		Map<String, Object> searchAdminListPage = 
				accountService.searchAdminListPageNum(searchPart, searchString, page);
		searchAdminList.put("searchAdminListPage", searchAdminListPage);
		searchAdminList.put("approval", approval);
		searchAdminList.put("searchPart", searchPart);
		searchAdminList.put("searchString", searchString);
		
		return searchAdminList;
	}
	
	// 관리자 가입 승인
	@GetMapping("/list/is_approval")
	public String isApproval(@RequestParam("a_no") int a_no, Model model) {
		log.info("isApproval()");
		
		String nextPage = "account/admin_list_form";
		
		accountService.isApproval(a_no);
		
		return nextPage;
	
	}
	
	// 비밀번호 초기화
	@GetMapping("/list/reset_password")
	public String resetPassword(@RequestParam("a_no") int a_no, Model model) {
		log.info("resetPassword()");
		
		int resetResult = accountService.resetPassword(a_no);
		
		model.addAttribute("resetResult", resetResult);
		
		return "redirect:/account/admin_list_form";
	}
	
	// AdminAccessDeniedHandler. 인가 실패 시 호출.
	@GetMapping("/access_denied_page")
	public String accessDeniedPage(
			@RequestParam("isLogined") boolean isLogined,
			Model model) {
		log.info("accessDeniedPage()");
		
		String nextPage = "account/access_denied_page";
		
		model.addAttribute("isLogined", isLogined);
		
		return nextPage;
	
	}
	
}