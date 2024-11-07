package com.see_nior.seeniorAdmin.qna;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.QnaCategoryDto;
import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.dto.QnaNoticeDto;
import com.see_nior.seeniorAdmin.enums.ImgUrlPath;
import com.see_nior.seeniorAdmin.enums.SqlResult;
import com.see_nior.seeniorAdmin.qna.mapper.QnaMapper;
import com.see_nior.seeniorAdmin.util.ImageFileService;
import com.see_nior.seeniorAdmin.util.PagingUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class QnaService {

	final private QnaMapper qnaMapper;
	final private AccountMapper accountMapper;
	final private RestTemplate restTemplate;
	final private ImageFileService imageFileService;
	
	// qna 리스트 가져오기
	public Map<String, Object> getQnaPagingList(String sortValue, String order, int page) {
		log.info("getQnaPagingList()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> qnaDtos = 
				qnaMapper.selectQnaList(PagingUtil.pagingParams(sortValue, order, page));
		pagingList.put("qnaDtos", qnaDtos);
		
		return pagingList;
		
	}

	// qna 리스트 총 개수
	public Map<String, Object> getQnaListPageNum(int page) {
		log.info("getQnaListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaListCnt = qnaMapper.selectAllQnaListCnt();
		
		return PagingUtil.pageNum("qnaListCnt", qnaListCnt, page);
		
	}
	
	// qna 리스트 중 답변 안 한 개수
	public int getUnansweredQnaCnt() {
		log.info("getUnansweredQnaCnt()");
		
		return qnaMapper.selectUnansweredQnaCnt();
		
	}
	
	// 질문 등록 여부 확인하기
	public List<QnaDto> getUnansweredQuestions() {
		log.info("getUnansweredQuestions()");
		
		List<QnaDto> unansweredQnaDtos = qnaMapper.selectUnansweredQuestions();
		
		return unansweredQnaDtos;
	}

	// qna 검색 리스트 가져오기
	public Map<String, Object> searchQnaPagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaPagingList()");
		
		Map<String, Object> pagingSearchList = new HashMap<>();

		List<AdminAccountDto> qnaDtos = 
				qnaMapper.selectSearchQnaList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchList.put("qnaDtos", qnaDtos);
		
		return pagingSearchList;
		
	}

	// 검색 qna 리스트 개수 
	public Map<String, Object> searchQnaListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaListCnt = qnaMapper.selectSearchQnaListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaListCnt", searchQnaListCnt, page);
		
	}
	
	// qna 검색 리스트 중 답변 안 한 개수
	public int getUnansweredSearchQnaCnt(String searchPart, String searchString) {
		log.info("getUnansweredSearchQnaCnt()");
		
		Map<String, Object> params = new HashMap<>();
		params.put("searchPart", searchPart);
		params.put("searchString", searchString);
		
		return qnaMapper.selectUnansweredSearchQnaCnt(params);
		
	}

	
	// qna 카테고리에 따른 리스트 가져오기
	public Map<String, Object> getQnaListByCategoryWithPage(int page, String sortValue, String order, int bqc_no) {
		log.info("getQnaListByCategoryWithPage()");
		
		Map<String, Object> pagingList = new HashMap<>();
		
		List<AdminAccountDto> qnaDtos = 
				qnaMapper.selectQnaListForSelectBox(PagingUtil.pagingParamsForSelectBox(sortValue, order, page, bqc_no));
		pagingList.put("qnaDtos", qnaDtos);
		
		return pagingList;
		
	}

	// qna 카테고리에 따른 리스트 총 개수
	public Map<String, Object> getQnaByCategoryPageNum(int page, int bqc_no) {
		log.info("getQnaByCategoryPageNum()");

		// 전체 리스트 개수 조회 
		int qnaListCnt = qnaMapper.selectAllQnaListCntForSelectBox(bqc_no);
		
		return PagingUtil.pageNum("qnaListCnt", qnaListCnt, page);
		
	}
 
	// qna 카테고리 리스트 중 답변 안 한 개수
	public int getUnansweredCategoryQnaCnt(int bqc_no) {
		log.info("getUnansweredCategoryQnaCnt()");
		
		return qnaMapper.selectUnansweredCategoryQnaCnt(bqc_no);
		
	}
	
	// qna 정보 가져오기 by no
	public QnaDto getQnaInfoByNo(int bq_no) {
		log.info("getQnaInfoByNo()");
		
		return qnaMapper.selectQnaInfoByNo(bq_no);
		
	}
	
	// qna 답변 확인
	@Transactional
	public boolean qnaAnswerConfirm(int bq_no, String bqa_answer, String a_id) {
		log.info("qnaAnswerConfirm()");
		
		AdminAccountDto adminAccountDto = 
				accountMapper.selectAdminAccountById(a_id);
		
		Map<String, Object> insertParams = new HashMap<>();
		insertParams.put("bqa_answer", bqa_answer);
		insertParams.put("a_id", a_id);
		insertParams.put("a_no", adminAccountDto.getA_no());

		try {
			
			int insertResult = qnaMapper.insertNewAnswer(insertParams);
			
			if (insertResult >= 0) {
				
				int bqa_no = qnaMapper.selectQnaAnswerLastNo();
				
				Map<String, Object> updateParams = new HashMap<>();
				updateParams.put("bq_no", bq_no);
				updateParams.put("bqa_no", bqa_no);
				
				// 답변 완료 후 board_qna 테이블 bq_answer_no(답변 테이블 no) 값 입력
				int updateResult = 
						qnaMapper.updateQnaFromAnswerComplete(updateParams);
				
				if (updateResult >= 0) {
					
					return SqlResult.SUCCESS.getValue();
					
				} else {
					
					throw new RuntimeException("updateQnaStateByNo() error!!");
					
				}
				
			} else {
				
				throw new RuntimeException("updateQnaStateByNo() error!!");
				
			}
			
		} catch (Exception e) {
			log.info("qnaAnswerConfirm Exception ------ {}", e);
			
			return SqlResult.FAIL.getValue();
		
		}

	}

	// qna 답변 수정 확인
	public boolean answerModifyConfirm(String a_id, String loginedId, int bqa_no, String bqa_answer) {
		log.info("answerModifyConfirm()");
		
		AdminAccountDto adminAccountDto =
				accountMapper.selectAdminAccountById(loginedId);
		
		if ((adminAccountDto != null && adminAccountDto.getA_authority_role().equals("SUPER_ADMIN")) 
				|| a_id.equals(loginedId)) {
			
			Map<String, Object> params = new HashMap<>();
			params.put("bqa_no", bqa_no);
			params.put("bqa_answer", bqa_answer);
			
			int result = qnaMapper.updateQnaAnswer(params);
			
			if(result >= 0)
				return SqlResult.SUCCESS.getValue();
			else 
				return SqlResult.FAIL.getValue();
			
		} else {
			
			return SqlResult.FAIL.getValue();
			
		}

	}
	
	// qna 질문 공개/비공개 변경 확인
	public boolean modifyQnaState(QnaDto qnaDto) {
		log.info("modifyQnaState()");
		
		int updateResult = 
				qnaMapper.updateQnaStateByNo(qnaDto);
		
		if(updateResult >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();

	}
	
	// qna 질문 삭제 확인
	public boolean deleteConfirm(int bq_no) {
		log.info("deleteConfirm()");
		
		int updateResult = 
				qnaMapper.updateQnaIsDeletedByNo(bq_no);
		
		if(updateResult >= 0) 
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}

	// qna 답변 삭제 확인
	@Transactional
	public boolean answerDeleteConfirm(int bq_no, int bqa_no) {
		log.info("answerDeleteConfirm()");
		
		try {
			
			int answerUpdateResult = 
					qnaMapper.updateQnaAnswerIsDeletedByNo(bqa_no);
			
			if (answerUpdateResult >= 0) {
				
				int result = qnaMapper.updateQnaBqAnswerNoDelete(bq_no);
				
				if (result >= 0) 
					return SqlResult.SUCCESS.getValue();
				else 
					throw new RuntimeException("updateQnaBqAnswerNoDelete fail");
					
			} else {
				
				throw new RuntimeException("updateQnaAnswerIsDeletedByNo fail");
				
			}
			
		} catch (Exception e) {
			log.info("answerDeleteConfirm Exception ------- {}", e.getMessage());
			
			return SqlResult.FAIL.getValue();
		
		}
		
	}

	// qna main 화면 리스트 가져오기
	public Object getQnaListForMain(int page_limit) {
		log.info("getQnaListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<QnaDto> qnaDtosForMain = qnaMapper.selectQnaListForMain(page_limit);
		responseMap.put("qnaDtos", qnaDtosForMain);
		
		return responseMap;
		
	}

	
	
	
	/////////// 카테고리
	
	// qna 카테고리명 중복 확인
	public boolean isQnaCategory(String bqc_name) {
		log.info("isQnaCategory()");
		
		return qnaMapper.isQnaCategory(bqc_name);
		
	}

	// qna 카테고리 등록 확인
	public boolean createCategoryConfirm(String bqc_name) {
		log.info("createCategoryConfirm()");
		
		boolean isQna = qnaMapper.isQnaCategory(bqc_name);
		
		if (!isQna) {
			
			int result = qnaMapper.insertNewQnaCategory(bqc_name);
			
			if (result >= 0) 
				return SqlResult.SUCCESS.getValue();
			else 
				return SqlResult.FAIL.getValue();
			
			
		} else {
			
			return SqlResult.FAIL.getValue();
			
		}
		
	}
	
	// qna 카테고리 리스트 가져오기 (select box)
	public Object getCategoryListSelect() {
		log.info("getCategoryListSelect()");
		
		Map<String, Object> qnaCategoryMap = new HashMap<>();
		
		List<QnaCategoryDto> qnaCategoryDtos = qnaMapper.selectQnaCategoryListForSelectBox(); 
		
		qnaCategoryMap.put("qnaCategoryDtos", qnaCategoryDtos);
		
		return qnaCategoryMap;
		
	}

	// qna 카테고리 페이징 리스트 가져오기
	public Map<String, Object> getQnaCategoryPagingList(String sortValue, String order, int page) {
		log.info("getQnaCategoryPagingList()");
		
		Map<String, Object> pagingCategoryList = new HashMap<>();
		
		List<AdminAccountDto> qnaCategoryDtos = 
				qnaMapper.selectQnaCategoryList(PagingUtil.pagingParams(sortValue, order, page));
		pagingCategoryList.put("qnaCategoryDtos", qnaCategoryDtos);
		
		return pagingCategoryList;
		
	}

	// qna 카테고리 리스트 총 개수
	public Map<String, Object> getQnaCategoryListPageNum(int page) {
		log.info("getQnaCategoryListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaCategoryListCnt = qnaMapper.selectAllQnaCategoryListCnt();
		
		return PagingUtil.pageNum("qnaCategoryListCnt", qnaCategoryListCnt, page);

	}

	// qna 카테고리 검색 리스트 가져오기
	public Map<String, Object> searchQnaCategoryPagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaCategoryPagingList()");
		
		Map<String, Object> pagingSearchCategoryList = new HashMap<>();
		
		List<AdminAccountDto> qnaCategoryDtos = 
				qnaMapper.selectSearchQnaCategoryList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchCategoryList.put("qnaCategoryDtos", qnaCategoryDtos);
		
		return pagingSearchCategoryList;
		
	}

	// qna 카테고리 검색 리스트 총 개수
	public Map<String, Object> searchQnaCategoryListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaCategoryListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaCategoryListCnt = qnaMapper.selectSearchQnaCategoryListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaCategoryListCnt", searchQnaCategoryListCnt, page);
		
	}

	// qna 카테고리 정보 가져오기 by no
	public QnaCategoryDto getQnaCategoryDtoByNo(int bqc_no) {
		log.info("getQnaCategoryDtoByNo()");
		
		return qnaMapper.selectQnaCategoryDtoByNo(bqc_no);
		
	}

	// qna 카테고리 수정 확인
	public boolean modifyCategoryConfirm(QnaCategoryDto qnaCategoryDto) {
		log.info("modifyCategoryConfirm()");
		
		int result = qnaMapper.updateQnaCategoryInfo(qnaCategoryDto);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}

	// qna 카테고리 삭제 확인
	public boolean deleteCategoryConfirm(int bqc_no) {
		log.info("deleteCategoryConfirm()");
		
		int result = qnaMapper.updateQnaCategoryIsDeletedByNo(bqc_no);
		
		if (result >= 0)
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}
	
	
	////////////// 공지사항
	
	// qna 공지사항 가져오기
	public Map<String, Object> getQnaNoticePagingList(String sortValue, String order, int page) {
		log.info("getQnaNoticePagingList()");
		
		Map<String, Object> pagingNoticeList = new HashMap<>();
		
		List<AdminAccountDto> qnaNoticeDtos = 
				qnaMapper.selectQnaNoticeList(PagingUtil.pagingParams(sortValue, order, page));
		pagingNoticeList.put("qnaNoticeDtos", qnaNoticeDtos);
		
		return pagingNoticeList;
		
	}

	// qna 공지사항 총 개수
	public Map<String, Object> getQnaNoticeListPageNum(int page) {
		log.info("getQnaNoticeListPageNum()");
		
		// 전체 리스트 개수 조회 
		int qnaNoticeListCnt = qnaMapper.selectAllQnaNoticeListCnt();
		
		return PagingUtil.pageNum("qnaNoticeListCnt", qnaNoticeListCnt, page);
		
	}

	// qna 공지사항 검색 리스트 가져오기
	public Map<String, Object> searchQnaNoticePagingList(String searchPart, String searchString, String sortValue,
			String order, int page) {
		log.info("searchQnaNoticePagingList()");
		
		Map<String, Object> pagingSearchQnaNoticeList = new HashMap<>();
		
		List<AdminAccountDto> qnaNoticeDtos = 
				qnaMapper.selectSearchQnaNoticeList(PagingUtil.searchPagingParams(searchPart, searchString, sortValue, order, page));
		pagingSearchQnaNoticeList.put("qnaNoticeDtos", qnaNoticeDtos);
		
		return pagingSearchQnaNoticeList;
		
	}

	// qna 공지사항 검색 리스트 총 개수 
	public Map<String, Object> searchQnaNoticeListPageNum(String searchPart, String searchString, int page) {
		log.info("searchQnaNoticeListPageNum()");
		
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("searchPart", searchPart);
		searchParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회 
		int searchQnaNoticeListCnt = qnaMapper.selectSearchQnaNoticeListCnt(searchParams);
		
		return PagingUtil.pageNum("searchQnaNoticeListCnt", searchQnaNoticeListCnt, page);
		
	}

	// qna 공지사항 등록 확인
	public boolean createNoticeConfrim(List<MultipartFile> files, QnaNoticeDto qnaNoticeDto) {
		log.info("createNoticeConfrim()");
			
		Date now = new Date();	      
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String date = dateFormat.format(now);
	
		// 첨부된 파일이 있는 경우
		if (files != null && files.size() != 0 && files.get(0).getSize() != 0) {
			log.info("files is not empty.");
			
    		String filePath = ImgUrlPath.QNA_NOTICE_FILE_PATH.getValue() + date;
    		
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
					
					String bqn_body = qnaNoticeDto.getBqn_body();
					
					// bqn_body img src 경로 수정 (이미지 서버 파일 저장 경로)
					if (savedFileNames != null) {
						
						Pattern pattern = Pattern.compile("img src=\"[^\"]*\"");
						Matcher matcher = pattern.matcher(bqn_body);
						
						StringBuilder new_bqn_body = new StringBuilder();
						int index = 0;
						
						while (matcher.find()) {
							
							String newSrc = "img src=\"http://" 
									+ ImgUrlPath.QNA_NOTICE_PATH.getValue() 
									+"/"
									+ date
									+"/"
									+ savedFileNames.get(index++) + "\"";
							
							matcher.appendReplacement(new_bqn_body, newSrc);
							
						}
						
						matcher.appendTail(new_bqn_body);
						
						bqn_body = new_bqn_body.toString();
						
					// List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames") 변환 실패한 경우
					} else {
						
						throw new RuntimeException("List<String> savedFileNames 변환 실패");
						
					}
					
					qnaNoticeDto.setBqn_body(bqn_body);
					qnaNoticeDto.setBqn_dir_name(date);
					
					int insertResult = 
							qnaMapper.insertNewQnaNotice(qnaNoticeDto);
					
					if (insertResult >= 0) 
						return SqlResult.SUCCESS.getValue();
					else 
						throw new RuntimeException("insertNewQnaNotice() fail");
					
				} catch (Exception e) {
					log.info("createNoticeConfrim fail ----- {}", e.getMessage());

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
					qnaMapper.insertNewQnaNotice(qnaNoticeDto);
			
			if (insertResult >= 0) 
				return SqlResult.SUCCESS.getValue();
			else
				return SqlResult.FAIL.getValue();
			
		}
		
	}

	// qna 공지사항 정보 가져오기 by no
	public QnaNoticeDto getQnaNoticeInfoByNo(int bqn_no) {
		log.info("getQnaNoticeInfoByNo()");
		
		return qnaMapper.selectQnaNoticeInfoByNo(bqn_no);
		
	}

	// qna 공지사항 수정 확인
	public boolean modifyNoticeConfirm(
			List<MultipartFile> files, List<String> deleteFileNames, QnaNoticeDto qnaNoticeDto) {
		log.info("modifyNoticeConfirm()");

		String filePath = "";
		
		// 기존 저장된 img가 없는데 추가한 img가 있는 경우 dir_name 추가
		if (files != null && qnaNoticeDto.getBqn_dir_name() == null) {
			
			Date now = new Date();	      
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    		String date = dateFormat.format(now);
    		
    		qnaNoticeDto.setBqn_dir_name(date);
    		
		} 
		
		// 이미지 서버에 요청할 파일 저장 경로 생성
		filePath = ImgUrlPath.QNA_NOTICE_FILE_PATH.getValue() + qnaNoticeDto.getBqn_dir_name();
		
		// 추가한 이미지가 없는 경우
		if (files == null) {
			
			int updateResult = qnaMapper.updateQnaNotice(qnaNoticeDto);
			
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
					
					@SuppressWarnings("unchecked") //(List<String>) 강제 캐스팅 에러
					List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
					
					String bqn_body = qnaNoticeDto.getBqn_body();
						
					Pattern pattern = Pattern.compile("img src=\"blob:[^\"]*\"");
					Matcher matcher = pattern.matcher(bqn_body);
					
					StringBuilder new_bqn_body = new StringBuilder();
					int index = 0;
					
					while (matcher.find()) {
						
						String newSrc = "img src=\"http://" 
								+ ImgUrlPath.QNA_NOTICE_PATH.getValue() 
								+"/"
								+ qnaNoticeDto.getBqn_dir_name()
								+"/"
								+ savedFileNames.get(index++) + "\"";
						
						matcher.appendReplacement(new_bqn_body, newSrc);
						
					} 
					
					matcher.appendTail(new_bqn_body);
					bqn_body = new_bqn_body.toString();
					
					QnaNoticeDto newQnaNoticeDto = new QnaNoticeDto();
					newQnaNoticeDto.setBqn_no(qnaNoticeDto.getBqn_no());
					newQnaNoticeDto.setBqn_title(qnaNoticeDto.getBqn_title());
					newQnaNoticeDto.setBqn_dir_name(qnaNoticeDto.getBqn_dir_name());
					newQnaNoticeDto.setBqn_body(bqn_body);
					
					int updateResult = qnaMapper.updateQnaNotice(newQnaNoticeDto);
					
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
	
	// qna 공지사항 삭제 확인
	public boolean deleteNoticeConfrim(int bqn_no) {
		log.info("deleteNoticeConfrim()");
		
		int updateResult = 
				qnaMapper.updateQnaNoticeIsDeletedByNo(bqn_no);
		
		if (updateResult >= 0) 
			return SqlResult.SUCCESS.getValue();
		else 
			return SqlResult.FAIL.getValue();
		
	}

	// qna 공지사항 img 파일 imgageServer에 저장
	public ResponseEntity<String> uploadNoticeImg(List<MultipartFile> files) {
		log.info("uploadNoticeImg()");
		
		try {
			
			// Request Header 설정
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    		
    		// Request body 설정 (파일 배열을 보낼 때)
    		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
    		
    		for (MultipartFile file : files) {
    			
    			// 파일 이름 가져오기
    			String fileName = file.getOriginalFilename();
    			
    			// 파일을 ByteArrayResource로 변환
    			Resource fileResource = new ByteArrayResource(file.getBytes()) {
    				
    				@Override
    				public String getFilename() {
    					return fileName;
    				}
    				
    			};
    			
    			// 파일을 requestBody에 추가
    			requestBody.add("files", fileResource);
    			
    		}
    		
    		// 파일 저장 경로 생성 후 filePath를 키 값으로 requestBody에 추가 (맨 앞에 상위 폴더 경로 꼭! 추가)
    		String filePath = "\\qna\\notice\\";
    		requestBody.add("filePath", filePath);
    		
    		// Request Entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String serverURL = "http://localhost:8091/upload_file"; //local
            ResponseEntity<String> response = restTemplate.postForEntity(serverURL, requestEntity, String.class);

            return response;
			
		} catch (Exception e) {
			log.info("uploadNoticeImg error ----- {}", e.getMessage());
		
			return null;
			
		}
		
	}

	// qna 공지사항 main 화면 리스트 가져오기
	public Object getNoticeListForMain(int page_limit) {
		log.info("getNoticeListForMain()");
		
		Map<String, Object> responseMap = new HashMap<>();
		
		List<QnaNoticeDto> qnaNoticeDtos = qnaMapper.selectQnaNoticeListForMain(page_limit);
		responseMap.put("qnaNoticeDtos", qnaNoticeDtos);
		
		return responseMap;
		
	}











}
