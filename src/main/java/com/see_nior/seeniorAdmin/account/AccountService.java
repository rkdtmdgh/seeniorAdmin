package com.see_nior.seeniorAdmin.account;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	final static public int ADMIN_MODIFY_SUCCESS = 1;
	final static public int ADMIN_MODIFY_FAIL = 0;
	
	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수
	
	final private AccountMapper accountMapper;
	final private PasswordEncoder passwordEncoder;
	
	public AccountService(AccountMapper accountMapper, PasswordEncoder passwordEncoder) {
		this.accountMapper = accountMapper;
		this.passwordEncoder = passwordEncoder;
		
	}
	
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
					return ADMIN_SIGN_UP_FAIL;
				} else {
					return ADMIN_SIGN_UP_SUCCESS;
				}
				
			} else {
				
				adminAccountDto.setA_pw(passwordEncoder.encode(adminAccountDto.getA_pw()));
				
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
	public int modifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("modifyConfirm()");
		
		if (adminAccountDto.getA_pw() != "" && adminAccountDto.getA_pw() != null) {
			
			adminAccountDto.setA_pw(passwordEncoder.encode(adminAccountDto.getA_pw()));
			
		}
		
		int modifyResult = 
				accountMapper.updateMyAdminInfo(adminAccountDto);
		
		
		if (modifyResult >= 0) {
			
			return ADMIN_MODIFY_SUCCESS;
			
		} else {
			
			return ADMIN_MODIFY_FAIL;
			
		}
	}
	
	// SUPER_ADMIN - ADMIN 정보 수정 확인
	public void adminModifyConfirm(AdminAccountDto adminAccountDto) {
		log.info("adminModifyConfirm()");
		
		accountMapper.updateAdminInfoFromSuper(adminAccountDto);
		
	}
	
	// 회원 탈퇴 확인 
	public int deleteConfirm(int a_no) {
		log.info("deleteConfirm()");
		
		log.info("a_no ---- {}", a_no);
		
		return accountMapper.updateAdminIsDeletedByNo(a_no);
	}
	
	// 관리자 리스트 가져오기
	public Map<String, Object> getAdminPagingList(String approval, int page) {
		log.info("getAdminList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("approval", approval);

		List<AdminAccountDto> adminAccountDtos = accountMapper.selectAdminList(pagingParams);
		pagingList.put("adminAccountDtos", adminAccountDtos);
		
		return pagingList;
	}
	
	// 관리자 리스트 총 개수
	public Map<String, Object> getAdminListPageNum(int page) {
		log.info("getAccountListPageNum()");
		
		Map<String, Object> accountListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회 
		int accountListCnt = accountMapper.selectAllAccountListCnt();

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) accountListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		accountListPageNum.put("accountListCnt", accountListCnt);
		accountListPageNum.put("page", page);
		accountListPageNum.put("maxPage", maxPage);
		accountListPageNum.put("startPage", startPage);
		accountListPageNum.put("endPage", endPage);
		accountListPageNum.put("blockLimit", blockLimit);
		accountListPageNum.put("pageLimit", pageLimit);
		
		return accountListPageNum;
	}
	
	// 관리자 검색 리스트 가져오기
	public Map<String, Object> searchAdminPagingList(String searchPart, String searchString, String approval, int page) {
		log.info("searchAdminPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingSearchList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("approval", approval);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);

		List<AdminAccountDto> adminAccountDtos = accountMapper.selectSearchAdminList(pagingParams);
		pagingSearchList.put("adminAccountDtos", adminAccountDtos);
		
		return pagingSearchList;
	}
	
	// 관리자 검색 리스트 개수
	public Map<String, Object> searchAdminListPageNum(String searchPart, String searchString, int page) {
		log.info("searchAdminListPageNum()");
		
		Map<String, Object> searchAdminListPageNum = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchAdminListCnt = accountMapper.selectSearchAdminListCnt(searchParams);

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchAdminListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchAdminListPageNum.put("searchAdminListCnt", searchAdminListCnt);
		searchAdminListPageNum.put("page", page);
		searchAdminListPageNum.put("maxPage", maxPage);
		searchAdminListPageNum.put("startPage", startPage);
		searchAdminListPageNum.put("endPage", endPage);
		searchAdminListPageNum.put("blockLimit", blockLimit);
		searchAdminListPageNum.put("pageLimit", pageLimit);
		
		return searchAdminListPageNum;
	}
	
	// 관리자 가입 승인
	public void isApproval(int a_no) {
		log.info("isApproval()");
		
		accountMapper.updateAdminRoleByNo(a_no);
		
	}

	
	// 비밀번호 초기화
	public int resetPassword(int a_no) {
		log.info("resetPassword()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountByNo(a_no);
		
		String a_pw = "s" + adminAccountDto.getA_birth() + "!";
		
		return accountMapper.updateAdminPwReset(a_no, passwordEncoder.encode(a_pw));
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
