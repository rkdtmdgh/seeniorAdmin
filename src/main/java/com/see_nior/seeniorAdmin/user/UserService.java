package com.see_nior.seeniorAdmin.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.user.mapper.UserMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	private int pageLimit = 5;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수
	
	private UserMapper userMapper;
	
	public UserService(UserMapper userMapper) {
		this.userMapper = userMapper;
		
	}
	
	// 일반 멤버 리스트 가져오기 
	public Map<String, Object> getUserPagingList(int page) {
		log.info("getUserPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Integer> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);

		List<AdminAccountDto> userAccountDtos = userMapper.selectUserList(pagingParams);
		pagingList.put("userAccountDtos", userAccountDtos);
		
		return pagingList;
		
	}

	// 일반 멤버 리스트 개수
	public Map<String, Object> getUserListPageNum(int page) {
		log.info("getUserListPageNum()");
		
		Map<String, Object> userListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회 
		int userListCnt = userMapper.selectUserListCnt();

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) userListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		userListPageNum.put("userListCnt", userListCnt);
		userListPageNum.put("page", page);
		userListPageNum.put("maxPage", maxPage);
		userListPageNum.put("startPage", startPage);
		userListPageNum.put("endPage", endPage);
		
		return userListPageNum;
	}

	//일반 멤버 검색 리트스 가져오기
	public Map<String, Object> searchUserPagingList(int page, String searchPart, String searchString) {
		log.info("searchUserPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);

		List<AdminAccountDto> searchUserAccountDtos = userMapper.selectSearchUserList(pagingParams);
		pagingList.put("searchUserAccountDtos", searchUserAccountDtos);
		
		return pagingList;
	}

	// 일반 멤버 검색 리스트 개수
	public Map<String, Object> searchUserListPageNum(int page, String searchPart, String searchString) {
		log.info("searchUserListPageNum()");
		
		Map<String, Object> searchUserListPageNum = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchParams);
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
		
		return searchUserListPageNum;
	}
	
	

}
