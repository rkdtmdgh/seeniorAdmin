package com.see_nior.seeniorAdmin.qna;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaCategoryDto;
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
	final private AccountMapper accountMapper;
	
	// qna 리스트 가져오기
	public Map<String, Object> getQnaPagingList(String sortValue, String order, int page) {
		log.info("getQnaPagingList()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> qnaDtos = 
				qnaMapper.selectQnaList(PagingUtil.pagingParams(sortValue, order, page));
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
	
	// qna 답변 확인
	@Transactional
	public boolean qnaAnswerConfirm(int bq_no, String bqa_answer, String a_id) {
		log.info("qnaAnswerConfirm()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountById(a_id);
		
		Map<String, Object> insertParams = new HashMap<>();
		insertParams.put("bqa_answer", bqa_answer);
		insertParams.put("a_id", a_id);
		insertParams.put("a_no", adminAccountDto.getA_no());

		try {
			
			int insertResult = qnaMapper.insertNewAnswer(insertParams);
			
			if (insertResult >= 0) {
				
				int bqa_no = qnaMapper.selectQnaAnswerLastNo();
				
				Map<String, Object> updateParams = new HashMap<>();
				updateParams.put("bq_no", bq_no);
				updateParams.put("bqa_no", bqa_no);
				
				int updateResult = 
						qnaMapper.updateQnaStateByNo(updateParams);
				
				if (updateResult >= 0) {
					
					return SqlResult.SUCCESS.getValue();
					
				} else {
					
					throw new RuntimeException("updateQnaStateByNo() error!!");
					
				}
				
			} else {
				
				throw new RuntimeException("updateQnaStateByNo() error!!");
				
			}
			
		} catch (Exception e) {
			log.info("qnaAnswerConfirm Exception ------ {}", e);
			
			return SqlResult.FAIL.getValue();
		
		}

	}

	// qna 답변 수정 확인
	public boolean answerModifyConfirm(String a_id, String loginedId, int bqa_no, String bqa_answer) {
		log.info("answerModifyConfirm()");
		
		AdminAccountDto adminAccountDto =
				accountMapper.selectAdminAccountById(loginedId);
		
		if ((adminAccountDto != null && adminAccountDto.getA_authority_role().equals("SUPER_ADMIN")) 
				|| a_id.equals(loginedId)) {
			
			int result = qnaMapper.updateQnaAnswer(bqa_no, bqa_answer);
			
			if(result >= 0)
				return SqlResult.SUCCESS.getValue();
			else 
				return SqlResult.FAIL.getValue();
			
		} else {
			
			return SqlResult.FAIL.getValue();
			
		}

	}

	
	/////////// 카테고리
	
	// qna 카테고리명 중복 확인
	public boolean isQnaCategory(String bqc_name) {
		log.info("isQnaCategory()");
		
		return qnaMapper.isQnaCategory(bqc_name);
		
	}

	// qna 카테고리 등록 확인
	public boolean createCategoryConfirm(String bqc_name) {
		log.info("createCategoryConfirm()");
		
		boolean isQna = qnaMapper.isQnaCategory(bqc_name);
		
		if (!isQna) {
			
			return SqlResult.FAIL.getValue();
			
		} else {
			
			int result = qnaMapper.insertNewQnaCategory(bqc_name);
			
			if (result >= 0) 
				return SqlResult.SUCCESS.getValue();
			else 
				return SqlResult.FAIL.getValue();
			
		}
		
	}

	// qna 카테고리 페이징 리스트 가져오기
	public Map<String, Object> getQnaCategoryPagingList(String sortValue, String order, int page) {
		log.info("getQnaCategoryPagingList()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<AdminAccountDto> qnaCategoryDtos = 
				qnaMapper.selectQnaCategoryList(PagingUtil.pagingParams(sortValue, order, page));
		pagingCategoryList.put("qnaCategoryDtos", qnaCategoryDtos);
		
		return pagingCategoryList;
		
	}

	// qna 카테고리 리스트 총 개수
	public Map<String, Object> getQnaCategoryListPageNum(int page) {
		log.info("getQnaCategoryListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaCategoryListCnt = qnaMapper.selectAllQnaCategoryListCnt();
		
		return PagingUtil.pageNum("qnaCategoryListCnt", qnaCategoryListCnt, page);

	}

	// qna 카테고리 검색 리스트 가져오기
	public Map<String, Object> searchQnaCategoryPagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaCategoryPagingList()");
		
		Map<String, Object> pagingSearchCategoryList = new HashMap<>();
		
		List<AdminAccountDto> qnaCategoryDtos = 
				qnaMapper.selectSearchQnaCategoryList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchCategoryList.put("qnaCategoryDtos", qnaCategoryDtos);
		
		return pagingSearchCategoryList;
		
	}

	// qna 카테고리 검색 리스트 총 개수
	public Map<String, Object> searchQnaCategoryListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaCategoryListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaCategoryListCnt = qnaMapper.selectSearchQnaCategoryListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaCategoryListCnt", searchQnaCategoryListCnt, page);
		
	}

	// qna 카테고리 정보 가져오기 by no
	public QnaCategoryDto getQnaCategoryDtoByNo(int bqc_no) {
		log.info("getQnaCategoryDtoByNo()");
		
		return qnaMapper.selectQnaCategoryDtoByNo(bqc_no);
		
	}

	// qna 카테고리 수정 확인
	public boolean modifyCategoryConfirm(QnaCategoryDto qnaCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		int result = qnaMapper.updateQnaCategoryInfo(qnaCategoryDto);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}

	// qna 카테고리 삭제 확인
	public boolean deleteCategoryConfirm(int bqc_no) {
		log.info("deleteCategoryConfirm()");
		
		int result = qnaMapper.updateQnaCategoryIsDeletedByNo(bqc_no);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}
	
	
	
	////////////// 공지사항
	
	// qna 공지사항 가져오기
	public Map<String, Object> getQnaNoticePagingList(String sortValue, String order, int page) {
		log.info("getQnaNoticePagingList()");
		
		Map<String, Object> pagingNoticeList = new HashMap<>();
		
		List<AdminAccountDto> qnaNoticeDtos = 
				qnaMapper.selectQnaNoticeList(PagingUtil.pagingParams(sortValue, order, page));
		pagingNoticeList.put("qnaNoticeDtos", qnaNoticeDtos);
		
		return pagingNoticeList;
		
	}

	// qna 공지사항 총 개수
	public Map<String, Object> getQnaNoticeListPageNum(int page) {
		log.info("getQnaNoticeListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaNoticeListCnt = qnaMapper.selectAllQnaNoticeListCnt();
		
		return PagingUtil.pageNum("qnaNoticeListCnt", qnaNoticeListCnt, page);
		
	}

	// qna 공지사항 검색 리스트 가져오기
	public Map<String, Object> searchQnaNoticePagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaNoticePagingList()");
		
		Map<String, Object> pagingSearchQnaNoticeList = new HashMap<>();
		
		List<AdminAccountDto> qnaNoticeDtos = 
				qnaMapper.selectSearchQnaNoticeList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchQnaNoticeList.put("qnaNoticeDtos", qnaNoticeDtos);
		
		return pagingSearchQnaNoticeList;
		
	}

	// qna 공지사항 검색 리스트 총 개수 
	public Map<String, Object> searchQnaNoticeListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaNoticeListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaNoticeListCnt = qnaMapper.selectSearchQnaNoticeListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaNoticeListCnt", searchQnaNoticeListCnt, page);
		
	}

}
