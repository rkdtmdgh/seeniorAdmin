package com.see_nior.seeniorAdmin.advertisement;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("info/get_position_list_select")
	public Object getPositionListSelect() {
		log.info("getPositionListSelect()");
		
		Map<String, Object> advertisementDtos = advertisementService.getPositionList();
		
		return advertisementDtos;
		
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
	
	// 광고 위치에 따른 광고 가져오기(페이지네이션)
	@ResponseBody
	@GetMapping("/info/get_advertisement_list_by_positon")
	public Object getAdvertisementListByPosition(
			@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "all") String sortValue,
			@RequestParam(value = "order") int position) {
		log.info("getAdvertisementListByPosition()");
		
		// 페이지 번호에 따른 위치별 광고 리스트들 가져오기
		Map<String, Object> advertisementListByPositionWithPage = advertisementService.getAdvertisementListByPositionWithPage(page, position);
		
		// 위치별 광고 총 페이지 개수 가져오기
		Map<String, Object> advertisementByPositionPageNum = advertisementService.getAdvertisementByPositionPageNum(page, position);
		
		advertisementListByPositionWithPage.put("advertisementByPositionPageNum", advertisementByPositionPageNum);
		advertisementListByPositionWithPage.put("sortValue", sortValue);
		advertisementListByPositionWithPage.put("order", position);
		
		return advertisementListByPositionWithPage;
		
	}
	
	
	// 광고 한개 가져오기
	@ResponseBody
	@GetMapping("/info/get_advertisement")
	public Object getAdvertisement(@RequestParam(value = "ad_no") int ad_no) {
		log.info("getAdvertisement()");
		
		AdvertisementDto advertisementDto = advertisementService.getAdvertisement(ad_no);
		
		return advertisementDto;
		
	}
	
	// 광고 수정 양식
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam(value = "ad_no") int ad_no, Model model) {
		log.info("modifyForm()");
		
		String nextPage = "advertisement/modify_form";
		
		AdvertisementDto advertisementDto = advertisementService.getAdvertisement(ad_no);
		
		model.addAttribute("advertisementDto", advertisementDto);
		
		return nextPage;
		
	}
	
	// 광고 수정 확인
	@ResponseBody
	@PostMapping("/info/modify_confirm")
	public boolean modifyConfirm(AdvertisementDto advertisementDto) {
		log.info("modifyConfirm()");
		
		boolean modifyResult = advertisementService.modifyConfirm(advertisementDto);
		
		return modifyResult;
		
	}
	
	// 광고 삭제 확인
	@ResponseBody
	@PostMapping("/info/delete_confirm")
	public boolean deleteConfirm(@RequestParam(value = "ad_nos") List<Integer> ad_nos) {
		log.info("deleteConfirm()");
		
		boolean deleteResult = advertisementService.deleteConfirm(ad_nos);
		
		return deleteResult;
		
	}
	
	// 광고 검색(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("info/search_advertisement_list")
	public Object searchAdvertisementList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchAdvertisementList()");
		
		// 페이지 번호에 따른 검색 광고 리스트들 가져오기
		Map<String, Object> searchAdvertisementListWithPage = advertisementService.getSearchAdvertisementListWithPage(searchPart, searchString, page);
		
		// 검색 광고 총 페이지 개수 가져오기
		Map<String, Object> searchAdvertisementListPageNum = advertisementService.getSearchAdvertisementListPageNum(searchPart, searchString, page);
		
		searchAdvertisementListWithPage.put("searchAdvertisementListPageNum", searchAdvertisementListPageNum);
		searchAdvertisementListWithPage.put("searchPart", searchPart);
		searchAdvertisementListWithPage.put("searchString", searchString);
		
		return searchAdvertisementListWithPage;
		
	}

	
	
}
