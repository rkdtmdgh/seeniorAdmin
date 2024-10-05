package com.see_nior.seeniorAdmin.advertisement.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

@Mapper
public interface AdvertisementMapper {

	// 광고 등록
	int insertNewAdvertisement(AdvertisementDto advertisementDto);

	// 페이지에 따른 광고 가져오기(모든 광고)
	List<AdvertisementDto> getAdvertisementListWithPage(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(모든 광고)
	int getAllAdvertisementCnt();

}
