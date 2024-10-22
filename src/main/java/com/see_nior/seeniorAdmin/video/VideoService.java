package com.see_nior.seeniorAdmin.video;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.VideoDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.util.PagingUtil;
import com.see_nior.seeniorAdmin.video.mapper.VideoMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class VideoService {

	final private VideoMapper videoMapper;
	
	// 비디오 리스트 가져오기
	public Map<String, Object> getVideoPagingList(String sortValue, String order, int page) {
		log.info("getVideoPagingList()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> videoDtos = 
				videoMapper.selectVideoList(PagingUtil.pagingParams(sortValue, order, page));
		pagingList.put("videoDtos", videoDtos);
		
		return pagingList;
	}

	// 비디오 리스트 총 개수
	public Map<String, Object> getVideoListPageNum(int page) {
		log.info("getVideoListPageNum()");
		
		// 전체 리스트 개수 조회 
		int videoListCnt = videoMapper.selectAllVideoListCnt();
		
		return PagingUtil.pageNum("videoListCnt", videoListCnt, page);
		
	}
	
	// 비디오 검색 리스트 가져오기
	public Map<String, Object> searchVideoPagingList(String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("searchVideoPagingList()");
		
		Map<String, Object> pagingSearchList = new HashMap<>();

		List<VideoDto> videoDtos = 
				videoMapper.selectSearchVideoList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchList.put("videoDtos", videoDtos);
		
		return pagingSearchList;
	}

	// 비디오 검색 리스트 총 개수
	public Map<String, Object> searchVideoListPageNum(String searchPart, String searchString, int page) {
		log.info("searchVideoPagingList()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchVideoListCnt = videoMapper.selectSearchVideoListCnt(searchParams);
		
		return PagingUtil.pageNum("searchVideoListCnt", searchVideoListCnt, page);
		
	}
	
	// 비디오 등록 확인
	public boolean createConfirm(VideoDto videoDto) {
		log.info("createConfirm()");
		
		int createRsult = videoMapper.insertNewVideo(videoDto);
		
		if (createRsult > 0) {
			return SqlResult.SUCCESS.getValue();
		} 
		
		return SqlResult.FAIL.getValue();
	}

	// 비디오 수정 확인
	public boolean modifyConfirm(VideoDto videoDto) {
		log.info("modifyConfirm()");
		
		int updateResult =  videoMapper.updateVideoInfo(videoDto);
		
		if (updateResult > 0) {
			return SqlResult.SUCCESS.getValue();
		}
		
		return SqlResult.FAIL.getValue();
	}

	// 비디오 정보 가져오기
	public VideoDto getVideoInfo(int v_no) {
		log.info("getVideoInfo()");
		
		VideoDto videoDto = videoMapper.selectVideoInfoByNo(v_no);
		
		return videoDto;
	}

	// 비디오 삭제 확인
	@Transactional
	public boolean deleteConfirm(List<Integer> v_nos) {
		log.info("deleteConfirm()");
		
		try {
			
			for (int v_no : v_nos) {
				
				int deleteResult = videoMapper.deleteConfirmByNo(v_no);
				
				if (deleteResult <= 0) {
					log.info("삭제 실패 : v_no -- {}", v_no);
					
					throw new RuntimeException("삭제 실패 : v_no -- " + v_no);
				}
				
			}
			
			return SqlResult.SUCCESS.getValue();
			
		} catch (Exception e) {
			log.info("deleteConfirm error : {}", e);
			
			return SqlResult.FAIL.getValue();
		}
		
	}
	
}
