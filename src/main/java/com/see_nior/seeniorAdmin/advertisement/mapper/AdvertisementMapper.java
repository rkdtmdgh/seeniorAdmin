package com.see_nior.seeniorAdmin.advertisement.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdvertisementCategoryDto;
import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

@Mapper
public interface AdvertisementMapper {
	
	// -------------------------------------------광고 위치-------------------------------------------
	
	// 광고 위치 등록
	public int insertNewAdvertisementCategory(AdvertisementCategoryDto advertisementCategoryDto);
	
	// 광고 위치명 중복 확인
	public boolean isAdvertisementCategory(String ac_name);
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 select 박스)
	public List<AdvertisementCategoryDto> getAdvertisementCategoryList();
	
	// 광고 위치 별 광고의 개수 구하기
	public int getCategoryItemCnt(int ac_no);
	
	// 광고 위치 별 광고의 개수 광고 위치 테이블에 update
	public void updateAdvertisementCategoryItemCnt(Map<String, Object> itemCntParam);
	
	// 페이지에 따른 광고 위치 리스트 가져오기
	public List<AdvertisementCategoryDto> getAdvertisementCategoryListWithPage(Map<String, Object> pagingParams);
	
	// 광고 위치의 총 리스트 개수 구하기
	public int getAllAdvertisementCategoryCnt();
	
	// 광고 위치 한개 가져오기
	public AdvertisementCategoryDto getAdvertisementCategory(int ac_no);
	
	// 광고 위치 수정
	public int updateAdvertisementCategory(AdvertisementCategoryDto advertisementCategoryDto);
	
	// 광고 위치 삭제
	public int deleteAdvertisementCategory(int ac_no);
	
	// 페이지에 따른 광고 위치 가져오기(검색한 광고 위치)
	public List<AdvertisementCategoryDto> getSearchAdvertisementCategory(Map<String, Object> pagingParams);
	
	// 광고 위치의 총 페이지 개수 구하기(검색한 광고 위치)
	public int getSearchAdvertisementCategoryListCnt(Map<String, Object> pagingParams);
	
	// -------------------------------------------광고 -------------------------------------------

	// 해당 광고 위치의 광고 maxIdx값 구하기
	public Integer getAdvertisementIdxMaxNumByCategory(int ad_category_no);
	
	// idx값을 중간값으로 입력 시 나머지 idx들 +1 처리 하기
	public int updateAdvertisementIdx(AdvertisementDto advertisementDto);
	
	// 광고 등록
	public int insertNewAdvertisement(AdvertisementDto advertisementDto);

	// 페이지에 따른 광고 가져오기(모든 광고)
	public List<AdvertisementDto> getAdvertisementListWithPage(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(모든 광고)
	public int getAllAdvertisementCnt();
	
	// 페이지에 따른 광고 가져오기(위치별 광고)
	public List<AdvertisementDto> getAdvertisementListByCategoryWithPage(Map<String, Object> pagingParams);

	// 광고의 총 리스트 개수 구하기(위치별 광고)
	public int getAdvertisementByCategoryCnt(int ac_no);
	
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
