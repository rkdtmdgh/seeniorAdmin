package com.see_nior.seeniorAdmin.video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.VideoDto;
import com.see_nior.seeniorAdmin.video.mapper.VideoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class VideoService {
	
	final static public boolean CREATE_VIDEO_SUCCESS = true;
	final static public boolean CREATE_VIDEO_FAIL = false;
	
	private int pageLimit = 10;		// 한 페이지당 보여줄 정보 수
	private int blockLimit = 5;		// 하단에 보여질 페이지 번호 수

	final private VideoMapper videoMapper;
	
	// 비디오 리스트 가져오기
	public Map<String, Object> getVideoPagingList(String sortValue, String order, int page) {
		log.info("getVideoPagingList()");
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);

		List<AdminAccountDto> videoDtos = videoMapper.selectVideoList(pagingParams);
		pagingList.put("videoDtos", videoDtos);
		
		return pagingList;
	}

	// 비디오 리스트 총 개수
	public Map<String, Object> getVideoListPageNum(int page) {
		log.info("getVideoListPageNum()");
		
		Map<String, Object> videoListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회 
		int videoListCnt = videoMapper.selectAllVideoListCnt();

		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) videoListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		videoListPageNum.put("videoListCnt", videoListCnt);
		videoListPageNum.put("page", page);
		videoListPageNum.put("maxPage", maxPage);
		videoListPageNum.put("startPage", startPage);
		videoListPageNum.put("endPage", endPage);
		videoListPageNum.put("blockLimit", blockLimit);
		videoListPageNum.put("pageLimit", pageLimit);
		
		return videoListPageNum;
		
	}

	public Object createConfirm(VideoDto videoDto) {
		log.info("createConfirm()");
		
		int createRsult = videoMapper.insertNewVideo(videoDto);
		
		if (createRsult < 0) {
			return CREATE_VIDEO_SUCCESS;
		} 
		
		return CREATE_VIDEO_FAIL;
	}
	
}
