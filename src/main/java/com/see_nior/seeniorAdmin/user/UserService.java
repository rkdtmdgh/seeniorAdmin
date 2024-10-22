package com.see_nior.seeniorAdmin.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.UserAccountDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.user.mapper.UserMapper;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수
	
	private UserMapper userMapper;
	
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
		
	}
	
	// 일반 멤버 리스트 가져오기 
	public Map<String, Object> getUserAccountPagingList(String sortValue, String order, int page) {
		log.info("getUserAccountPagingList()");
		
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> userAccountDtos = 
				userMapper.selectUserList(PagingUtil.pagingParams(sortValue, order, page));
		pagingList.put("userAccountDtos", userAccountDtos);
		
		return pagingList;
		
	}

	// 일반 멤버 리스트 개수
	public Map<String, Object> getUserListPageNum(int page) {
		log.info("getUserListPageNum()");
		
		// 전체 리스트 개수 조회 
		int userAccountListCnt = userMapper.selectAllUserListCnt();
		
		return PagingUtil.pageNum("userAccountListCnt", userAccountListCnt, page);
	}

	//일반 멤버 검색 리트스 가져오기
	public Map<String, Object> searchUserPagingList(String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("searchUserPagingList()");
		
		Map<String, Object> pagingSearchList = new HashMap<>();
		
		List<AdminAccountDto> userAccountDtos = 
				userMapper.selectSearchUserList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchList.put("userAccountDtos", userAccountDtos);
		
		return pagingSearchList;
	}

	// 일반 멤버 검색 리스트 개수
	public Map<String, Object> searchUserListPageNum(int page, String searchPart, String searchString) {
		log.info("searchUserListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchUserListCnt = userMapper.selectSearchUserListCnt(searchParams);
		
		return PagingUtil.pageNum("searchUserListCnt", searchUserListCnt, page);
	}

	// 일반 멤버 정보 조회 by no
	public UserAccountDto getUserAccountByNo(int u_no) {
		log.info("getUserAccountByNo()");
		
		UserAccountDto userAccountDto =
				userMapper.selectUserAccountByNo(u_no);
		
		return userAccountDto;
	}

	// 일반 멤버 정보 수정 확인
	public Object userAccountModifyConfirm(UserAccountDto userAccountDto) {
		log.info("userAccountModifyConfirm()");
		
		int result =  userMapper.updateUserAccountInfo(userAccountDto);
		
		if (result >= 0)
			return SqlResult.SUCCESS;
		else 
			return SqlResult.FAIL;
	
	}

	// 일반 멤버 탈퇴 확인 delete_confirm	
	public Object deleteConfirm(int u_no) {
		log.info("deleteConfirm()");
		
		int result = userMapper.updateUserIsDeletedByNo(u_no);
		
		if (result >= 0) 
			return SqlResult.SUCCESS;
		else
			return SqlResult.FAIL;
			
	}

	// 일반 멤버 계정 정지 확인
	public Object blockedConfirm(UserAccountDto userAccountDto) {
		log.info("blockedConfirm()");
		
		int result = userMapper.updateUserIsBlockedByNo(userAccountDto);
		
		if (result >= 0) 
			return SqlResult.SUCCESS;
		else
			return SqlResult.FAIL;
		
	}

}


