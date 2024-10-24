package com.see_nior.seeniorAdmin.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.enums.AdminSighUpStatus;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class AccountService {
	
	final private AccountMapper accountMapper;
	final private PasswordEncoder passwordEncoder;
	
	// 아이디 중복 확인
	public boolean isAccount(String a_id) {
		log.info("isAccount()");
		
		return accountMapper.isAccount(a_id);
	}
	
	// 회원 가입 확인
	public int signUpConfirm(AdminAccountDto adminAccountDto) {
		log.info("signUpConfirm()");
		
		// 아이디 중복 여부
		boolean isAccount = accountMapper.isAccount(adminAccountDto.getA_id());
		
		if (!isAccount) {
			
			if (adminAccountDto.getA_id().equals("super_admin@seenior.com")) {
				
				adminAccountDto.setA_pw(passwordEncoder.encode(adminAccountDto.getA_pw()));
				adminAccountDto.setA_authority_role("SUPER_ADMIN");
				
				int result = accountMapper.insertNewAdmin(adminAccountDto);
				
				if (result <= 0) {
					return AdminSighUpStatus.FAIL.getValue();
				} else {
					return AdminSighUpStatus.SUCCESS.getValue();
				}
				
			} else {
				
				adminAccountDto.setA_pw(passwordEncoder.encode(adminAccountDto.getA_pw()));
				
				int result = accountMapper.insertNewAdmin(adminAccountDto);
				
				if (result <= 0) {
					return AdminSighUpStatus.FAIL.getValue();
				} else {
					return AdminSighUpStatus.SUCCESS.getValue();
				}
				
			}
			
		} else {
			return AdminSighUpStatus.ALREADY.getValue();
		}
		
	}
	
	// 관리자 정보 조회 by id
	public AdminAccountDto getAdminAccountById(String a_id) {
		log.info("getAdminAccountById()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountById(a_id);
		
		return adminAccountDto;
	}
	
	// 관리자 정보 조회 by no
	public AdminAccountDto getAdminAccountByNo(int a_no) {
		log.info("getAdminAccountByNo()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountByNo(a_no);
		
		return adminAccountDto;
	}
	
	// 내 정보 수정 확인
	public boolean modifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		// 비밀번호가 null &b 빈값이 아닐 경우에만 암호화.
		if (adminAccountDto.getA_pw() != null && !adminAccountDto.getA_pw().equals("")) {
			
			adminAccountDto.setA_pw(passwordEncoder.encode(adminAccountDto.getA_pw()));
			
		}
		
		int modifyResult = 
				accountMapper.updateMyAdminInfo(adminAccountDto);
		
		if (modifyResult >= 0) {
			
			return SqlResult.SUCCESS.getValue();
			
		} else {
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}
	
	// SUPER_ADMIN - ADMIN 정보 수정 확인
	public boolean adminModifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("adminModifyConfirm()");
		
		int result = 
				accountMapper.updateAdminInfoFromSuper(adminAccountDto);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}
	
	// 회원 탈퇴 확인 
	public boolean deleteConfirm(int a_no) {
		log.info("deleteConfirm()");
		
		int result = 
				accountMapper.updateAdminIsDeletedByNo(a_no);
		
		if (result >= 0) 
			return SqlResult.SUCCESS.getValue();
		else
			return SqlResult.FAIL.getValue();
	}
	
	// 관리자 리스트 가져오기
	public Map<String, Object> getAdminPagingList(String sortValue, String order, int page) {
		log.info("getAdminList()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> adminAccountDtos = 
				accountMapper.selectAdminList(PagingUtil.pagingParams(sortValue, order, page));
		pagingList.put("adminAccountDtos", adminAccountDtos);
		
		return pagingList;
	}
	
	// 관리자 리스트 총 개수
	public Map<String, Object> getAdminListPageNum(int page) {
		log.info("getAccountListPageNum()");
		
		// 전체 리스트 개수 조회 
		int accountListCnt = accountMapper.selectAllAccountListCnt();
		
		return PagingUtil.pageNum("accountListCnt", accountListCnt, page);
	}
	
	// 관리자 검색 리스트 가져오기
	public Map<String, Object> searchAdminPagingList(String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("searchAdminPagingList()");
		
		Map<String, Object> pagingSearchList = new HashMap<>();

		List<AdminAccountDto> adminAccountDtos = 
				accountMapper.selectSearchAdminList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchList.put("adminAccountDtos", adminAccountDtos);
		
		return pagingSearchList;
	}
	
	// 관리자 검색 리스트 개수
	public Map<String, Object> searchAdminListPageNum(String searchPart, String searchString, int page) {
		log.info("searchAdminListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchAdminListCnt = accountMapper.selectSearchAdminListCnt(searchParams);
		
		return PagingUtil.pageNum("searchAdminListCnt", searchAdminListCnt, page);
	}
	
	// 관리자 가입 승인
	public void isApproval(int a_no) {
		log.info("isApproval()");
		
		accountMapper.updateAdminRoleByNo(a_no);
		
	}

	
	// 비밀번호 초기화
	public boolean resetPassword(int a_no) {
		log.info("resetPassword()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountByNo(a_no);
		
		String a_pw = "s" + adminAccountDto.getA_birth().replace("-", "") + "!";
		
		adminAccountDto.setA_pw(passwordEncoder.encode(a_pw));
		
		int result = 
				accountMapper.updateAdminPwReset(adminAccountDto);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}

	// 비밀번호 확인
	public AdminAccountDto modifyCheck(String a_id, String a_pw) {
		log.info("modifyCheck()");
		
		AdminAccountDto loginedAdminDto = 
				accountMapper.selectAdminAccountById(a_id);
		
		if (passwordEncoder.matches(a_pw, loginedAdminDto.getA_pw())) {
			
			return loginedAdminDto;
			
		}
		
		return null;
	}

}
