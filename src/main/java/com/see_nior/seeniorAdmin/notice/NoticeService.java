package com.see_nior.seeniorAdmin.notice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.dto.DeleteNoticeDto;
import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.enums.ImgUrlPath;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.notice.mapper.NoticeMapper;
import com.see_nior.seeniorAdmin.util.ImageFileService;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeMapper noticeMapper;
	private final ImageFileService imageFileService;

	// 전체 공지사항 정보 가져오기 by no
	public NoticeDto getNoticeInfoByNo(int n_no) {
		log.info("getNoticeInfoByNo()");
		
		NoticeDto noticeDto = 
				noticeMapper.selectNoticeInfoByNo(n_no);
		
		if (noticeDto == null) {
			throw new RuntimeException("noticeDto is null");
		}
		
		return noticeDto;
		
	}

	// 전체 공지사항 페이징 리스트 가져오기 
	public Map<String, Object> getNoticePagingList(int page_limit, String sortValue, String order, int page) {
		log.info("getNoticePagingList()");
		
		Map<String, Object> pagingNoticeList = new HashMap<>();
		
		List<NoticeDto> noticeDtos = 
				noticeMapper.selectNoticeList(PagingUtil.pagingParams(page_limit, sortValue, order, page));
		pagingNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingNoticeList;
		
	}

	// 전체 공지사항 리스트 총 개수 
	public Map<String, Object> getNoticeListPageNum(int page_limit, int block_limit, int page) {
		log.info("getNoticeListPageNum()");
		
		// 전체 리스트 개수 조회 
		int noticeListCnt = noticeMapper.selectAllNoticeListCnt();
		
		return PagingUtil.pageNum(page_limit, block_limit, "noticeListCnt", noticeListCnt, page);
		
	}

	// 전체 공지사항 검색 페이징 리스트 가져오기
	public Map<String, Object> searchNoticePagingList(int page_limit, String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchNoticePagingList()");
		
		Map<String, Object> pagingSearchNoticeList = new HashMap<>();
		
		List<NoticeDto> noticeDtos = 
				noticeMapper.selectSearchNoticeList(PagingUtil.searchPagingParams(page_limit, searchPart, searchString, sortValue, order, page));
		pagingSearchNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingSearchNoticeList;
		
	}

	// 전체 공지사항 검색 리스트 총 개수
	public Map<String, Object> searchNoticeListPageNum(int page_limit, int block_limit, String searchPart, String searchString, int page) {
		log.info("searchNoticeListPageNum()");

		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchNoticeListCnt = noticeMapper.selectSearchNoticeListCnt(searchParams);
		
		return PagingUtil.pageNum(page_limit, block_limit, "searchNoticeListCnt", searchNoticeListCnt, page);
		
	}

	// 전체 공지사항 main 화면 리스트 가져오기 
	public Object getNoticeListForMain(int page_limit) {
		log.info("getNoticeListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<NoticeDto> noticeDtos = noticeMapper.selectNoticeListForMain(page_limit);
		
		responseMap.put("noticeDtos", noticeDtos);
		
		return responseMap;
		
	}

	// 전체 공지사항 등록 확인
	public boolean createConfirm(List<MultipartFile> files, NoticeDto noticeDto) {
		log.info("createConfirm()");
		
		Date now = new Date();	      
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateFormat.format(now);
	
		// 첨부된 파일이 있는 경우
		if (files != null && files.size() != 0 && files.get(0).getSize() != 0) {
			log.info("files is not empty.");
			
    		String filePath = ImgUrlPath.NOTICE_FILE_PATH.getValue() + date;
    		
    		// 이미지 저장 요청
    		ResponseEntity<String> savedFiles = 
    				imageFileService.uploadFiles(files, filePath);
    		
			// 이미지 서버 파일 저장 완료
			if (savedFiles != null) {
				log.info("uploadNoticeImgFile success");
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					
					Map<String, Object> savedFileObj = 
							objectMapper.readValue(savedFiles.getBody(), new TypeReference<Map<String, Object>>() {});
					
					@SuppressWarnings("unchecked")
					List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
					
					String n_body = noticeDto.getN_body();
					
					// bqn_body img src 경로 수정 (이미지 서버 파일 저장 경로)
					if (savedFileNames != null) {
						
						Pattern pattern = Pattern.compile("img src=\"[^\"]*\"");
						Matcher matcher = pattern.matcher(n_body);
						
						StringBuilder new_n_body = new StringBuilder();
						int index = 0;
						
						while (matcher.find()) {
							
							String newSrc = "img src=\"http://" 
									+ ImgUrlPath.NOTICE_PATH.getValue() 
									+"/"
									+ date
									+"/"
									+ savedFileNames.get(index++) + "\"";
							
							matcher.appendReplacement(new_n_body, newSrc);
							
						}
						
						matcher.appendTail(new_n_body);
						
						n_body = new_n_body.toString();
						
					// List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames") 변환 실패한 경우
					} else {
						
						throw new RuntimeException("List<String> savedFileNames 변환 실패");
						
					}
					
					noticeDto.setN_body(n_body);
					noticeDto.setN_dir_name(date);
					
					int insertResult = 
							noticeMapper.insertNewNotice(noticeDto);
					
					if (insertResult >= 0) 
						return SqlResult.SUCCESS.getValue();
					else 
						throw new RuntimeException("insertNewNotice() fail");
					
				} catch (Exception e) {
					log.info("createConfirm fail ----- {}", e.getMessage());

					return SqlResult.FAIL.getValue();
					
				}
			
			// 이미지 서버 저장 실패
			} else {
				log.info("uploadNoticeImg fail");
				
				return SqlResult.FAIL.getValue();
				
			}
		
		// 첨부된 파일이 없는 경우
		} else {
			
			int insertResult = 
					noticeMapper.insertNewNotice(noticeDto);
			
			if (insertResult >= 0) 
				return SqlResult.SUCCESS.getValue();
			else
				return SqlResult.FAIL.getValue();
			
		}
		
	}

	// 전체 공지사항 수정 확인
	public boolean modifyConfirm(
			List<MultipartFile> files, List<String> deleteFileNames, NoticeDto noticeDto) {
		log.info("modifyConfirm()");
		
		String filePath = "";
		
		// 기존 저장된 img가 없는데 추가한 img가 있는 경우 dir_name 추가
		if (files != null && noticeDto.getN_dir_name() == null) {
			
			Date now = new Date();	      
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    		String date = dateFormat.format(now);
    		
    		noticeDto.setN_dir_name(date);
    		
		} 
		
		// 이미지 서버에 요청할 파일 저장 경로 생성
		filePath = ImgUrlPath.NOTICE_FILE_PATH.getValue() + noticeDto.getN_dir_name();
		
		// 추가한 이미지가 없는 경우
		if (files == null) {
			
			int updateResult = noticeMapper.updateNotice(noticeDto);
			
			if (updateResult >= 0) {
				log.info("updateQnaNotice success");
				
				// 삭제할 이미지가 있는 경우
				if (deleteFileNames.size() != 0) {
					
					ResponseEntity<String> deletedFiles = 
							imageFileService.deleteFiles(deleteFileNames, filePath);
					
					if (deletedFiles != null) {
						log.info("imageFileService.deleteFiles() success");
						return SqlResult.SUCCESS.getValue();
					} else {
						log.info("imageFileService.deleteFiles() fail");
						return SqlResult.FAIL.getValue();
					}  
					
				} 
				
			} else { 
				log.info("updateQnaNotice fail");
				return SqlResult.FAIL.getValue();
			}
			
		} else {
			
			// 이미지 서버에 새로운 이미지 저장 저장 요청
			ResponseEntity<String> savedFiles = imageFileService.uploadFiles(files, filePath);
			
			// 이미지 서버 파일 저장 완료 
			if (savedFiles != null) {
				log.info("imageFileService.uploadFiles() success");
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					
					Map<String,Object> savedFileObj = 
							objectMapper.readValue(savedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
					
					@SuppressWarnings("unchecked")
					List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
					
					String n_body = noticeDto.getN_body();
						
					Pattern pattern = Pattern.compile("img src=\"blob:[^\"]*\"");
					Matcher matcher = pattern.matcher(n_body);
					
					StringBuilder new_n_body = new StringBuilder();
					int index = 0;
					
					while (matcher.find()) {
						
						String newSrc = "img src=\"http://" 
								+ ImgUrlPath.NOTICE_PATH.getValue() 
								+"/"
								+ noticeDto.getN_dir_name()
								+"/"
								+ savedFileNames.get(index++) + "\"";
						
						matcher.appendReplacement(new_n_body, newSrc);
						
					} 
					
					matcher.appendTail(new_n_body);
					n_body = new_n_body.toString();
					
					NoticeDto newNoticeDto = new NoticeDto();
					newNoticeDto.setN_no(noticeDto.getN_no());
					newNoticeDto.setN_title(noticeDto.getN_title());
					newNoticeDto.setN_dir_name(noticeDto.getN_dir_name());
					newNoticeDto.setN_state(noticeDto.isN_state());
					newNoticeDto.setN_body(n_body);
					
					int updateResult = noticeMapper.updateNotice(newNoticeDto);
					
					if (updateResult >= 0) {
						log.info("qnaMapper.updateQnaNotice() success");
						
						// 삭제할 img가 있을 경우
						if (deleteFileNames.size() != 0) {
							
							ResponseEntity<String> deletedFiles = 
									imageFileService.deleteFiles(deleteFileNames, filePath);
							
							if (deletedFiles == null) {
								log.info("imageFileService.deleteFiles() fail");
								return SqlResult.FAIL.getValue();
							}
							
						}
						
						
					} else {
						log.info("qnaMapper.updateQnaNotice() fail");
						
						return SqlResult.FAIL.getValue();
						
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			} else {
				
				return SqlResult.FAIL.getValue();
				
			}
			
		}
		
		return SqlResult.SUCCESS.getValue();
		
	}

	// 전체 공지사항 삭제 확인
	public boolean deleteConfirm(int n_no) {
		log.info("deleteConfirm()");
		
		int updateResult = 
				noticeMapper.updateIsDeletedByNo(n_no);
		
		if (updateResult >= 0) 
			return SqlResult.SUCCESS.getValue();	
		else 
			return SqlResult.FAIL.getValue();
		
	}
	
	// 전체 공지사항 삭제(is_deleted 값 update) 30일 경과 후 img 저장 폴더 삭제 스케쥴러
	@Scheduled(cron = "0 1 0 * * ?")	//초 분 시 일 월 요일 년 (각 자리에 *는 모든 값을 의미)
	public void deleteImgFolder() {
		log.info("deleteImgFolder()");
		
		List<DeleteNoticeDto> deleteNoticeDots = 
				noticeMapper.selectDeleteNoticeInfo();

		// 공지사항 삭제 후 30일 경과된 컬럼이 없는 경우 리턴.  
		if (deleteNoticeDots.size() == 0) {
			log.info("deleteNoticeDots.size() == 0");
			return;
		}
		
		List<String> deleteFolderPaths = new ArrayList<String>();
		
		// 30일이 경과된 컬럼이 있는 경우 삭제 할 dir 경로 추출.
		for (int i = 0; i < deleteNoticeDots.size(); i++) {
			if (deleteNoticeDots.get(i).getDn_dir_name() != null) {
				String folderPath = ImgUrlPath.NOTICE_FILE_PATH.getValue() 
						+ deleteNoticeDots.get(i).getDn_dir_name();
				deleteFolderPaths.add(folderPath);
			}
		}
		
		// 30일이 경과된 컬럼이 있지만 삭제 할 이미지는 없는 경우.
		if (deleteFolderPaths.size() == 0) {
			log.info("deleteFolderPaths.size() == 0");
			
			int updateResult = 0;
			List<Integer> deleteFailedList = new ArrayList<>();
			
			for (int i = 0; i < deleteNoticeDots.size(); i++) {
				
				updateResult =
						noticeMapper.updateDeleteNoticeIsDeleted(deleteNoticeDots.get(i).getDn_no());
				
				if (updateResult <= 0) {
					deleteFailedList.add(deleteNoticeDots.get(i).getDn_no());
				}
				
			}
			
			log.info("updateDeleteNoticeIsDeleted failed list ----- {}", deleteFailedList);
			
			return;
		}
		
		// 30일이 경과된 컬럼 이미지 서버 삭제 요청 
		ResponseEntity<String> deletedFolders =
				imageFileService.deleteFolders(deleteFolderPaths);
		
		// 이미지 서버 삭제 성공
		if (deletedFolders.getBody().equals("1")) {
			log.info("DELETEDFOLDERS SUCCESS!");
			
			int updateResult = 0;
			List<Integer> deleteFailedList = new ArrayList<>();
			
			for (int i = 0; i < deleteNoticeDots.size(); i++) {
				
				updateResult =
						noticeMapper.updateDeleteNoticeIsDeleted(deleteNoticeDots.get(i).getDn_no());
				
				if (updateResult <= 0) {
					deleteFailedList.add(deleteNoticeDots.get(i).getDn_no());
				}
				
			}
			
			log.info("updateDeleteNoticeIsDeleted failed list ----- {}", deleteFailedList);
			
		// 이미지 서버 삭제 실패
		} else if (deletedFolders.getBody().equals("0")) {
			log.info("FOLDER NAME OR PATH NOT FOUND!!");
			log.info("response value: {}", deletedFolders.getBody());
		} else {
			log.info("FOLDER DELETE FAIL!!");
			log.info("response value: {}",deletedFolders.getBody());
		}
		
	}
	
}
