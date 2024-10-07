package com.see_nior.seeniorAdmin.advertisement.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

@Mapper
public interface AdvertisementMapper {

	// 광고 등록
	public int insertNewAdvertisement(AdvertisementDto advertisementDto);
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	public List<AdvertisementDto> getPositionList();

	// 페이지에 따른 광고 가져오기(모든 광고)
	public List<AdvertisementDto> getAdvertisementListWithPage(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(모든 광고)
	public int getAllAdvertisementCnt();
	
	// 페이지에 따른 광고 가져오기(위치별 광고)
	public List<AdvertisementDto> getAdvertisementListByPositionWithPage(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(위치별 광고)
	public int getAdvertisementByPositionCnt(int position);
	
	// 광고 한개 가져오기
	public AdvertisementDto getAdvertisementByNo(int ad_no);

	// 광고 수정 확인
	public int updateAdvertisement(AdvertisementDto advertisementDto);

	// 광고 삭제 확인
	public int deleteAdvertisement(int ad_no);

	// 페이지에 따른 광고 가져오기(검색한 광고)
	public List<AdvertisementDto> getSearchAdvertisement(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(검색한 광고)
	public int getSearchAdvertisementListCnt(Map<String, Object> pagingParams);


}
