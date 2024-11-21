package com.see_nior.seeniorAdmin.notice.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.DeleteNoticeDto;
import com.see_nior.seeniorAdmin.dto.NoticeDto;

@Mapper
public interface NoticeMapper {

	public NoticeDto selectNoticeInfoByNo(int n_no);

	public List<NoticeDto> selectNoticeList(Map<String, Object> pagingParams);

	public int selectAllNoticeListCnt();

	public List<NoticeDto> selectSearchNoticeList(Map<String, Object> searchPagingParams);

	public int selectSearchNoticeListCnt(Map<String, Object> searchParams);

	public List<NoticeDto> selectNoticeListForMain(int page_limit);

	public int insertNewNotice(NoticeDto noticeDto);

	public int updateNotice(NoticeDto newNoticeDto);

	public int updateIsDeletedByNo(int n_no);

	public List<DeleteNoticeDto> selectDeleteNoticeInfo();

}
