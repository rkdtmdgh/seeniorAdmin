package com.see_nior.seeniorAdmin.video.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@Mapper
public interface VideoMapper {

	public List<AdminAccountDto> selectVideoList(Map<String, Object> pagingParams);

	public int selectAllVideoListCnt();
	
}
