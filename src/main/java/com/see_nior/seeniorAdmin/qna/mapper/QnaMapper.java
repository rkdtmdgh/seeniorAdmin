package com.see_nior.seeniorAdmin.qna.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.DeleteQnaoticeDto;
import com.see_nior.seeniorAdmin.dto.QnaCategoryDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.dto.QnaNoticeDto;

@Mapper
public interface QnaMapper {

	// qna
	public List<QnaDto> selectQnaList(Map<String, Object> pagingParams);

	public int selectAllQnaListCnt();
	
	public int selectUnansweredQnaCnt();

	public List<QnaDto> selectUnansweredQuestions();

	public List<QnaDto> selectSearchQnaList(Map<String, Object> pagingParams);

	public int selectSearchQnaListCnt(Map<String, Object> searchParams);
	
	public int selectUnansweredSearchQnaCnt(Map<String, Object> params);

	public QnaDto selectQnaInfoByNo(int bq_no);

	public int updateQnaAnswer(Map<String, Object> params);

	public List<QnaNoticeDto> selectQnaNoticeList(Map<String, Object> pagingParams);

	public int selectAllQnaNoticeListCnt();
	
	public List<QnaDto> selectQnaListForSelectBox(Map<String, Object> pagingParamsForSelectBox);

	public int selectAllQnaListCntForSelectBox(int bqc_no);
	
	public int selectUnansweredCategoryQnaCnt(int bqc_no);
	
	public int insertNewAnswer(Map<String, Object> params);

	public int selectQnaAnswerLastNo();

	public int updateQnaFromAnswerComplete(Map<String, Object> updateParams);
	
	public int updateQnaStateByNo(QnaDto qnaDto);

	public int updateQnaIsDeletedByNo(int bq_no);
	
	public int updateQnaAnswerIsDeletedByNo(int bqa_no);
	
	public int updateQnaBqAnswerNoDelete(int bq_no);
	
	public List<QnaDto> selectQnaListForMain(int page_limit);
	
	// category
	public boolean isQnaCategory(String bqc_name);

	public int insertNewQnaCategory(String bqc_name);

	public List<QnaCategoryDto> selectQnaCategoryList(Map<String, Object> pagingParams);

	public int selectAllQnaCategoryListCnt();

	public List<QnaCategoryDto> selectSearchQnaCategoryList(Map<String, Object> pagingParams);

	public int selectSearchQnaCategoryListCnt(Map<String, Object> searchParams);

	public QnaCategoryDto selectQnaCategoryDtoByNo(int bqc_no);

	public int updateQnaCategoryInfo(QnaCategoryDto qnaCategoryDto);

	public int updateQnaCategoryIsDeletedByNo(int bqc_no);
	
	public List<QnaCategoryDto> selectQnaCategoryListForSelectBox();
	
	// notice
	public List<QnaNoticeDto> selectSearchQnaNoticeList(Map<String, Object> searchPagingParams);

	public int selectSearchQnaNoticeListCnt(Map<String, Object> searchParams);

	public int insertNewQnaNotice(QnaNoticeDto qnaNoticeDto);

	public QnaNoticeDto selectQnaNoticeInfoByNo(int bqn_no);

	public int updateQnaNotice(QnaNoticeDto qnaNoticeDto);

	public int updateQnaNoticeIsDeletedByNo(int bqn_no);

	public List<QnaNoticeDto> selectQnaNoticeListForMain(int page_limit);

	public List<DeleteQnaoticeDto> selectDeleteQnaNoticeInfo();

	public int updateDeleteQnaNoticeIsDeleted(int dbqn_no);

}
