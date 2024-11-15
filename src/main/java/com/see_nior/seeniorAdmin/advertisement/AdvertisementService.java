package com.see_nior.seeniorAdmin.advertisement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.advertisement.mapper.AdvertisementMapper;
import com.see_nior.seeniorAdmin.dto.AdvertisementCategoryDto;
import com.see_nior.seeniorAdmin.dto.AdvertisementDto;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.util.ImageFileService;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {
	
	final private AdvertisementMapper advertisementMapper;
	final private ImageFileService imageFileService;
	
////////////////////////////////////////////////////////// 광고 위치
	
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
		if (createResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();		
		
	}
	
	// 모든 광고 위치 가져오기 (광고 리스트에서 <select>박스 => 비동기)
	public Map<String, Object> getCategoryList() {
		log.info("getCategoryList()");
		
		Map<String, Object> advertisementCategoryDtos = new HashMap<>();
		
		List<AdvertisementCategoryDto> advertisementCategoryDto = (List<AdvertisementCategoryDto>) advertisementMapper.getAdvertisementCategoryList();
		
		advertisementCategoryDtos.put("advertisementCategoryDtos", advertisementCategoryDto);	
				
		return advertisementCategoryDtos;
		
	}
	
	// 페이지에 따른 광고 위치 리스트 가져오기
	public Map<String, Object> getAdvertisementCategoryListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getAdvertisementCategoryListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdvertisementCategoryDto> advertisementCategoryDtos = advertisementMapper.getAdvertisementCategoryListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		
		pagingList.put("advertisementCategoryDtos", advertisementCategoryDtos);
		
		return pagingList;
		
	}
	
	// 광고 위치의 총 페이지 개수 구하기
	public Map<String, Object> getAdvertisementCategoryListPageNum(int page_limit, int block_limit, int page) {
		log.info("getAdvertisementCategoryListPageNum()");
		
		// 전체 리스트 개수 조회
		int advertisementCategoryListCnt = advertisementMapper.getAllAdvertisementCategoryCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "advertisementCategoryListCnt", advertisementCategoryListCnt, page);
		
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
		if (modifyResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
	
	}
	
	// 광고 위치 삭제 확인
	public boolean deleteCategoryConfirm(int ac_no) {
		log.info("deleteCategoryConfirm()");
		
		int deleteResult = advertisementMapper.deleteAdvertisementCategory(ac_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}
	
	// 페이지에 따른 광고 위치 가져오기(검색한 광고 위치)
	public Map<String, Object> getSearchAdvertisementCategoryListWithPage(int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("getSearchAdvertisementCategoryListWithPage()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<AdvertisementCategoryDto> searchAdvertisementCategoryDtos = advertisementMapper.getSearchAdvertisementCategory(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
				
		pagingCategoryList.put("advertisementCategoryDtos", searchAdvertisementCategoryDtos);
		
		return pagingCategoryList;
		
	}
	
	// 광고 카테고리의 총 페이지 개수 구하기(검색한 광고 카테고리)
	public Map<String, Object> getSearchAdvertisementCategoryListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementCategoryListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchAdvertisementCategoryListCnt = advertisementMapper.getSearchAdvertisementCategoryListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchAdvertisementCategoryListCnt", searchAdvertisementCategoryListCnt, page);
		
	}
	
////////////////////////////////////////////////////////// 광고
	
	// 광고 등록 양식에서 광고를 등록할 위치를 선택 했을 시 해당 위치의 maxIdx 가져오기
	public int getAdvertisementIdxMaxNum(int ad_category_no) {
		log.info("getAdvertisementIdxMaxNum()");
		
		Integer advertisementIdxMaxNum = advertisementMapper.getAdvertisementIdxMaxNumByCategory(ad_category_no);
		
		if (advertisementIdxMaxNum == null) advertisementIdxMaxNum = 0;
		
		return advertisementIdxMaxNum;
	}
	
	// 광고 등록 확인
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean createConfirm(AdvertisementDto advertisementDto, List<MultipartFile> files) {
		log.info("createConfirm()");
		
		// 이미지 서버에 요청할 파일 저장 경로 생성
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateFormat.format(now);
		
		// advertisement 테이블에서 maxNo값 가져오기
		int maxNo = advertisementMapper.getAdvertisementMaxNo();
		
		String filePath = "\\advertisement\\" + (maxNo + 1) + "\\" + date;
		
		// 이미지 저장 요청
		ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
		
		if (savedFile != null) {
			log.info("uploadFile SUCCESS!!");
			
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody(), new TypeReference<Map<String, Object>>() {});

				String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
				
				// 광고 디렉토리명과 이미지 URL 세팅
				advertisementDto.setAd_dir_name(date);
				advertisementDto.setAd_img(savedFileName);
				
				// 선택한 광고 위치의 maxIdx값 가져오기
				Integer advertisementMaxIdx = advertisementMapper.getAdvertisementIdxMaxNumByCategory(advertisementDto.getAd_category_no());
				
				if(advertisementMaxIdx == null) advertisementMaxIdx = 0;
				
				try {
					
					// idx값을 중간값으로 입력 시 나머지 idx들 +1 처리 하기
					if (advertisementDto.getAd_idx() <= advertisementMaxIdx) {
						
						Map<String, Object> updateIdxSumParams = new HashMap<>();
						
						updateIdxSumParams.put("advertisementDto", advertisementDto);
						updateIdxSumParams.put("curIdx", null);
						
						int updateIdxResult = advertisementMapper.updateAdvertisementIdxSum(updateIdxSumParams);
						
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
						return SqlResult.SUCCESS.getValue();
								
					}
					
				} catch (Exception e) {
					log.info("createConfirm() Exception 발생!!");
					e.printStackTrace();
					
					return SqlResult.FAIL.getValue();
					
				}
				
			} catch (JsonMappingException e) {
				log.info("JsonMappingException!!");
				e.printStackTrace();
				
				return SqlResult.FAIL.getValue();
				
			} catch (JsonProcessingException e) {
				log.info("JsonProcessingException!!");
				e.printStackTrace();
				
				return SqlResult.FAIL.getValue();
				
			}
			
		} else {
			log.info("upload file fail!!");
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}
	
	// 홈 화면에서 보여질 광고 가져오기
	public Object getAdvertisementListForMain(int page_limit) {
		log.info("getAdvertisementListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListForMain(page_limit);
		responseMap.put("advertisementDtos", advertisementDtos);
		
		return responseMap;
		
	}
	
	// 페이지에 따른 광고 가져오기(모든 광고)
	public Map<String, Object> getAdvertisementListWithPage(int page_limit, String sortValue, String order, int page) {
		log.info("getAdvertisementListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListWithPage(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		pagingList.put("advertisementDtos", advertisementDtos);
		
		return pagingList;
		
	}

	// 광고의 총 페이지 개수 구하기(모든 광고)
	public Map<String, Object> getAdvertisementListPageNum(int page_limit, int block_limit, int page) {
		log.info("getAdvertisementListPageNum()");
		
		// 전체 리스트 개수 조회
		int advertisementListCnt = advertisementMapper.getAllAdvertisementCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "advertisementListCnt", advertisementListCnt, page);
		
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
					
					return SqlResult.SUCCESS.getValue();
					
				}
				
			} else {
				throw new RuntimeException("matchingModifyAdvertisementIdx() error!");
				
			}
			
			
		} catch (Exception e) {
			log.info("modifyAdvertisementIdx() Exception 발생!!");
			e.printStackTrace();
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}
	
	// 페이지에 따른 광고 가져오기(위치별 광고)
	public Map<String, Object> getAdvertisementListByCategoryWithPage(int page_limit, int page, String sortValue, String order, int ac_no) {
		log.info("getAdvertisementListByCategoryWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdvertisementDto> advertisementDtos = advertisementMapper.getAdvertisementListByCategoryWithPage(PagingUtil.pagingParamsForSelectBox(page_limit, sortValue, order, page, ac_no));
		pagingList.put("advertisementDtos", advertisementDtos);
		
		return pagingList;
		
	}
	
	// 광고의 총 페이지 개수 구하기 (위치별 광고)
	public Map<String, Object> getAdvertisementByCategoryPageNum(int page_limit, int block_limit, int page, int ac_no) {
		log.info("getAdvertisementByCategoryPageNum()");
		
		// 전체 리스트 개수 주회
		int advertisementListByCategoryCnt = advertisementMapper.getAdvertisementByCategoryCnt(ac_no);
		
		return PagingUtil.pageNum(page_limit, block_limit, "advertisementListByCategoryCnt", advertisementListByCategoryCnt, page);
	
	}
	

	// 광고 한개 가져오기
	public AdvertisementDto getAdvertisement(int ad_no) {
		log.info("getAdvertisement()");
		
		AdvertisementDto advertisementDto = advertisementMapper.getAdvertisementByNo(ad_no);
		
		return advertisementDto;
		
	}

	// 광고 수정 확인
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean modifyConfirm(AdvertisementDto advertisementDto, String current_ad_img, List<MultipartFile> files) {
		log.info("modifyConfirm()");
		
		// 사진 변경이 있을 시
		if (files != null && files.size() != 0 && files.get(0).getSize() != 0) {
			
			String filePath = "\\advertisement\\" + advertisementDto.getAd_no() + "\\" + advertisementDto.getAd_dir_name();
				
			// 이미지 서버에 저장된 이미지 파일 이름 가져오기
			ResponseEntity<String> savedFile = imageFileService.uploadFiles(files, filePath);
			
			if (savedFile != null) {
				log.info("uploadFile SUCCESS!!");
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					Map<String, Object> savedFileObj = objectMapper.readValue(savedFile.getBody(), new TypeReference<Map<String, Object>>() {});
					
					String savedFileName = ((List<String>) savedFileObj.get("savedFileNames")).get(0);
					
					// 광고 이미지 URL 세팅
					advertisementDto.setAd_img(savedFileName);
					
					if (current_ad_img != null) {
						
						List<String> deleteFiles = new ArrayList<>();
						
						deleteFiles.add(current_ad_img);
						
						ResponseEntity<String> deleteFile = imageFileService.deleteFiles(deleteFiles, filePath);
						
						if (deleteFile != null) {
							log.info("deleteFile SUCCESS!!");
							
						} else {
							log.info("deleteFile FAIL!!");
							
						}
						
					}
				
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
			
			advertisementDto.setAd_dir_name(null);
			advertisementDto.setAd_img(null);
			
		}
		
		// 기존에 등록되어있던 ad_category_no와 ad_idx 가져오기
		AdvertisementDto curAdvertisementDto = advertisementMapper.getAdvertisementByNo(advertisementDto.getAd_no());
		Integer curCategoryNo = curAdvertisementDto.getAd_category_no();
		Integer curIdx = curAdvertisementDto.getAd_idx();
		
		try {
			
			// 광고 위치 변경이 없을 시
			if (curCategoryNo == advertisementDto.getAd_category_no()) {
				
				// 새로 입력한 IDX가 기존에 할당되어 있던 IDX 값 보다 작으면
				if (advertisementDto.getAd_idx() < curIdx) {
					
					Map<String, Object> updateIdxSumParams = new HashMap<>();
					
					updateIdxSumParams.put("advertisementDto", advertisementDto);
					updateIdxSumParams.put("curIdx", curIdx);
						
					int updateIdxResult = advertisementMapper.updateAdvertisementIdxSum(updateIdxSumParams);
					
					if (updateIdxResult <= 0) {
						throw new RuntimeException("idx 업데이트 실패!!");
						
					}
					
				// 새로 입력한 IDX가 기존에 할당되어 있던 IDX값 보다 크면
				} else if (advertisementDto.getAd_idx() > curIdx) {
					
					Map<String, Object> updateIdxSubParams = new HashMap<>();
					
					updateIdxSubParams.put("advertisementDto", advertisementDto);
					updateIdxSubParams.put("curIdx", curIdx);
					updateIdxSubParams.put("curCategoryNo", null);
					
					int updateIdxResult = advertisementMapper.updateAdvertisementIdxSub(updateIdxSubParams);
					
					if (updateIdxResult <= 0) {
						throw new RuntimeException("idx 업데이트 실패!!");
						
					}
					
				}
				
			// 광고 위치 변경 시
			} else {
				
				Map<String, Object> updateIdxSumParams = new HashMap<>();
				
				updateIdxSumParams.put("advertisementDto", advertisementDto);
				updateIdxSumParams.put("curIdx", null);
				
				int updateIdxResult = 1;
				Integer advertisementIdxMaxNum = 0;
				
				// 변경될 위치에 있는 광고의 MaxIdx 가져오기
				advertisementIdxMaxNum = advertisementMapper.getAdvertisementIdxMaxNumByCategory(advertisementDto.getAd_category_no());
				
				if (advertisementIdxMaxNum == null) advertisementIdxMaxNum = 0;
				
				// 변경될 위치에 있는 광고의 MaxIdx 값이 0보다 크면
				if (advertisementIdxMaxNum > 0) {
					
					// 변경될 위치에 있는 광고 중 입력한 idx값과 같거나 큰것들 +1
					updateIdxResult = advertisementMapper.updateAdvertisementIdxSum(updateIdxSumParams);
					
				}
				
				if (updateIdxResult <= 0) {
					throw new RuntimeException("idx 업데이트 실패!!");
					
				} else {
					
					Map<String, Object> updateIdxSubParams = new HashMap<>();
					
					updateIdxSubParams.put("advertisementDto", advertisementDto);
					updateIdxSubParams.put("curIdx", curIdx);
					updateIdxSubParams.put("curCategoryNo", curCategoryNo);
					
					
					// 광고의 기존 위치에서 MaxIdx 가져오기
					advertisementIdxMaxNum = advertisementMapper.getAdvertisementIdxMaxNumByCategory(curCategoryNo);
					
					// 기존 위치의 광고의 MaxIdx 가 기존 Idx 값 보다 크면
					if (advertisementIdxMaxNum > curIdx) {
						
						// 기존 위치에 있는 광고들 중 기존의 Idx 값 보다 큰 것들 -1
						updateIdxResult = advertisementMapper.updateAdvertisementIdxSub(updateIdxSubParams);
						
						if (updateIdxResult <= 0) {
							throw new RuntimeException("idx 업데이트 실패!!");
							
						}
						
					}
					
				}
				
			}
			
			int modifyResult = advertisementMapper.updateAdvertisement(advertisementDto);
			
			if (modifyResult <= 0) {
				throw new RuntimeException("updateAdvertisement() error!!");
				
			} else {
				
				return SqlResult.SUCCESS.getValue();
			}
			
		} catch (Exception e) {
			log.info("modifyConfirm() Exception 발생!!");
			e.printStackTrace();
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}

	// 광고 삭제 확인
	public boolean deleteConfirm(int ad_no) {
		log.info("deleteConfirm()");
		
		int deleteResult = advertisementMapper.deleteAdvertisement(ad_no);
		
		// DB에 입력 실패
		if (deleteResult <= 0) return SqlResult.FAIL.getValue();
		// DB에 입력 성공
		else return SqlResult.SUCCESS.getValue();	
		
	}

	// 페이지에 따른 광고 가져오기(검색한 광고)
	public Map<String, Object> getSearchAdvertisementListWithPage(int page_limit, String searchPart, String searchString, String sortValue, String order, int page) {
		log.info("getSearchAdvertisementListWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdvertisementDto> searchAdvertisementDtos = advertisementMapper.getSearchAdvertisement(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		pagingList.put("advertisementDtos", searchAdvertisementDtos);
		
		return pagingList;
		
	}

	// 광고의 총 페이지 개수 구하기(검색한 광고)
	public Map<String, Object> getSearchAdvertisementListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("getSearchAdvertisementListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchAdvertisementListCnt = advertisementMapper.getSearchAdvertisementListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchAdvertisementListCnt", searchAdvertisementListCnt, page);
		
	}
	
}
