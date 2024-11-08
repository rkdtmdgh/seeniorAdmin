package com.see_nior.seeniorAdmin.notice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.dto.QnaNoticeDto;

@Mapper
public interface NoticeMapper {

	public NoticeDto selectNoticeInfoByNo(int n_no);

	public List<AdminAccountDto> selectNoticeList(Map<String, Object> pagingParams);

	public int selectAllNoticeListCnt();

	public List<AdminAccountDto> selectSearchNoticeList(Map<String, Object> searchPagingParams);

	public int selectSearchNoticeListCnt(Map<String, Object> searchParams);

	public List<QnaNoticeDto> selectNoticeListForMain(int page_limit);

	public int insertNewNotice(NoticeDto noticeDto);

}
