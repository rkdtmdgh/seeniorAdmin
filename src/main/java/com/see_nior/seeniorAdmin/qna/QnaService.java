package com.see_nior.seeniorAdmin.qna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.qna.mapper.QnaMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class QnaService {

	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수
	
	final private QnaMapper qnaMapper;
	
	// qna 리스트 가져오기
	public Map<String, Object> getQnaPagingList(String sortValue, String order, int page) {
		log.info("getQnaPagingList()");
		
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);

		List<AdminAccountDto> qnaDtos = qnaMapper.selectQnaList(pagingParams);
		pagingList.put("qnaDtos", qnaDtos);
		
		return pagingList;
	}

	// qna 리스트 총 개수
	public Map<String, Object> getQnaListPageNum(int page) {
		log.info("getQnaListPageNum()");
		
		Map<String, Object> qnaListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회 
		int qnaListCnt = qnaMapper.selectAllQnaListCnt();

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) qnaListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		qnaListPageNum.put("qnaListCnt", qnaListCnt);
		qnaListPageNum.put("page", page);
		qnaListPageNum.put("maxPage", maxPage);
		qnaListPageNum.put("startPage", startPage);
		qnaListPageNum.put("endPage", endPage);
		qnaListPageNum.put("blockLimit", blockLimit);
		qnaListPageNum.put("pageLimit", pageLimit);
		
		return qnaListPageNum;
		
	}
	
	// 질문 등록 여부 확인하기
	public List<QnaDto> getUnansweredQuestions() {
		log.info("getUnansweredQuestions()");
		
		List<QnaDto> unansweredQnaDtos = qnaMapper.selectUnansweredQuestions();
		
		return unansweredQnaDtos;
	}

	// qna 검색 리스트 가져오기
	public Map<String, Object> searchQnaPagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingSearchList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);

		List<AdminAccountDto> qnaDtos = qnaMapper.selectSearchQnaList(pagingParams);
		pagingSearchList.put("qnaDtos", qnaDtos);
		
		return pagingSearchList;
		
	}

	// 검색 qna 리스트 개수 
	public Map<String, Object> searchQnaListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaListPageNum()");
		
		Map<String, Object> searchQnaPageNum = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaListCnt = qnaMapper.selectSearchQnaListCnt(searchParams);

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchQnaListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchQnaPageNum.put("searchQnaListCnt", searchQnaListCnt);
		searchQnaPageNum.put("page", page);
		searchQnaPageNum.put("maxPage", maxPage);
		searchQnaPageNum.put("startPage", startPage);
		searchQnaPageNum.put("endPage", endPage);
		searchQnaPageNum.put("pageLimit", pageLimit);
		searchQnaPageNum.put("blockLimit", blockLimit);
		
		return searchQnaPageNum;
		
	}
	
	
}
