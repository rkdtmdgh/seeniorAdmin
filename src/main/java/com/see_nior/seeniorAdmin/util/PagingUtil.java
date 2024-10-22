package com.see_nior.seeniorAdmin.util;

import java.util.HashMap;
import java.util.Map;

import com.see_nior.seeniorAdmin.enums.PagingStatus;

public class PagingUtil {

	// pageNum 계산
	public static Map<String, Object> pageNum(String listName, int listCnt, int page) {
		
		Map<String, Object> pageNum = new HashMap<>();
		
		// 전체 페이지 개수 계산
		int maxPage = PagingStatus.PAGE_LIMIT.maxPage(listCnt);
		
		// 시작 페이지 값 계산
		int startPage = PagingStatus.BLOCK_LIMIT.startPage(page);
		
		// 마지막 페이지 값 계산
		int endPage = startPage + PagingStatus.BLOCK_LIMIT.getValue() - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		pageNum.put(listName, listCnt);
		pageNum.put("page", page);
		pageNum.put("maxPage", maxPage);
		pageNum.put("startPage", startPage);
		pageNum.put("endPage", endPage);
		pageNum.put("blockLimit", PagingStatus.BLOCK_LIMIT.getValue());
		pageNum.put("pageLimit", PagingStatus.PAGE_LIMIT.getValue());
		
		return pageNum;
		
	}
	
	// 리스트 가져오기 params
	public static Map<String, Object> pagingParams(String sortValue, String order, int page) {
		
		Map<String, Object> pagingParams = new HashMap<>();
		
		int pagingStart = PagingStatus.PAGE_LIMIT.pagingStart(page);
		
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", PagingStatus.PAGE_LIMIT.getValue());
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		return pagingParams;
		
	}
	
	// 검색 리스트 가져오기 params
	public static Map<String, Object> searchPagingParams(String searchPart, String searchString, String sortValue, String order, int page) {
		
		Map<String, Object> searchPagingParams = new HashMap<>();
		
		int pagingStart = PagingStatus.PAGE_LIMIT.pagingStart(page);
		
		searchPagingParams.put("start", pagingStart);
		searchPagingParams.put("limit", PagingStatus.PAGE_LIMIT.getValue());
		searchPagingParams.put("sortValue", sortValue);
		searchPagingParams.put("order", order);
		searchPagingParams.put("searchPart", searchPart);
		searchPagingParams.put("searchString", searchString);
		
		return searchPagingParams;
		
	}
	
}
