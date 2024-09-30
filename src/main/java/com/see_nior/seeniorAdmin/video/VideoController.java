package com.see_nior.seeniorAdmin.video;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.VideoDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/video")
public class VideoController {

	final private VideoService videoService;
	
	// 비디오 리스트 양식
	@GetMapping("/info/video_list_form")
	public String videoListForm() {
		log.info("videoListForm()");
		
		String nextPage = "video/video_list_form";
		
		return nextPage;
	}
	
	// 비디오 리스트 가져오기 (비동기)
	@GetMapping("/info/get_video_list")
	@ResponseBody
	public Object getVideoList(
			@RequestParam(value = "sortValue", required = false, defaultValue = "v_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getVideoList()");
		
		Map<String, Object> videoList = videoService.getVideoPagingList(sortValue, order, page);
		
		Map<String, Object> videoListPage = videoService.getVideoListPageNum(page);
		videoList.put("videoListPage", videoListPage);
		videoList.put("sortValue", sortValue);
		videoList.put("order", order);
		
		return videoList;
	
	}
	
	// 비디오 리스트 검색 (비동기)
	@GetMapping("/info/search_video_list")
	@ResponseBody
	public Object searchVideo(
			@RequestParam("searchPart") String searchPart,
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "sortValue", required = false, defaultValue = "v_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchVideoList()");
		
		Map<String, Object> searchVideoList =
				videoService.searchVideoPagingList(searchPart, searchString, sortValue, order, page);
		
		Map<String, Object> searchAdminListPage = 
				videoService.searchVideoListPageNum(searchPart, searchString, page);
		
		searchVideoList.put("searchAdminListPage", searchAdminListPage);
		searchVideoList.put("sortValue", sortValue);
		searchVideoList.put("order", order);
		searchVideoList.put("searchPart", searchPart);
		searchVideoList.put("searchString", searchString);
		
		return searchVideoList;
	}
	
	// 비디오 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "video/create_form";
		
		return nextPage;
	}
	
	// 비디오 등록 확인 (비동기)
	@PostMapping("/info/create_confirm")
	@ResponseBody
	public Object createConfirm(VideoDto videoDto) {
		log.info("createConfirm()");
		
		return videoService.createConfirm(videoDto);
	}
	
	
	// 비디오 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam("v_no") int v_no, Model model) {
		log.info("modifyForm()");
		
		String nextPage = "video/modify_form";
		
		VideoDto videoDto = videoService.getVideoInfo(v_no);
		model.addAttribute(videoDto);
		
		return nextPage;
	}
	
	// 비디오 수정 확인 (비동기)
	@PostMapping("/info/modify_confirm")
	@ResponseBody
	public Object modifyConfirm(VideoDto videoDto) {
		log.info("modifyConfirm()");
		
		return videoService.modifyConfirm(videoDto);
		
	}
	
	// 비디오 삭제 확인 (비동기)
	@PostMapping("/info/delete_confirm")
	@ResponseBody
	public Object deleteConfirm(@RequestParam("v_no") int v_no) {
		log.info("deleteConfirm()");
		
		return videoService.deleteConfirm(v_no);
		
	}
	
}
