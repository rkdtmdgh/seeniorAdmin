package com.see_nior.seeniorAdmin.advertisement;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.dto.AdvertisementCategoryDto;
import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/advertisement")
public class AdvertisementController {

	final private AdvertisementService advertisementService;
	
	// --------------------------------------------------------- 광고 위치
	
	// 광고 위치 등록 양식
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		String nextPage = "advertisement/create_category_form";
		
		return nextPage;
		
	}
	
	// 광고 위치명 중복 확인
	@ResponseBody
	@GetMapping("/cate_info/is_advertisement_category")
	public boolean isAdvertisementCategory(@RequestParam(value = "ac_name") String ac_name) {
		log.info("isAdvertisementCategory()");
		
		boolean isAdvertisementCategory = advertisementService.isAdvertisementCategory(ac_name);
		
		return isAdvertisementCategory;
		
	}
	
	// 광고 위치 등록 확인
	@ResponseBody
	@PostMapping("/cate_info/create_category_confirm")
	public boolean createCategoryConfirmW(AdvertisementCategoryDto advertisementCategoryDto) {
		log.info("createCategoryConfirm()");
		
		boolean createCategoryResult = advertisementService.createCategoryConfirm(advertisementCategoryDto);
		
		return createCategoryResult;
		
	}
	
	// 광고 위치 리스트 양식
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		String nextPage = "advertisement/category_list_form";
		
		return nextPage;
		
	}
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list_select")
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> advertisementCategoryDtos = advertisementService.getCategoryList();
		
		return advertisementCategoryDtos;
		
	}

	// 모든 광고 위치 가져오기(페이지네이션 => 광고 위치 관리용 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/get_category_list")
	public Object getCategoryList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "ac_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getCategoryList()");
		
		// 페이지 번호에 따른 광고 위치 리스트들 가져오기
		Map<String, Object> advertisementCategoryListWithPage = advertisementService.getCategoryListWithPage(page, sortValue, order);
		
		// 광고 위치 총 페이지 개수 가져오기
		Map<String, Object> advertisementCategoryListPageNum = advertisementService.getAdvertisementCategoryListPageNum(page);
		
		advertisementCategoryListWithPage.put("advertisementCategoryListPageNum", advertisementCategoryListPageNum);
		advertisementCategoryListWithPage.put("sortValue", sortValue);
		advertisementCategoryListWithPage.put("order", order);
		
		return advertisementCategoryListWithPage;
		
	}
	
	// 광고 위치 수정 양식
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(@RequestParam(value = "ac_no") int ac_no, Model model) {
		log.info("modifyCategoryForm()");
		
		String nextPage = "advertisement/modify_category_form";
		
		AdvertisementCategoryDto advertisementCategoryDto = advertisementService.getCategory(ac_no);
		
		model.addAttribute("advertisementCategoryDto", advertisementCategoryDto);
		
		return nextPage;
		
	}
	
	// 광고 위치 수정 확인(비동기)
	@ResponseBody
	@PostMapping("/cate_info/modify_category_confirm")
	public boolean modifyCategoryConfirm(AdvertisementCategoryDto advertisementCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		boolean modifyCategoryResult = advertisementService.modifyCategoryConfirm(advertisementCategoryDto);
		
		return modifyCategoryResult;
		
	}
	
	// 광고 위치 삭제 확인(비동기)
	@ResponseBody
	@PostMapping("/cate_info/delete_category_confirm")
	public boolean deleteCategoryConfirm(@RequestParam(value = "ac_no") int ac_no) {
		log.info("deleteCategoryConfirm()");
		
		boolean deleteCategoryResult = advertisementService.deleteCategoryConfirm(ac_no);
		
		return deleteCategoryResult;
		
	}
	
	// 광고 위치 검색(페이지네이션 => 비동기)
	@ResponseBody
	@GetMapping("/cate_info/search_advertisement_category_list")
	public Object searchAdvertisementCategoryList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchAdvertisementCategoryList()");
		
		// 페이지 번호에 따른 검색 광고 위치 리스트들 가져오기
		Map<String, Object> searchAdvertisementCategoryListWithPage = advertisementService.getSearchAdvertisementCategoryListWithPage(searchPart, searchString, page);
		
		// 검색 광고 위치 총 페이지 개수 가져오기
		Map<String, Object> searchAdvertisementCategoryListPageNum = advertisementService.getSearchAdvertisementCategoryListPageNum(searchPart, searchString, page);
		
		searchAdvertisementCategoryListWithPage.put("searchAdvertisementCategoryListPageNum", searchAdvertisementCategoryListPageNum);
		searchAdvertisementCategoryListWithPage.put("searchPart", searchPart);
		searchAdvertisementCategoryListWithPage.put("searchString", searchString);
		
		return searchAdvertisementCategoryListWithPage;
		
	}
	
	// --------------------------------------------------------- 광고
	
	// 광고 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "advertisement/create_form";
		
		return nextPage;
		
	}
	
	// 광고 등록 양식에서 광고를 등록할 위치를 선택 했을 시 해당 위치의 maxIdx 가져오기
	@ResponseBody
	@GetMapping("/info/create_category_select")
	public Object createCategorySelect(@RequestParam(value = "ad_category_no") int ad_category_no) {
		log.info("createCategorySelect()");
		
		int advertisementIdxMaxNum = advertisementService.getAdvertisementIdxMaxNum(ad_category_no);
		
		return advertisementIdxMaxNum;
		
	}
	
	// 광고 등록 확인
	@ResponseBody
	@PostMapping("/info/create_confirm")
	public boolean createConfirm(
			@RequestParam(value = "files") MultipartFile file,
			AdvertisementDto advertisementDto) {
		log.info("createConfirm()");
		log.info("file ---> {}", file);
		log.info("advertisementDto ---->{}", advertisementDto.getAd_idx());
		
		// 이미지 서버에 저장된 이미지 파일 이름 가져오기
		ResponseEntity<String> savedFile = advertisementService.uploadFile(file, advertisementDto);
		log.info("savedFile ========> {}", savedFile);
		
		if (savedFile != null) {
			log.info("uploadFile SUCCESS!!");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody(), new TypeReference<Map<String, Object>>() {});
				
				String ad_dir_name = (String) savedFileObj.get("dir_name");
				String savedFileName = (String) savedFileObj.get("savedFileName");
				log.info("ad_dir_name ----> {}", ad_dir_name);
				log.info("savedFileName ----> {}", savedFileName);
				
				boolean createResult = advertisementService.createConfirm(advertisementDto, ad_dir_name, savedFileName);
				
				return createResult;
				
			} catch (JsonMappingException e) {
				log.info("JsonMappingException!!");
				e.printStackTrace();
				
				return false;
				
			} catch (JsonProcessingException e) {
				log.info("JsonProcessingException!!");
				e.printStackTrace();
				
				return false;
				
			}
			
		} else {
			log.info("upload file fail!!");
			
			return false;
			
		}
		
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
	
	// 광고 위치에 따른 광고 가져오기(페이지네이션)
	@ResponseBody
	@GetMapping("/info/get_advertisement_list_by_category")
	public Object getAdvertisementListByCategory(
			@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "all") String sortValue,
			@RequestParam(value = "order") int ac_no) {
		log.info("getAdvertisementListByCategory()");
		
		// 페이지 번호에 따른 위치별 광고 리스트들 가져오기
		Map<String, Object> advertisementListByCategoryWithPage = advertisementService.getAdvertisementListByCategoryWithPage(page, ac_no);
		
		// 위치별 광고 총 페이지 개수 가져오기
		Map<String, Object> advertisementByCategoryPageNum = advertisementService.getAdvertisementByCategoryPageNum(page, ac_no);
		
		advertisementListByCategoryWithPage.put("advertisementByCategoryPageNum", advertisementByCategoryPageNum);
		advertisementListByCategoryWithPage.put("sortValue", sortValue);
		advertisementListByCategoryWithPage.put("order", ac_no);
		
		return advertisementListByCategoryWithPage;
		
	}
	
	// 광고 순서 변경(광고 위치 디테일뷰에서)
	@PostMapping("/info/modify_advertisement_idx")
	@ResponseBody
	public boolean modifyAdvertisementIdx(
			@RequestParam(value = "ac_no")int ac_no,
			@RequestParam(value = "ad_no")int ad_no,
			@RequestParam(value = "current_ad_idx") int current_ad_idx,
			@RequestParam(value = "ad_idx") int ad_idx) {
		log.info("modifyAdvertisementIdx()");
		
		boolean modifyIdxResult = advertisementService.modifyAdvertisementIdx(ac_no, ad_no, current_ad_idx, ad_idx);
		
		return modifyIdxResult;
		
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
	public boolean modifyConfirm(
			@RequestParam(value = "files", required = false) MultipartFile file,
			AdvertisementDto advertisementDto) {
		log.info("modifyConfirm()");
		log.info("files -------> {}", file);
		
		// 사진 변경 시 => 프론트에서 어떻게 넘어오는지 확인하고, 조건 처리 해아함
		if (file != null && file.getSize() != 0) {
			
			// 이미지 서버에 저장된 이미지 파일 이름 가져오기
			ResponseEntity<String> savedFile = advertisementService.uploadFile(file, advertisementDto);
			log.info("savedFile ========> {}", savedFile);
			
			if (savedFile != null) {
				log.info("uploadFile SUCCESS!!");
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody(), new TypeReference<Map<String, Object>>() {});
					
					String ad_dir_name = (String) savedFileObj.get("dir_name");
					String savedFileName = (String) savedFileObj.get("savedFileName");
					log.info("ad_dir_name ----> {}", ad_dir_name);
					log.info("savedFileName ----> {}", savedFileName);
					
					boolean modifyResult = advertisementService.modifyConfirm(advertisementDto, ad_dir_name, savedFileName);
					
					return modifyResult;
					
				} catch (JsonMappingException e) {
					log.info("JsonMappingException!!");
					e.printStackTrace();
					
					return false;
					
				} catch (JsonProcessingException e) {
					log.info("JsonProcessingException!!");
					e.printStackTrace();
					
					return false;
					
				}
				
			} else {
				log.info("upload file fail!!");
				
				return false;
				
			}
			
		// 사진 변경이 없을 시
		} else {
			
			boolean modifyResult = advertisementService.modifyConfirm(advertisementDto, null, null);
			
			return modifyResult;
			
		}
		
	}
	
	// 광고 삭제 확인
	@ResponseBody
	@PostMapping("/info/delete_confirm")
	public boolean deleteConfirm(@RequestParam(value = "ad_no") int ad_no) {
		log.info("deleteConfirm()");
		
		boolean deleteResult = advertisementService.deleteConfirm(ad_no);
		
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
