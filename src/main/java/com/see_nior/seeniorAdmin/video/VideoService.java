package com.see_nior.seeniorAdmin.video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	final static public boolean MODIFY_VIDEO_SUCCESS = true;
	final static public boolean MODIFY_VIDEO_FAIL = false;
	
	final static public boolean DELETE_VIDEO_SUCCESS = true;
	final static public boolean DELETE_VIDEO_FAIL = false;
	
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
	
	// 비디오 검색 리스트 가져오기
	public Map<String, Object> searchVideoPagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchVideoPagingList()");
		log.info("searchPart ----- {}", searchPart);
		log.info("searchString ----- {}", searchString);
		log.info("sortValue ----- {}", sortValue);
		log.info("order ----- {}", order);
		log.info("page ----- {}", page);
		
		int pagingStart = (page - 1) * pageLimit;	
		
		Map<String, Object> pagingSearchList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);

		List<AdminAccountDto> videoDtos = videoMapper.selectSearchVideoList(pagingParams);
		pagingSearchList.put("videoDtos", videoDtos);
		
		log.info("videoDtos ------- {}", videoDtos);
		
		return pagingSearchList;
	}

	// 비디오 검색 리스트 총 개수
	public Map<String, Object> searchVideoListPageNum(String searchPart, String searchString, int page) {
		log.info("searchVideoPagingList()");
		
		Map<String, Object> searchVideoListPageNum = new HashMap<>();
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchVideoListCnt = videoMapper.selectSearchVideoListCnt(searchParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchVideoListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchVideoListPageNum.put("searchVideoListCnt", searchVideoListCnt);
		searchVideoListPageNum.put("page", page);
		searchVideoListPageNum.put("maxPage", maxPage);
		searchVideoListPageNum.put("startPage", startPage);
		searchVideoListPageNum.put("endPage", endPage);
		searchVideoListPageNum.put("blockLimit", blockLimit);
		searchVideoListPageNum.put("pageLimit", pageLimit);
		
		return searchVideoListPageNum;
		
	}
	
	// 비디오 등록 확인
	public Object createConfirm(VideoDto videoDto) {
		log.info("createConfirm()");
		
		int createRsult = videoMapper.insertNewVideo(videoDto);
		
		log.info("createRsult --- {}", createRsult);
		
		if (createRsult > 0) {
			return CREATE_VIDEO_SUCCESS;
		} 
		
		return CREATE_VIDEO_FAIL;
	}

	// 비디오 수정 확인
	public Object modifyConfirm(VideoDto videoDto) {
		log.info("modifyConfirm()");
		
		int updateResult =  videoMapper.updateVideoInfo(videoDto);
		
		if (updateResult > 0) {
			return MODIFY_VIDEO_SUCCESS;
		}
		
		return MODIFY_VIDEO_FAIL;
	}

	// 비디오 정보 가져오기
	public VideoDto getVideoInfo(int v_no) {
		log.info("getVideoInfo()");
		
		VideoDto videoDto = videoMapper.selectVideoInfoByNo(v_no);
		
		return videoDto;
	}

	// 비디오 삭제 확인
	@Transactional
	public Object deleteConfirm(List<Integer> v_nos) {
		log.info("deleteConfirm()");
		
		try {
			
			for (int v_no : v_nos) {
				
				int deleteResult = videoMapper.deleteConfirmByNo(v_no);
				
				if (deleteResult <= 0) {
					log.info("삭제 실패 : v_no -- {}", v_no);
					
					throw new RuntimeException("삭제 실패 : v_no -- " + v_no);
				}
				
			}
			
			return DELETE_VIDEO_SUCCESS;
			
		} catch (Exception e) {
			log.info("deleteConfirm error : {}", e);
			
			return DELETE_VIDEO_FAIL;
		}
		
	}
	
}
