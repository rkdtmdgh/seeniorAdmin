package com.see_nior.seeniorAdmin.advertisement.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

@Mapper
public interface AdvertisementMapper {

	// 광고 등록
	int insertNewAdvertisement(AdvertisementDto advertisementDto);

}
