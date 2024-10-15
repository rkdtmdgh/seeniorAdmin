package com.see_nior.seeniorAdmin.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.UserAccountDto;
import com.see_nior.seeniorAdmin.user.mapper.UserMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	final static public boolean USER_MODIFY_SUCCESS = true;
	final static public boolean USER_MODIFY_FAIL = false;
	
	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수
	
	private UserMapper userMapper;
	
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
		
	}
	
	// 일반 멤버 리스트 가져오기 
	public Map<String, Object> getUserAccountPagingList(String sortValue, String order, int page) {
		log.info("getUserAccountPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);

		List<AdminAccountDto> userAccountDtos = userMapper.selectUserList(pagingParams);
		pagingList.put("userAccountDtos", userAccountDtos);
		
		return pagingList;
		
	}

	// 일반 멤버 리스트 개수
	public Map<String, Object> getUserListPageNum(int page) {
		log.info("getUserListPageNum()");
		
		Map<String, Object> userListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회 
		int userAccountListCnt = userMapper.selectAllUserListCnt();

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) userAccountListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		userListPageNum.put("userAccountListCnt", userAccountListCnt);
		userListPageNum.put("page", page);
		userListPageNum.put("maxPage", maxPage);
		userListPageNum.put("startPage", startPage);
		userListPageNum.put("endPage", endPage);
		userListPageNum.put("blockLimit", blockLimit);
		userListPageNum.put("pageLimit", pageLimit);
		
		return userListPageNum;
	}

	//일반 멤버 검색 리트스 가져오기
	public Map<String, Object> searchUserPagingList(String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("searchUserPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingSearchList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);

		List<AdminAccountDto> userAccountDtos = userMapper.selectSearchUserList(pagingParams);
		pagingSearchList.put("userAccountDtos", userAccountDtos);
		
		return pagingSearchList;
	}

	// 일반 멤버 검색 리스트 개수
	public Map<String, Object> searchUserListPageNum(int page, String searchPart, String searchString) {
		log.info("searchUserListPageNum()");
		
		Map<String, Object> searchUserListPageNum = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchUserListCnt = userMapper.selectSearchUserListCnt(searchParams);

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchUserListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchUserListPageNum.put("searchUserListCnt", searchUserListCnt);
		searchUserListPageNum.put("page", page);
		searchUserListPageNum.put("maxPage", maxPage);
		searchUserListPageNum.put("startPage", startPage);
		searchUserListPageNum.put("endPage", endPage);
		searchUserListPageNum.put("pageLimit", pageLimit);
		searchUserListPageNum.put("blockLimit", blockLimit);
		
		return searchUserListPageNum;
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
			return USER_MODIFY_SUCCESS;
		else 
			return USER_MODIFY_FAIL;
	
	}
	
	

}
