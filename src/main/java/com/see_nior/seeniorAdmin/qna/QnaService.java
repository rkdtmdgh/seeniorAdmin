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
	
	
}
