package com.see_nior.seeniorAdmin.notice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.dto.QnaNoticeDto;
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
		
		return noticeMapper.selectNoticeInfoByNo(n_no);
		
	}

	// 전체 공지사항 페이징 리스트 가져오기 
	public Map<String, Object> getNoticePagingList(String sortValue, String order, int page) {
		log.info("getNoticePagingList()");
		
		Map<String, Object> pagingNoticeList = new HashMap<>();
		
		List<AdminAccountDto> noticeDtos = 
				noticeMapper.selectNoticeList(PagingUtil.pagingParams(sortValue, order, page));
		pagingNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingNoticeList;
		
	}

	// 전체 공지사항 리스트 총 개수 
	public Map<String, Object> getNoticeListPageNum(int page) {
		log.info("getNoticeListPageNum()");
		
		// 전체 리스트 개수 조회 
		int noticeListCnt = noticeMapper.selectAllNoticeListCnt();
		
		return PagingUtil.pageNum("noticeListCnt", noticeListCnt, page);
		
	}

	// 전체 공지사항 검색 페이징 리스트 가져오기
	public Map<String, Object> searchNoticePagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchNoticePagingList()");
		
		Map<String, Object> pagingSearchNoticeList = new HashMap<>();
		
		List<AdminAccountDto> noticeDtos = 
				noticeMapper.selectSearchNoticeList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchNoticeList.put("noticeDtos", noticeDtos);
		
		return pagingSearchNoticeList;
		
	}

	// 전체 공지사항 검색 리스트 총 개수
	public Map<String, Object> searchNoticeListPageNum(String searchPart, String searchString, int page) {
		log.info("searchNoticeListPageNum()");

		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchNoticeListCnt = noticeMapper.selectSearchNoticeListCnt(searchParams);
		
		return PagingUtil.pageNum("searchNoticeListCnt", searchNoticeListCnt, page);
		
	}

	// 전체 공지사항 main 화면 리스트 가져오기 
	public Object getNoticeListForMain(int page_limit) {
		log.info("getNoticeListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<QnaNoticeDto> noticeDtos = noticeMapper.selectNoticeListForMain(page_limit);
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
	
	
	
	
}
