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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

//@RequiredArgsConstructor --- 초기화 되지않은 final 필드나, @NonNull 이 붙은 필드에 대해 생성자를 생성해 줍니다.

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
	
	final private AccountService accountService;

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
	@GetMapping("/sign_in_result")
	public String signInNg(
			@RequestParam(value = "errMsg", required = false) String errMsg,
			@RequestParam("result") boolean result,
			Model model) {
		log.info("signInNg()");
		
		String nextPage = "account/sign_in_result";
		
		model.addAttribute("errMsg", errMsg);
		model.addAttribute("result", result);
		
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
	@ResponseBody
	public Object modifyConfirm(Model model, Principal principal, AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		if (principal.getName().equals(adminAccountDto.getA_id())) {
			
			return accountService.modifyConfirm(adminAccountDto);
			
		} else {
			
			return false;
			
		}
		
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
	@ResponseBody
	public Object adminModifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("adminModifyConfirm()");
		
		return accountService.adminModifyConfirm(adminAccountDto);
		
	}
	
	// 회원 탈퇴 확인 
	@PostMapping("/list/delete_confirm")
	@ResponseBody
	public Object deleteConfirm(
			@RequestParam("a_no") int a_no) {
		log.info("deleteConfirm()");
		
		return accountService.deleteConfirm(a_no);
		
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
			@RequestParam(value = "sortValue", required = false, defaultValue = "a_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getAdminList()");
		
		Map<String, Object> adminList = accountService.getAdminPagingList(sortValue, order, page);
		
		Map<String, Object> adminListPage = accountService.getAdminListPageNum(page);
		adminList.put("adminListPage", adminListPage);
		adminList.put("approval", sortValue);
		adminList.put("order", order);
		
		return adminList;
	}
	
	// 관리자 리스트 검색
	@GetMapping("/list/search_admin_list")
	@ResponseBody
	public Object searchAdminList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "a_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchAdminList()");
		
		Map<String, Object> searchAdminList = 
				accountService.searchAdminPagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchAdminListPage = 
				accountService.searchAdminListPageNum(searchPart, searchString, page);
		
		searchAdminList.put("searchAdminListPage", searchAdminListPage);
		searchAdminList.put("approval", sortValue);
		searchAdminList.put("order", order);
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
	@PostMapping("/list/reset_password")
	@ResponseBody
	public Object resetPassword(@RequestParam("a_no") int a_no) {
		log.info("resetPassword()");
		
		return accountService.resetPassword(a_no);
		
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