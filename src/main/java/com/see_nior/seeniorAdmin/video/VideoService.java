package com.see_nior.seeniorAdmin.video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.video.mapper.VideoMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VideoService {
	
	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수

	final private VideoMapper videoMapper;
	
	public VideoService(VideoMapper videoMapper) {
		this.videoMapper = videoMapper;
		
	}
	
	// 비디오 리스트 가져오기
	public Map<String, Object> getVideoPagingList(String d_name, int page) {
		log.info("getVideoPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("d_name", d_name);

		List<AdminAccountDto> videoDtos = videoMapper.selectVideoList(pagingParams);
		pagingList.put("videoDtos", videoDtos);
		
		return pagingList;
	}

	// 비디오 리스트 총 개수
	public Map<String, Object> getVideoListPageNum(int page) {
		log.info("getVideoListPageNum()");
		
		
		return null;
	}
	
}
