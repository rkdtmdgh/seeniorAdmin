package com.see_nior.seeniorAdmin.advertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.advertisement.mapper.AdvertisementMapper;
import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	// 광고 관련
	final static public boolean ADVERTISEMENT_CREATE_FAIL = false;		// 광고 추가 실패
	final static public boolean ADVERTISEMENT_CREATE_SUCCESS = true;	// 광고 추가 성공
	final static public boolean ADVERTISEMENT_MODIFY_FAIL = false;		// 광고 수정 실패
	final static public boolean ADVERTISEMENT_MODIFY_SUCCESS = true;	// 광고 수정 성공
	final static public boolean ADVERTISEMENT_DELETE_FAIL = false;		// 광고 삭제 실패
	final static public boolean ADVERTISEMENT_DELETE_SUCCESS = true;	// 광고 삭제 성공
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	final private AdvertisementMapper advertisementMapper;

	// 광고 등록 확인
	public boolean createConfirm(AdvertisementDto advertisementDto) {
		log.info("createConfirm()");
		
		int createResult = advertisementMapper.insertNewAdvertisement(advertisementDto);
		
		// DB에 입력 실패
		if (createResult <= 0) {
			return ADVERTISEMENT_CREATE_FAIL;
		
		// DB에 입력 성공
		} else {
			return ADVERTISEMENT_CREATE_SUCCESS;
					
		}
		
	}
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getPositionList() {
		log.info("getPositionList()");
		
		Map<String, Object> advertisementPositionDtos = new HashMap<>();
		
		List<AdvertisementDto> advertisementPositionDto = advertisementMapper.getPositionList();
		
		advertisementPositionDtos.put("advertisementPositionDto", advertisementPositionDto);
		
		return advertisementPositionDtos;
	}

	// 페이지에 따른 광고 가져오기(모든 광고)
	public Map<String, Object> getAdvertisementListWithPage(int page, String sortValue, String order) {
		log.info("getAdvertisementListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListWithPage(pagingParams);
		pagingList.put("advertisementDtos", advertisementDtos);
		
		return pagingList;
		
	}

	// 광고의 총 페이지 개수 구하기(모든 광고)
	public Map<String, Object> getAdvertisementListPageNum(int page) {
		log.info("getAdvertisementListPageNum()");
		
		Map<String, Object> advertisementListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int advertisementListCnt = advertisementMapper.getAllAdvertisementCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) advertisementListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		advertisementListPageNum.put("advertisementListCnt", advertisementListCnt);
		advertisementListPageNum.put("page", page);
		advertisementListPageNum.put("maxPage", maxPage);
		advertisementListPageNum.put("startPage", startPage);
		advertisementListPageNum.put("endPage", endPage);
		advertisementListPageNum.put("blockLimit", blockLimit);
		advertisementListPageNum.put("pageLimit", pageLimit);
		
		return advertisementListPageNum;
		
	}
	
	// 페이지에 따른 광고 가져오기(위치별 광고)
	public Map<String, Object> getAdvertisementListByPositionWithPage(int page, int position) {
		log.info("getAdvertisementListByPositionWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("position", position);
		
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListByPositionWithPage(pagingParams);
		pagingList.put("advertisementDtos", advertisementDtos);
		
		return pagingList;
		
	}
	
	// 광고의 총 페이지 개수 구하기 (위치별 광고)
	public Map<String, Object> getAdvertisementByPositionPageNum(int page, int position) {
		log.info("getAdvertisementByPositionPageNum()");
		
		Map<String, Object> advertisementListByPositionPageNum = new HashMap<>();
		
		// 전체 리스트 개수 주회
		int advertisementListByPositionCnt = advertisementMapper.getAdvertisementByPositionCnt(position);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) advertisementListByPositionCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		advertisementListByPositionPageNum.put("advertisementListByPositionCnt", advertisementListByPositionCnt);
		advertisementListByPositionPageNum.put("page", page);
		advertisementListByPositionPageNum.put("maxPage", maxPage);
		advertisementListByPositionPageNum.put("startPage", startPage);
		advertisementListByPositionPageNum.put("endPage", endPage);
		advertisementListByPositionPageNum.put("blockLimit", blockLimit);
		advertisementListByPositionPageNum.put("pageLimit", pageLimit);
		
		return advertisementListByPositionPageNum;
	}
	

	// 광고 한개 가져오기
	public AdvertisementDto getAdvertisement(int ad_no) {
		log.info("getAdvertisement()");
		
		AdvertisementDto advertisementDto = advertisementMapper.getAdvertisementByNo(ad_no);
		
		return advertisementDto;
		
	}

	// 광고 수정 확인
	public boolean modifyConfirm(AdvertisementDto advertisementDto) {
		log.info("modifyConfirm()");
		
		int modifyResult = advertisementMapper.updateAdvertisement(advertisementDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) {
			
			return ADVERTISEMENT_MODIFY_FAIL;
		}
		
		return false;
	}

	// 광고 삭제 확인
	public boolean deleteConfirm(List<Integer> ad_nos) {
		log.info("deleteConfirm()");
		
		try {
			
			for (int ad_no : ad_nos) {
				
				int deleteResult = advertisementMapper.deleteAdvertisement(ad_no);
				
				if (deleteResult <= 0) {
					log.info("삭제에 실패하였습니다 : ad_no ---> {}", ad_no);
					
					throw new RuntimeException();
					
				}
				
			}
			
		} catch (Exception e) {
			log.error("deleteConfirm Error : {}", e);
			
			return ADVERTISEMENT_DELETE_FAIL;
			
		}

		
		return ADVERTISEMENT_DELETE_SUCCESS;
	}

	// 페이지에 따른 광고 가져오기(검색한 광고)
	public Map<String, Object> getSearchAdvertisementListWithPage(String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		List<AdvertisementDto> searchAdvertisementDtos = advertisementMapper.getSearchAdvertisement(pagingParams);
		pagingList.put("advertisementDtos", searchAdvertisementDtos);
		
		return pagingList;
		
	}

	// 광고의 총 페이지 개수 구하기(검색한 광고)
	public Map<String, Object> getSearchAdvertisementListPageNum(String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementListPageNum()");
		
		Map<String, Object> searchAdvertisementListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchAdvertisementListCnt = advertisementMapper.getSearchAdvertisementListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchAdvertisementListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchAdvertisementListPageNum.put("searchAdvertisementListCnt", searchAdvertisementListCnt);
		searchAdvertisementListPageNum.put("page", page);
		searchAdvertisementListPageNum.put("maxPage", maxPage);
		searchAdvertisementListPageNum.put("startPage", startPage);
		searchAdvertisementListPageNum.put("endPage", endPage);
		searchAdvertisementListPageNum.put("blockLimit", blockLimit);
		searchAdvertisementListPageNum.put("pageLimiti", pageLimit);
		
		return searchAdvertisementListPageNum;
		
	}

	
	
}
