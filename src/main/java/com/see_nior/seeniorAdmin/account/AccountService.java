package com.see_nior.seeniorAdmin.account;

import java.util.ArrayList;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AccountService {
	
	final static public int ADMIN_ALREADY = -1;
	final static public int ADMIN_SIGN_UP_FAIL = 0;
	final static public int ADMIN_SIGN_UP_SUCCESS = 1;
	
	final private AccountMapper accountMapper;
	final private PasswordEncoder passwordEncoder;
	
	public AccountService(AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
		this.accountMapper = accountMapper;
		this.passwordEncoder = passwordEncoder;
		
	}
	
	// 회원 가입 확인
	public int signUpConfirm(AdminAccountDto adminAccountDto) {
		log.info("signUpConfirm()");
		
		// 아이디 중복 여부
		boolean isAccount = accountMapper.isAccount(adminAccountDto.getId());
		
		if (!isAccount) {
			
			if (adminAccountDto.getId().equals("super_admin@seenior.com")) {
				
				adminAccountDto.setPw(passwordEncoder.encode(adminAccountDto.getPw()));
				adminAccountDto.setAuthority_role("SUPER_ADMIN");
				
				int result = accountMapper.insertNewAdmin(adminAccountDto);
				
				if (result <= 0) {
					
					return ADMIN_SIGN_UP_FAIL;
					
				} else {
					
					return ADMIN_SIGN_UP_SUCCESS;
					
				}
				
			} else {
				
				adminAccountDto.setPw(passwordEncoder.encode(adminAccountDto.getPw()));
				
				int result = accountMapper.insertNewAdmin(adminAccountDto);
				
				if (result <= 0) {
					
					return ADMIN_SIGN_UP_FAIL;
					
				} else {
					
					return ADMIN_SIGN_UP_SUCCESS;
					
				}
				
			}
			
		} else {
			
			return ADMIN_ALREADY;
			
		}
		
	}
	
	// 관리자 정보 조회 by id
	public AdminAccountDto getAdminAccountById(String id) {
		log.info("getAdminAccountById()");
		
		AdminAccountDto adminAccountDto = accountMapper.getAdminAccountById(id);
		
		return adminAccountDto;
		
	}
	
	// 회원 정보 수정 확인
	public void modifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		adminAccountDto.setPw(passwordEncoder.encode(adminAccountDto.getPw()));
		
		accountMapper.updateAdminInfo(adminAccountDto);
		
	}
	
	// 회원 탈퇴 확인 
	public int deleteConfirm(String id) {
		log.info("deleteConfirm()");
		
		return accountMapper.updateAdminIsDeleted(id);
	}
	
	// 관리자 리스트 가져오기
	public ArrayList<AdminAccountDto> getAdminList() {
		log.info("getAdminList()");

		return accountMapper.getAdminList();
	}
	
	// 관리자 가입 승인
	public void isApproval(int no) {
		log.info("isApproval()");
		
		accountMapper.updateAdminRoleByNo(no);
		
	}
	
}
