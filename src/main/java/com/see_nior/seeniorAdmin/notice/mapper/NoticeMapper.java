package com.see_nior.seeniorAdmin.notice.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.NoticeDto;

@Mapper
public interface NoticeMapper {

	public NoticeDto selectNoticeInfoByNo(int n_no);

}
