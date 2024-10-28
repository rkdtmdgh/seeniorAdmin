package com.see_nior.seeniorAdmin.qna.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaCategoryDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;

@Mapper
public interface QnaMapper {

	// qna
	public List<AdminAccountDto> selectQnaList(Map<String, Object> pagingParams);

	public int selectAllQnaListCnt();

	public List<QnaDto> selectUnansweredQuestions();

	public List<AdminAccountDto> selectSearchQnaList(Map<String, Object> pagingParams);

	public int selectSearchQnaListCnt(Map<String, Object> searchParams);

	public QnaDto selectQnaInfoByNo(int bq_no);

	public int updateQnaAnswer(int bqa_no, String bqa_answer);

	public List<AdminAccountDto> selectQnaNoticeList(Map<String, Object> pagingParams);

	public int selectAllQnaNoticeListCnt();

	public int insertNewAnswer(Map<String, Object> params);

	public int selectQnaAnswerLastNo();

	public int updateQnaStateByNo(Map<String, Object> updateParams);

	
	// category
	public boolean isQnaCategory(String bqc_name);

	public int insertNewQnaCategory(String bqc_name);

	public List<AdminAccountDto> selectQnaCategoryList(Map<String, Object> pagingParams);

	public int selectAllQnaCategoryListCnt();

	public List<AdminAccountDto> selectSearchQnaCategoryList(Map<String, Object> pagingParams);

	public int selectSearchQnaCategoryListCnt(Map<String, Object> searchParams);

	public QnaCategoryDto selectQnaCategoryDtoByNo(int bqc_no);

	public int updateQnaCategoryInfo(QnaCategoryDto qnaCategoryDto);

	public int updateQnaCategoryIsDeletedByNo(int bqc_no);
	
	
	// notice
	public List<AdminAccountDto> selectSearchQnaNoticeList(Map<String, Object> searchPagingParams);

	public int selectSearchQnaNoticeListCnt(Map<String, Object> searchParams);
	
}
