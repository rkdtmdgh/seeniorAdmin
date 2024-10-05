package com.see_nior.seeniorAdmin.advertisement;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/advertisement")
public class AdvertisementController {

	final private AdvertisementService advertisementService;
	
	// 광고 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "advertisement/create_form";
		
		return nextPage;
		
	}
	
	// 광고 등록 확인
	@ResponseBody
	@PostMapping("/info/create_confirm")
	public boolean createConfirm(AdvertisementDto advertisementDto) {
		log.info("createConfirm()");
		
		boolean createResult = advertisementService.createConfirm(advertisementDto);
		
		return createResult;
		
	}
	
	// 광고 리스트 양식
	@GetMapping("info/advertisement_list_form")
	public String advertisementListForm() {
		log.info("advertisementListForm()");
		
		String nextPage = "advertisement/advertisement_list_form";
		
		return nextPage;
		
	}
	
	
	// 모든 광고 가져오기(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("info/get_advertisement_list")
	public Object getAdvertisementList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "ad_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getAdvertisementList()");
		
		// 페이지 번호에 따른 광고 리스트들 가져오기
		Map<String, Object> advertisementListWithPage = advertisementService.getAdvertisementListWithPage(page, sortValue, order);
		
		// 질환 총 페이지 개수 가져오기
		Map<String, Object> advertisementListPageNum = advertisementService.getAdvertisementListPageNum(page);
		
		advertisementListWithPage.put("advertisementListPageNum", advertisementListPageNum);
		advertisementListWithPage.put("sortValue", sortValue);
		advertisementListWithPage.put("order", order);
		
		return advertisementListWithPage;
		
	}
	
}
