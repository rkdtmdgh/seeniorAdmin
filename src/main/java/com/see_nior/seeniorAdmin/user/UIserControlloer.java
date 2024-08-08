package com.see_nior.seeniorAdmin.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/user")
public class UIserControlloer {

	final private UserService userService; 
	
	public UIserControlloer(UserService userService) {
		this.userService = userService;
		
	}
	
	// 일반 멤버 리스트 가져오기 get_user_list
	@GetMapping("/get_user_list")
	public String getUserList() {
		log.info("getUserList()");
		
		
		return new String();
	}
	
	// 일반 멤버 정보 수정 양식 modify_form
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
	
	
	
}
