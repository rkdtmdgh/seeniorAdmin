package com.see_nior.seeniorAdmin.user;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user")
public class UserControlloer {

	final private UserService userService; 
	
	public UserControlloer(UserService userService) {
		this.userService = userService;
		
	}
	
	// 일반 멤버 리스트 양식
	@GetMapping("/user_list_form")
	public String userListForm() {
		log.info("userListForm()");
		
		String nextPage = "user/user_list_form";
		
		return nextPage;
	}

	// 일반 멤버 리스트 가져오기 
	@GetMapping("/get_user_list")
	@ResponseBody
	public Object getUserList(
			@RequestParam(value = "sort", required = false, defaultValue = "all") String sort,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getUserList()");
		
		Map<String, Object> userList = userService.getUserPagingList(sort, page);
		
		Map<String, Object> userListPage = userService.getUserListPageNum(page);
		userList.put("userListPage", userListPage);
		userList.put("sort", sort);
		
		return userList;
	} 
	
	// 일반 멤버 검색 리스트 가져오기
	@GetMapping("/search_user_list")
	@ResponseBody
	public Object searchUserList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString) {
		log.info("searchUserList()");
		
		Map<String, Object> searchUserList = 
				userService.searchUserPagingList(page, searchPart, searchString);
		
		Map<String, Object> userListPage = 
				userService.searchUserListPageNum(page, searchPart, searchString);
		searchUserList.put("userListPage", userListPage);
		searchUserList.put("searchPart", searchPart);
		searchUserList.put("searchString", searchString);
		
		return searchUserList;
	}
	
	// 일반 멤버 정보 수정 양식
	@GetMapping("/modify_form")
	public String modifyForm() {
		log.info("modifyForm()");
		
		return new String();
	}
	
	// 일반 멤버 정보 수정 확인 modify_confirm
	@PostMapping("/modify_confirm")
	public String modifyConfirm() {
		log.info("getUserList()");
		
		return null;
	}
	
	// 일반 멤버 탈퇴 확인 delete_confirm
	@GetMapping("/delete_confirm")
	public String deleteConfirm() {
		log.info("deleteConfirm()");
		
		return new String();
	}
	
	
	
}
