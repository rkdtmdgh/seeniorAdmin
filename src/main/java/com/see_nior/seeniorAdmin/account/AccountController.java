package com.see_nior.seeniorAdmin.account;

import java.security.Principal;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	public Object isAccount(@RequestParam("id") String id) {
		log.info("isAccount()");
		
		boolean result = accountService.isAccount(id);
		
		return result;
	}
	
	
	// 로그인 양식
	@GetMapping("/sign_in_form")
	public String signInForm(Model model) {
		log.info("signInForm()");
		
		String nextPage = "account/sign_in_form";
		
		return nextPage;
	}
	
	// 로그인 확인
	@PostMapping("/sign_in_confirm")
	public String signInConfirm() {
		log.info("signUpConfirm()");
		
		return null;
	}
	
	// 로그인 결과 확인 - SecurityConfig formLogin successHandler, failureHandler 에서 호출  
	@GetMapping("/sign_in_result")
	public String signInResult(
			@RequestParam("logined") boolean logined,
			@RequestParam(value = "errMsg", required = false) String errMsg,
			Model model) {
		log.info("signInResult()");
		
		String nextPage = "home";
		
		if (!logined) {
			nextPage = "account/sign_in_ng";
			model.addAttribute("logined", logined);
			model.addAttribute("errMsg", errMsg);
			
		}
		
		return nextPage;
	}
	
	// 회원 정보 수정 양식
	@GetMapping("/modify_form")
	public String modifyForm(Model model, Principal principal) {
		log.info("modifyForm()");
		
		String nextPage = "account/modify_form";
		
		principal.getName();
		
		/*
		 * 로그인 ID 꺼내는 방법
		 * 1. Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 * 		authentication.getName();
		 * 2. principal.getName();
		*/
		
		AdminAccountDto loginedAdminDto = accountService.getAdminAccountById(principal.getName());
		
		model.addAttribute("loginedAdminDto", loginedAdminDto);
		
		return nextPage;
	}
	
	// 회원 정보 수정 확인
	@PostMapping("/modify_confirm")
	public String modifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		accountService.modifyConfirm(adminAccountDto);
		
		return "redirect:/account/modify_form";
	}
	
	// 회원 탈퇴 확인 
	@GetMapping("/delete_confirm")
	public String deleteConfirm(
			HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		log.info("deleteConfirm");
		
		String nextPage = "account/delete_result";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		int deleteResult = accountService.deleteConfirm(authentication.getName());
		
		model.addAttribute("deleteResult", deleteResult);
		
		if (deleteResult > 0) {
		
			if (authentication != null) {
				new SecurityContextLogoutHandler().logout(request, response, authentication);
				
			}
			
		}
		
		return nextPage;
	}
	
	// 관리자 리스트 바로가기 
	@GetMapping("/admin_list_form")
	public String adminListForm() {
		log.info("adminListForm()");
		
		String nextPage = "account/admin_list_form";
		
		return nextPage;
	}
	
	
	// 관리자 리스트 가져오기
	@GetMapping("/get_admin_list")
	@ResponseBody
	public Object getAdminList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getAdminList()");
		
		Map<String, Object> adminList = accountService.getAdminPagingList(page);
		
		Map<String, Object> accountListPage = accountService.getAccountListPageNum(page);
		adminList.put("accountListPage", accountListPage);
		
		return adminList;
	}
	
	
	// 관리자 가입 승인
	@GetMapping("/is_approval")
	public String isApproval(@RequestParam("no") int no, Model model) {
		log.info("isApproval()");
		
		String nextPage = "account/admin_list_form";
		
		accountService.isApproval(no);
		
		return nextPage;
	
	}
	
	// AdminAccessDeniedHandler. 인가 실패 시 호출.
	@GetMapping("/access_denied_page")
	public String accessDeniedPage() {
		log.info("accessDeniedPage()");
		
		String nextPage = "account/access_denied_page";
		
		return nextPage;
	
	}
	
}