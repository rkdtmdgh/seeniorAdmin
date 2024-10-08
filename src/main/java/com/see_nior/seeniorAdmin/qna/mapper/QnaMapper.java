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

	
}
