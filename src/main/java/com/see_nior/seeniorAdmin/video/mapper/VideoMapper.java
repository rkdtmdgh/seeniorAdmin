package com.see_nior.seeniorAdmin.video.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.VideoDto;

@Mapper
public interface VideoMapper {

	public List<AdminAccountDto> selectVideoList(Map<String, Object> pagingParams);

	public int selectAllVideoListCnt();

	public int insertNewVideo(VideoDto videoDto);

	public List<VideoDto> selectSearchVideoList(Map<String, Object> pagingParams);

	public int selectSearchVideoListCnt(Map<String, Object> searchParams);

	public int updateVideoInfo(VideoDto videoDto);

	public VideoDto selectVideoInfoByNo(int v_no);

	public int deleteConfirmByNo(int v_no);
	
}
