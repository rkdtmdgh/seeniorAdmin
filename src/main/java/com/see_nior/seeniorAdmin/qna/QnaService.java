package com.see_nior.seeniorAdmin.qna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaAnswerDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.qna.mapper.QnaMapper;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class QnaService {

	final private QnaMapper qnaMapper;
	
	// qna 리스트 가져오기
	public Map<String, Object> getQnaPagingList(String sortValue, String order, int page) {
		log.info("getQnaPagingList()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> qnaDtos = qnaMapper.selectQnaList(PagingUtil.pagingParams(sortValue, order, page));
		pagingList.put("qnaDtos", qnaDtos);
		
		return pagingList;
	}

	// qna 리스트 총 개수
	public Map<String, Object> getQnaListPageNum(int page) {
		log.info("getQnaListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaListCnt = qnaMapper.selectAllQnaListCnt();
		
		return PagingUtil.pageNum("qnaListCnt", qnaListCnt, page);
		
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
		
		Map<String, Object> pagingSearchList = new HashMap<>();

		List<AdminAccountDto> qnaDtos = 
				qnaMapper.selectSearchQnaList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchList.put("qnaDtos", qnaDtos);
		
		return pagingSearchList;
		
	}

	// 검색 qna 리스트 개수 
	public Map<String, Object> searchQnaListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaListCnt = qnaMapper.selectSearchQnaListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaListCnt", searchQnaListCnt, page);
		
	}
 
	// qna 정보 가져오기 by no
	public QnaDto getQnaInfoByNo(int bq_no) {
		log.info("getQnaInfoByNo()");
		
		return qnaMapper.selectQnaInfoByNo(bq_no);
		
	}
	
	// qna answers 가져오기 by bq_no
	public List<QnaAnswerDto> getQnaAnswerInfosByBqNo(int bq_no) {
		log.info("getQnaAnswerInfosByBqNo()");
		
		return qnaMapper.selectQnaAnswerInfosByBqNo(bq_no);
	}

	// qna 답변 확인
	@Transactional
	public boolean qnaAnswerConfirm(int bq_no, String bqa_answer, String a_id) {
		log.info("qnaAnswerConfirm()");
		
		Map<String, Object> params = new HashMap<>();
		params.put("bq_no", bq_no);
		params.put("bqa_answer", bqa_answer);
		params.put("a_id", a_id);
		
		try {
			
			int insertResult = qnaMapper.insertQnaAnswer(params);
			
			if (insertResult >= 0) {
				int updateResult = qnaMapper.updateQnaState(bq_no);
				
				if (updateResult >= 0) {
					return SqlResult.SUCCESS.getValue();
				} else {
					throw new RuntimeException("qnaAnswerConfirm() updateQnaState fail");
				}
				
			} else {
				return SqlResult.FAIL.getValue();
			}			
			
		} catch (Exception e) {
			log.info("qnaAnswerConfirm() Exception! ---- {}", e);
			
			return SqlResult.FAIL.getValue();
		}
		
	}

	public boolean answerModifyConfirm(int bqa_no, String bqa_answer) {
		log.info("answerModifyConfirm()");
		
		int result = qnaMapper.updateQnaAnswer(bqa_no, bqa_answer);
		
		if(result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();

	}

}
