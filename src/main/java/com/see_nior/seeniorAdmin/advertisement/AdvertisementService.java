package com.see_nior.seeniorAdmin.advertisement;

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
	
}
