package com.see_nior.seeniorAdmin.qna.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;

@Mapper
public interface QnaMapper {

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

	public boolean isQnaCategory(String bqc_name);
	
}
