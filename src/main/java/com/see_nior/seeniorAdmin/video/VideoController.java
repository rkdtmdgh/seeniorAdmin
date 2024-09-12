package com.see_nior.seeniorAdmin.video;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.VideoDto;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/video")
public class VideoController {

	final private VideoService videoService;
	
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}
	
	// 비디오 리스트 양식
	@GetMapping("/list_form")
	public String listForm() {
		log.info("listForm()");
		
		String nextPage = "video/list_form";
		
		return nextPage;
	}
	
	// 비디오 리스트 가져오기 (비동기)
	@GetMapping("/get_video_list")
	@ResponseBody
	public Object getVideoList(
		@RequestParam(value = "d_name", required = false, defaultValue = "all") String d_name,
		@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("getVideoList()");
		
		Map<String, Object> videoList = videoService.getVideoPagingList(d_name, page);
		
		Map<String, Object> videoListPage = videoService.getVideoListPageNum(page);
		videoList.put("videoListPage", videoListPage);
		videoList.put("d_name", d_name);
		
		return videoList;
	
	}
	
	// 비디오 등록 양식
	@GetMapping("/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "video/create_form";
		
		return nextPage;
	}
	
	// 비디오 등록 확인 (비동기)
	@PostMapping("/create_confirm")
	@ResponseBody
	public Object createConfirm(VideoDto videoDto) {
		log.info("createConfirm()");
		
		return null;
	}
	
	
	// 비디오 수정 양식
	@GetMapping("/modify_form")
	public String modifyForm(@RequestParam("v_no") int v_no) {
		log.info("modifyForm()");
		
		String nextPage = "video/modify_form";
		
		return nextPage;
	}
	
	// 비디오 수정 확인 (비동기)
	@PostMapping("/modify_confirm")
	@ResponseBody
	public Object modifyConfirm(VideoDto videoDto) {
		log.info("modifyConfirm()");
		
		return null;
	}
	
	// 비디오 삭제 확인 (비동기)
	@PostMapping("/delete_confirm")
	@ResponseBody
	public Object deleteConfirm(@RequestParam("v_no") int v_no) {
		log.info("deleteConfirm()");
		
		return null;
	}
	
	// 비디오 리스트 검색 (비동기)
	@GetMapping("search_video")
	@ResponseBody
	public Object searchVideo() {
		log.info("searchVideo()");
		
		return null;
	}
	
	
}
