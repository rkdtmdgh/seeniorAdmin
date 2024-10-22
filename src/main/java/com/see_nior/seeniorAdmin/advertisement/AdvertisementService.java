package com.see_nior.seeniorAdmin.advertisement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.see_nior.seeniorAdmin.advertisement.mapper.AdvertisementMapper;
import com.see_nior.seeniorAdmin.dto.AdvertisementCategoryDto;
import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	// 이미지 서버 경로
	private String imgServerPath = "127.0.0.1:8091/seeniorUploadImg/";
	
	// 광고 위치 관련
	final static public boolean ADVERTISEMENT_CATEGORY_CREATE_FAIL = false;		// 광고 위치 추가 실패
	final static public boolean ADVERTISEMENT_CATEGORY_CREATE_SUCCESS = true;	// 광고 위치 추가 성공
	final static public boolean ADVERTISEMENT_CATEGORY_MODIFY_FAIL = false;		// 광고 위치 수정 실패
	final static public boolean ADVERTISEMENT_CATEGORY_MODIFY_SUCCESS = true;	// 광고 위치 수정 성공
	final static public boolean ADVERTISEMENT_CATEGORY_DELETE_FAIL = false;		// 광고 위치 삭제 실패
	final static public boolean ADVERTISEMENT_CATEGORY_DELETE_SUCCESS = true;	// 광고 위치 삭제 성공
	
	// 광고 관련
	final static public boolean ADVERTISEMENT_CREATE_FAIL = false;			// 광고 추가 실패
	final static public boolean ADVERTISEMENT_CREATE_SUCCESS = true;		// 광고 추가 성공
	final static public boolean ADVERTISEMENT_MODIFY_FAIL = false;			// 광고 수정 실패
	final static public boolean ADVERTISEMENT_MODIFY_SUCCESS = true;		// 광고 수정 성공
	final static public boolean ADVERTISEMENT_IDX_MODIFY_FAIL = false;		// 광고 idx 수정 실패
	final static public boolean ADVERTISEMENT_IDX_MODIFY_SUCCESS = true;	// 광고 idx  수정 성공
	final static public boolean ADVERTISEMENT_DELETE_FAIL = false;			// 광고 삭제 실패
	final static public boolean ADVERTISEMENT_DELETE_SUCCESS = true;		// 광고 삭제 성공
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	final private AdvertisementMapper advertisementMapper;
	final private RestTemplate restTemplate;
	
	// --------------------------------------------------------- 광고 위치
	
	// 광고 위치명 중복 확인
	public boolean isAdvertisementCategory(String ac_name) {
		log.info("isAdvertisementCategory()");
		
		boolean isAdvertisementCategory = advertisementMapper.isAdvertisementCategory(ac_name);
		
		return isAdvertisementCategory;
		
	}
	
	// 광고 위치 추가 확인
	public boolean createCategoryConfirm(AdvertisementCategoryDto advertisementCategoryDto) {
		log.info("createCategoryConfirm()");
		
		int createResult = advertisementMapper.insertNewAdvertisementCategory(advertisementCategoryDto);
		
		// DB에 입력 실패
		if (createResult <= 0) {
			
			return ADVERTISEMENT_CATEGORY_CREATE_FAIL;
			
		// DB에 입력 성공
		} else {
			
			return ADVERTISEMENT_CATEGORY_CREATE_SUCCESS;
			
		}		
		
	}
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> advertisementCategoryDtos = new HashMap<>();
		
		List<AdvertisementCategoryDto> advertisementCategoryDto = (List<AdvertisementCategoryDto>) advertisementMapper.getAdvertisementCategoryList();
		
		advertisementCategoryDtos.put("advertisementCategoryDto", advertisementCategoryDto);	
				
		return advertisementCategoryDtos;
		
	}
	
	// 페이지에 따른 광고 위치 리스트 가져오기
	public Map<String, Object> getCategoryListWithPage(int page, String sortValue, String order) {
		log.info("getCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<AdvertisementCategoryDto> advertisementCategoryDtos = advertisementMapper.getAdvertisementCategoryListWithPage(pagingParams);
		
		for (AdvertisementCategoryDto advertisementCategoryDto : advertisementCategoryDtos) {
			Map<String, Object> itemCntParam = new HashMap<>();
			int ac_no = advertisementCategoryDto.getAc_no();
			int item_cnt = advertisementMapper.getCategoryItemCnt(ac_no);
			itemCntParam.put("ac_no", ac_no);
			itemCntParam.put("item_cnt", item_cnt);
			advertisementMapper.updateAdvertisementCategoryItemCnt(itemCntParam);
			advertisementCategoryDto.setAc_item_cnt(item_cnt);
			
		}
		
		pagingList.put("advertisementCategoryDtos", advertisementCategoryDtos);
		
		return pagingList;
		
	}
	
	// 광고 위치의 총 페이지 개수 구하기
	public Map<String, Object> getAdvertisementCategoryListPageNum(int page) {
		log.info("getAdvertisementCategoryListPageNum()");
		
		Map<String, Object> advertisementCategoryListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int advertisementCategoryListCnt = advertisementMapper.getAllAdvertisementCategoryCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) advertisementCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산 
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		advertisementCategoryListPageNum.put("advertisementCategoryListCnt", advertisementCategoryListCnt);
		advertisementCategoryListPageNum.put("page", page);
		advertisementCategoryListPageNum.put("maxPage", maxPage);
		advertisementCategoryListPageNum.put("startPage", startPage);
		advertisementCategoryListPageNum.put("endPage", endPage);
		advertisementCategoryListPageNum.put("blockLimit", blockLimit);
		advertisementCategoryListPageNum.put("pageLimit", pageLimit);
		
		return advertisementCategoryListPageNum;
		
	}
	
	// 광고 위치 한개 가져오기
	public AdvertisementCategoryDto getCategory(int ac_no) {
		log.info("getCategory()");
		
		AdvertisementCategoryDto advertisementCategoryDto = advertisementMapper.getAdvertisementCategory(ac_no);
		
		return advertisementCategoryDto;
		
	}
	
	// 광고 위치 수정 확인
	public boolean modifyCategoryConfirm(AdvertisementCategoryDto advertisementCategoryDto) {
		log.info("modifyCategoryConfirm()");
			
		int modifyResult = advertisementMapper.updateAdvertisementCategory(advertisementCategoryDto);
		
		// DB에 입력 실패
		if (modifyResult <= 0) {
			
			return ADVERTISEMENT_CATEGORY_MODIFY_FAIL;
			
		// DB에 입력 성공
		} else {
			
			return ADVERTISEMENT_CATEGORY_MODIFY_SUCCESS;
			
		}
	
	}
	
	// 광고 위치 삭제 확인
	public boolean deleteCategoryConfirm(int ac_no) {
		log.info("deleteCategoryConfirm()");
		
		int deleteResult = advertisementMapper.deleteAdvertisementCategory(ac_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) {
			
			return ADVERTISEMENT_CATEGORY_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			
			return ADVERTISEMENT_CATEGORY_DELETE_SUCCESS;
			
		}
		
	}
	
	// 페이지에 따른 광고 위치 가져오기(검색한 광고 위치)
	public Map<String, Object> getSearchAdvertisementCategoryListWithPage(String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		List<AdvertisementCategoryDto> searchAdvertisementCategoryDtos = advertisementMapper.getSearchAdvertisementCategory(pagingParams);
		
		for (AdvertisementCategoryDto advertisementCategoryDto : searchAdvertisementCategoryDtos) {
			Map<String, Object> itemCntParam = new HashMap<>();
			int ac_no = advertisementCategoryDto.getAc_no();
			int item_cnt = advertisementMapper.getCategoryItemCnt(ac_no);
			itemCntParam.put("ac_no", ac_no);
			itemCntParam.put("item_cnt", item_cnt);
			advertisementMapper.updateAdvertisementCategoryItemCnt(itemCntParam);
			advertisementCategoryDto.setAc_item_cnt(item_cnt);
			
		}
		
		pagingList.put("advertisementCategoryDtos", searchAdvertisementCategoryDtos);
		
		return pagingList;
		
	}
	
	// 광고 카테고리의 총 페이지 개수 구하기(검색한 광고 카테고리)
	public Map<String, Object> getSearchAdvertisementCategoryListPageNum(String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementCategoryListPageNum()");
		
		Map<String, Object> searchAdvertisementCategoryListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchAdvertisementCategoryListCnt = advertisementMapper.getSearchAdvertisementCategoryListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchAdvertisementCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchAdvertisementCategoryListPageNum.put("searchAdvertisementCategoryListCnt", searchAdvertisementCategoryListCnt);
		searchAdvertisementCategoryListPageNum.put("page", page);
		searchAdvertisementCategoryListPageNum.put("maxPage", maxPage);
		searchAdvertisementCategoryListPageNum.put("startPage", startPage);
		searchAdvertisementCategoryListPageNum.put("endPage", endPage);
		searchAdvertisementCategoryListPageNum.put("blockLimit", blockLimit);
		searchAdvertisementCategoryListPageNum.put("pageLimit", pageLimit);
		
		return searchAdvertisementCategoryListPageNum;
		
	}
	
	
	
	
	// --------------------------------------------------------- 광고

	// 광고 이미지 저장 후 이미지 이름 가져오기
	public ResponseEntity<String> uploadFile(MultipartFile file, AdvertisementDto advertisementDto) {
		log.info("uploadFile()");
		log.info("file ---> {}", file);
		
		try {
			
			// Request Header 설정
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			
			// Request Body 설정
			MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
			
			requestBody.add("file", file.getResource());
			requestBody.add("ad_category_no", advertisementDto.getAd_category_no());
			
			// Request Entity
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);		
			
			// API 호출
//			String serverURL = "http://14.42.124.93:8091/advertisement_upload_file";
			String serverURL = "http://localhost:8091/advertisement_upload_file";	// local
			ResponseEntity<String> response = restTemplate.postForEntity(serverURL, requestEntity, String.class);
			
			return response;
			
		} catch (Exception e) {
			log.info("광고 이미지 파일 업로드 중 오류 발생 : {}", e.getMessage());
			
			return null;
			
		}
		
	}
	
	// 광고 등록 양식에서 광고를 등록할 위치를 선택 했을 시 해당 위치의 maxIdx 가져오기
	public int getAdvertisementIdxMaxNum(int ad_category_no) {
		log.info("getAdvertisementIdxMaxNum()");
		
		Integer advertisementIdxMaxNum = advertisementMapper.getAdvertisementIdxMaxNumByCategory(ad_category_no);
		
		if (advertisementIdxMaxNum == null) advertisementIdxMaxNum = 0;
		
		return advertisementIdxMaxNum;
	}
	
	// 광고 등록 확인
	@Transactional
	public boolean createConfirm(AdvertisementDto advertisementDto, String ad_dir_name, String savedFileName) {
		log.info("createConfirm()");
		
		// 광고 디렉토리명과 이미지 URL 세팅
		advertisementDto.setAd_dir_name(ad_dir_name);
		
		String newSrc = "http://"
				+ imgServerPath
				+"advertisement/"
				+ advertisementDto.getAd_category_no()
				+ "/"
				+ ad_dir_name
				+ "/"
				+ savedFileName;
		
		advertisementDto.setAd_img(newSrc);
		
		// 선택한 광고 위치의 maxIdx값 가져오기
		int AdvertisementMaxIdx = advertisementMapper.getAdvertisementIdxMaxNumByCategory(advertisementDto.getAd_category_no());
		
		try {
			
			// idx값을 중간값으로 입력 시 나머지 idx들 +1 처리 하기
			if (advertisementDto.getAd_idx() <= AdvertisementMaxIdx) {
				
				Map<String, Object> updateIdxParams = new HashMap<>();
				
				updateIdxParams.put("advertisementDto", advertisementDto);
				updateIdxParams.put("curIdx", null);
				
				int updateIdxResult = advertisementMapper.updateAdvertisementIdxSum(updateIdxParams);
				
				if (updateIdxResult <= 0) {
					throw new RuntimeException("idx 업데이트 실패!!");
					
				}
			}
			
			int createResult = advertisementMapper.insertNewAdvertisement(advertisementDto);
			
			// DB에 입력 실패
			if (createResult <= 0) {
				throw new RuntimeException("insertNewAdvertisement() error!!");
			
			// DB에 입력 성공
			} else {
				return ADVERTISEMENT_CREATE_SUCCESS;
						
			}
			
		} catch (Exception e) {
			log.info("createConfirm() Exception 발생!!");
			e.printStackTrace();
			
			return ADVERTISEMENT_CREATE_FAIL;
			
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
	
	// 광고 순서 변경(광고 위치 디테일뷰에서)
	@Transactional
	public boolean modifyAdvertisementIdx(int ac_no, int ad_no, int current_ad_idx, int ad_idx) {
		log.info("modifyAdvertisementIdx()");
		
		Map<String, Object> modifyIdxParams = new HashMap<>();
		
		modifyIdxParams.put("ac_no", ac_no);
		modifyIdxParams.put("ad_no", ad_no);
		modifyIdxParams.put("current_ad_idx", current_ad_idx);
		modifyIdxParams.put("ad_idx", ad_idx);
		
		int modifyIdxResult = 0;
		
		try {
			
			// 변경할 idx값에 있던 항목 current_ad_dix와 매치시키기
			modifyIdxResult = advertisementMapper.matchingModifyAdvertisementIdx(modifyIdxParams);
			
			if (modifyIdxResult > 0) {
				
				// idx 변경할 항목을 새로운 ad_idx로 할당하기
				modifyIdxResult = advertisementMapper.targetModifyAdvertisementIdx(modifyIdxParams);
				
				if (modifyIdxResult <= 0) {
					throw new RuntimeException("targetModifyAdvertisementIdx() error!");
					
				} else {
					
					return ADVERTISEMENT_IDX_MODIFY_SUCCESS;
					
				}
				
			} else {
				throw new RuntimeException("matchingModifyAdvertisementIdx() error!");
				
			}
			
			
		} catch (Exception e) {
			log.info("modifyAdvertisementIdx() Exception 발생!!");
			e.printStackTrace();
			
			return ADVERTISEMENT_IDX_MODIFY_FAIL;
			
		}
		
	}
	
	// 페이지에 따른 광고 가져오기(위치별 광고)
	public Map<String, Object> getAdvertisementListByCategoryWithPage(int page, String sortValue, String order, int ac_no) {
		log.info("getAdvertisementListByCategoryWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("ac_no", ac_no);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListByCategoryWithPage(pagingParams);
		pagingList.put("advertisementDtos", advertisementDtos);
		
		return pagingList;
		
	}
	
	// 광고의 총 페이지 개수 구하기 (위치별 광고)
	public Map<String, Object> getAdvertisementByCategoryPageNum(int page, int ac_no) {
		log.info("getAdvertisementByCategoryPageNum()");
		
		Map<String, Object> advertisementListByCategoryPageNum = new HashMap<>();
		
		// 전체 리스트 개수 주회
		int advertisementListByCategoryCnt = advertisementMapper.getAdvertisementByCategoryCnt(ac_no);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) advertisementListByCategoryCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		advertisementListByCategoryPageNum.put("advertisementListByCategoryCnt", advertisementListByCategoryCnt);
		advertisementListByCategoryPageNum.put("page", page);
		advertisementListByCategoryPageNum.put("maxPage", maxPage);
		advertisementListByCategoryPageNum.put("startPage", startPage);
		advertisementListByCategoryPageNum.put("endPage", endPage);
		advertisementListByCategoryPageNum.put("blockLimit", blockLimit);
		advertisementListByCategoryPageNum.put("pageLimit", pageLimit);
		
		return advertisementListByCategoryPageNum;
	}
	

	// 광고 한개 가져오기
	public AdvertisementDto getAdvertisement(int ad_no) {
		log.info("getAdvertisement()");
		
		AdvertisementDto advertisementDto = advertisementMapper.getAdvertisementByNo(ad_no);
		
		return advertisementDto;
		
	}

	// 광고 수정 확인
	@Transactional
	public boolean modifyConfirm(AdvertisementDto advertisementDto, String ad_dir_name, String savedFileName) {
		log.info("modifyConfirm()");
		
		// 사진 변경이 있을 시
		if (ad_dir_name != null && savedFileName != null) {
			
			// 광고 디렉토리명과 이미지 URL 세팅
			advertisementDto.setAd_dir_name(ad_dir_name);
			
			String newSrc = "http://"
					+ imgServerPath
					+"advertisement/"
					+ advertisementDto.getAd_category_no()
					+ "/"
					+ ad_dir_name
					+ "/"
					+ savedFileName;
			
			advertisementDto.setAd_img(newSrc);
			
		// 사진 변경이 없을 시
		} else {
			
			advertisementDto.setAd_dir_name(null);
			advertisementDto.setAd_img(null);
			
		}
		
		// 기존에 등록되어있던 ad_category_no와 ad_idx 가져오기
		AdvertisementDto curAdvertisementDto = advertisementMapper.getAdvertisementByNo(advertisementDto.getAd_no());
		int curCategoryNo = curAdvertisementDto.getAd_category_no();
		Integer curIdx = curAdvertisementDto.getAd_idx();
		
		
		try {
			
			// 광고 위치 변경이 없을 시
			if (curCategoryNo == advertisementDto.getAd_category_no()) {
				
				// 새로 입력한 IDX가 기존에 할당되어 있던 IDX 값 보다 작으면
				if (advertisementDto.getAd_idx() < curIdx) {
					
					Map<String, Object> updateIdxParams = new HashMap<>();
					
					updateIdxParams.put("advertisementDto", advertisementDto);
					updateIdxParams.put("curIdx", curIdx);
						
					int updateIdxResult = advertisementMapper.updateAdvertisementIdxSum(updateIdxParams);
					
					if (updateIdxResult <= 0) {
						throw new RuntimeException("idx 업데이트 실패!!");
						
					}
					
				// 새로 입력한 IDX가 기존에 할당되어 있던 IDX값 보다 크면
				} else if (advertisementDto.getAd_idx() > curIdx) {
					
					Map<String, Object> updateIdxParams = new HashMap<>();
					
					updateIdxParams.put("advertisementDto", advertisementDto);
					updateIdxParams.put("curIdx", curIdx);
					
					int updateIdxResult = advertisementMapper.updateAdvertisementIdxSub(updateIdxParams);
					
					if (updateIdxResult <= 0) {
						throw new RuntimeException("idx 업데이트 실패!!");
						
					}
					
				}
				
			// 광고 위치 변경 시
			} else {
				
				Map<String, Object> updateIdxParams = new HashMap<>();
				
				updateIdxParams.put("advertisementDto", advertisementDto);
				
//				int updateIdxResult = advertisementMapper.updateAdvertisementIdxOtherCategory(updateIdxParams);
//				
//				if (updateIdxResult <= 0) {
//					throw new RuntimeException("idx 업데이트 실패!!");
//					
//				}
				
			}
			
			
			int modifyResult = advertisementMapper.updateAdvertisement(advertisementDto);
			
			if (modifyResult <= 0) {
				throw new RuntimeException("updateAdvertisement() error!!");
				
			} else {
				
				return ADVERTISEMENT_MODIFY_SUCCESS;
			}
			
		} catch (Exception e) {
			log.info("modifyConfirm() Exception 발생!!");
			e.printStackTrace();
			
			return ADVERTISEMENT_MODIFY_FAIL;
			
		}
		
	}

	// 광고 삭제 확인
	public boolean deleteConfirm(int ad_no) {
		log.info("deleteConfirm()");
		
		int deleteResult = advertisementMapper.deleteAdvertisement(ad_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) {
			return ADVERTISEMENT_DELETE_FAIL;
		
		// DB에 입력 성공
		} else {
			return ADVERTISEMENT_DELETE_SUCCESS;
			
		}
		
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
		searchAdvertisementListPageNum.put("pageLimit", pageLimit);
		
		return searchAdvertisementListPageNum;
		
	}
	
}
