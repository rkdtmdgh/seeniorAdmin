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
		
		int result = advertisementMapper.insertNewAdvertisement(advertisementDto);
		
		// DB에 입력 실패
		if (result <= 0) {
			return ADVERTISEMENT_CREATE_FAIL;
		
		// DB에 입력 성공
		} else {
			return ADVERTISEMENT_CREATE_SUCCESS;
					
		}
		
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
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1);
		
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
	
}
