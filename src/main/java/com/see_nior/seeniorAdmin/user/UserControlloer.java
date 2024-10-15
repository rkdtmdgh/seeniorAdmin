package com.see_nior.seeniorAdmin.user;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.UserAccountDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user_account")
public class UserControlloer {

	final private UserService userService; 
	
	public UserControlloer(UserService userService) {
		this.userService = userService;
		
	}
	
	// 일반 멤버 리스트 양식
	@GetMapping("/info/user_account_list_form")
	public String userAccountListForm() {
		log.info("userAccountListForm()");
		
		String nextPage = "userAccount/user_account_list_form";
		
		return nextPage;
	}

	// 일반 멤버 리스트 가져오기 
	@GetMapping("/info/get_user_account_list")
	@ResponseBody
	public Object getUserAccountList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "u_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getUserAccountList()");
		
		Map<String, Object> userAccountList = userService.getUserAccountPagingList(sortValue, order, page);
		
		Map<String, Object> userAccountListPage = userService.getUserListPageNum(page);
		userAccountList.put("userAccountListPage", userAccountListPage);
		userAccountList.put("sortValue", sortValue);
		userAccountList.put("order", order);
		
		return userAccountList;
	} 
	
	// 일반 멤버 검색 리스트 가져오기
	@GetMapping("/info/search_user_account_list")
	@ResponseBody
	public Object searchUserAccountList(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "u_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchUserAccountList()");
		
		Map<String, Object> searchUserList = 
				userService.searchUserPagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchUserAccountListPageNum = 
				userService.searchUserListPageNum(page, searchPart, searchString);
		searchUserList.put("searchUserAccountListPageNum", searchUserAccountListPageNum);
		searchUserList.put("sortValue", sortValue);
		searchUserList.put("order", order);
		searchUserList.put("searchPart", searchPart);
		searchUserList.put("searchString", searchString);
		
		return searchUserList;
	}
	
	// 일반 멤버 정보 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam("u_no") int u_no, Model model) {
		log.info("modifyForm()");
		
		String nextPage = "userAccount/modify_form";
		
		UserAccountDto userAccountDto = userService.getUserAccountByNo(u_no);
		
		model.addAttribute(userAccountDto);
		
		return nextPage;
	}
	
	// 일반 멤버 정보 수정 확인 
	@PostMapping("/info/modify_confirm")
	@ResponseBody
	public Object modifyConfirm(UserAccountDto userAccountDto) {
		log.info("modifyConfirm()");
		
		return userService.userAccountModifyConfirm(userAccountDto);
		
	}
	
	// 일반 멤버 탈퇴 확인 delete_confirm
	@GetMapping("/info/delete_confirm")
	public String deleteConfirm() {
		log.info("deleteConfirm()");
		
		return new String();
	}
	
	
}
