package com.see_nior.seeniorAdmin.notice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.notice.mapper.NoticeMapper;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeMapper noticeMapper;

	// 전체 공지사항 정보 가져오기 by no
	public NoticeDto getNoticeInfoByNo(int n_no) {
		log.info("getNoticeInfoByNo()");
		
		return noticeMapper.selectNoticeInfoByNo(n_no);
		
	}

	// 전체 공지사항 페이징 리스트 가져오기 
	public Map<String, Object> getNoticePagingList(String sortValue, String order, int page) {
		log.info("getNoticePagingList()");
		
		Map<String, Object> pagingNoticeList = new HashMap<>();
		
		List<AdminAccountDto> noticeDtos = 
				noticeMapper.selectNoticeList(PagingUtil.pagingParams(sortValue, order, page));
		pagingNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingNoticeList;
		
	}

	// 전체 공지사항 리스트 총 개수 
	public Map<String, Object> getNoticeListPageNum(int page) {
		log.info("getNoticeListPageNum()");
		
		// 전체 리스트 개수 조회 
		int noticeListCnt = noticeMapper.selectAllNoticeListCnt();
		
		return PagingUtil.pageNum("noticeListCnt", noticeListCnt, page);
		
	}

	// 전체 공지사항 검색 페이징 리스트 가져오기
	public Map<String, Object> searchNoticePagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchNoticePagingList()");
		
		Map<String, Object> pagingSearchNoticeList = new HashMap<>();
		
		List<AdminAccountDto> noticeDtos = 
				noticeMapper.selectSearchNoticeList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingSearchNoticeList;
		
	}

	// 전체 공지사항 검색 리스트 총 개수
	public Map<String, Object> searchNoticeListPageNum(String searchPart, String searchString, int page) {
		log.info("searchNoticeListPageNum()");

		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchNoticeListCnt = noticeMapper.selectSearchNoticeListCnt(searchParams);
		
		return PagingUtil.pageNum("searchNoticeListCnt", searchNoticeListCnt, page);
		
	}
	
	
	
	
}
