package com.see_nior.seeniorAdmin.util;

import java.util.HashMap;
import java.util.Map;

public class PagingUtil {

	// pageNum 계산
	public static Map<String, Object> pageNum(int page_limit, int block_limit, String listName, int listCnt, int page) {
		
		Map<String, Object> pageNum = new HashMap<>();
		
		// 전체 페이지 개수 계산
//		int maxPage = PagingStatus.PAGE_LIMIT.maxPage(listCnt);
		int maxPage = (int) (Math.ceil((double) listCnt / page_limit));
		
		// 시작 페이지 값 계산
//		int startPage = PagingStatus.BLOCK_LIMIT.startPage(page);
		int startPage = ((int) (Math.ceil((double) page / block_limit)) - 1) * block_limit + 1;
		
		
		// 마지막 페이지 값 계산
//		int endPage = startPage + PagingStatus.BLOCK_LIMIT.getValue() - 1;
		int endPage = startPage + block_limit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		pageNum.put(listName, listCnt);
		pageNum.put("page", page);
		pageNum.put("maxPage", maxPage);
		pageNum.put("startPage", startPage);
		pageNum.put("endPage", endPage);
		pageNum.put("pageLimit", page_limit);
		pageNum.put("blockLimit", block_limit);		
		return pageNum;
		
	}
	
	// 리스트 가져오기 params
	public static Map<String, Object> pagingParams(int page_limit, String sortValue, String order, int page) {
		
		Map<String, Object> pagingParams = new HashMap<>();
		
//		int pagingStart = PagingStatus.PAGE_LIMIT.pagingStart(page);
		int pagingStart = (page - 1) * page_limit;
		
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", page_limit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		return pagingParams;
		
	}
	
	// 검색 리스트 가져오기 params
	public static Map<String, Object> searchPagingParams(
			int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		
		Map<String, Object> searchPagingParams = new HashMap<>();
		
//		int pagingStart = PagingStatus.PAGE_LIMIT.pagingStart(page);
		int pagingStart = (page - 1) * page_limit;
		
		searchPagingParams.put("start", pagingStart);
		searchPagingParams.put("limit", page_limit);
		searchPagingParams.put("sortValue", sortValue);
		searchPagingParams.put("order", order);
		searchPagingParams.put("searchPart", searchPart);
		searchPagingParams.put("searchString", searchString);
		
		return searchPagingParams;
		
	}
	
	// 리스트 가져오기 params
	public static Map<String, Object> pagingParamsForSelectBox(int page_limit, String sortValue, String order, int page, int info_no) {
		
		Map<String, Object> pagingParams = new HashMap<>();
		
//		int pagingStart = PagingStatus.PAGE_LIMIT.pagingStart(page);
		int pagingStart = (page - 1) * page_limit;
		
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", page_limit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("infoNo", info_no);
		
		return pagingParams;
		
	}
	
}
