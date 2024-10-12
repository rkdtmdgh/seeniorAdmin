package com.see_nior.seeniorAdmin.board;

import java.io.IOException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.see_nior.seeniorAdmin.board.mapper.BoardMapper;
import com.see_nior.seeniorAdmin.board.util.BoardItemCntUpdater;
import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;
import com.see_nior.seeniorAdmin.dto.BoardPostsDto;
import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	//필드 선언
	final private BoardMapper boardMapper;
	final private RestTemplate restTemplate;
	final private BoardItemCntUpdater boardItemCntUpdater;
	
//	public BoardService( BoardMapper boardMapper, 
//						BoardItemCntUpdater boardItemCntUpdater, 
//						RestTemplate restTemplate ) {
//		this.boardMapper = boardMapper;
//		this.boardItemCntUpdater = boardItemCntUpdater;
//		this.restTemplate = restTemplate;
//	}
	
	//모든 게시판 항목 가져오기
	public Object getList() {
		log.info("getList()");
				
		Map<String, Object> cateDtos = new HashMap<>();
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getList();
		
		cateDtos.put("boardCategoryDtos", boardCategoryDtos);
						
		return cateDtos;
	}
	
	//페이지에 따른 모든 게시판 항목 가져오기
	public Map<String, Object> getBoardCategoryListWithPage(int page, String sortValue, String order) {
		log.info("getBoardCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryListWithPage(pagingParams);
		
		pagingList.put("boardCategoryDtos", boardCategoryDtos);
		
		return pagingList;
	}
	
	// 게시판 카테고리의 총 페이지 개수 구하기
	public Map<String, Object> getBoardCategoryListPageNum(int page) {
		log.info("getBoardCategoryListPageNum()");
		
		Map<String, Object> boardCategoryListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int boardCategoryListCnt = boardMapper.getAllBoardCategoryCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) boardCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산 
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		boardCategoryListPageNum.put("boardCategoryListCnt", boardCategoryListCnt);
		boardCategoryListPageNum.put("page", page);
		boardCategoryListPageNum.put("maxPage", maxPage);
		boardCategoryListPageNum.put("startPage", startPage);
		boardCategoryListPageNum.put("endPage", endPage);
		boardCategoryListPageNum.put("blockLimit", blockLimit);
		boardCategoryListPageNum.put("pageLimit", pageLimit);
		
		return boardCategoryListPageNum;
	}
	
	//게시판명 중복 확인
	public boolean isBoardCategory(BoardCategoryDto boardCategoryDto) {
		log.info("isBoardCategory()");
		
		String bc_name = boardCategoryDto.getBc_name();
		
		boolean result = boardMapper.isBoardCategory(bc_name);
		
		return result;
	}
	
	//일반 게시판 마지막 순서(idx) 번호 가져오기
	public int getBoardCategoryIdxMaxNum() {
		log.info("getBoardCategoryIdxMaxNum()");
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryIdxMaxNum();
		
//		int IdxMaxNum = boardCategoryDtos.getFirst().getBc_idx();
		int IdxMaxNum = boardCategoryDtos.get(0).getBc_idx();
		
		return IdxMaxNum;
	}
	
	//게시판 생성 요청 처리
	public boolean createCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("createCategoryConfirm()");
					
		int bc_idx = boardCategoryDto.getBc_idx();
					
		log.info("bc_idx: ",bc_idx);
		
		//DB에서 board category idx 업데이트 처리
		int result = boardMapper.updateBoardCategoryIdx(bc_idx);
		
		if(result < 0) {
			
			log.info("updateBoardCategoryIdx() fail !!");
			
			return false;
			
		}else {
			
			//새로운 게시판 DB에 insert
			result = boardMapper.createBoardCategory(boardCategoryDto);
			
			if(result > 0) {
				log.info("createBoardCategory succuss!!");
				return true;
			}else {
				log.info("createBoardCategory fail!!");
				return false;
			}
			
		}
						
	}
	
	//게시판 name,idx 수정을 위한 dto 요청
	public List<BoardCategoryDto> getBoardCategoryForModify(BoardCategoryDto boardCategoryDto) {
		log.info("getBoardCategoryForModify()");
		
		int bc_no = boardCategoryDto.getBc_no();
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryForModify(bc_no);
						
		return boardCategoryDtos;
	}
	
	//특정 게시판 카테고리 정보 가져오기
	public Object getBoardInfo(int bc_no) {
		log.info("getBoardInfo()");
		
		//no값으로 dto가져오는 코드 재사용
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryForModify(bc_no);
		
		Map<String, Object> boardCategoryDto = new HashMap<>();
		boardCategoryDto.put("boardCategoryDto", boardCategoryDtos.get(0));				;
		
		return boardCategoryDto;
	}

//	public ResponseEntity<String> uploadFiles(List<MultipartFile> files) {
//		log.info("uploadFiles()");
//		log.info("files: {}", files);
//
//		// Request Header 설정
//		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//		// Request body 설정	
//		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
//		
////	requestBody.add("file", file.getResource());
//		
//		// Request Entity
//		HttpEntity<MultiValueMap<String, Object>> responseEntity = new HttpEntity<>(requestBody, headers);
//
//		// API 호출
////		String severURL = "http://14.42.124.93:8091/upload_file";
//		String severURL = "http://localhost:8091/upload_file"; //local
//		ResponseEntity<String> response = restTemplate.postForEntity(severURL, responseEntity, String.class);
////		Object response = restTemplate.postForEntity(severURL, responseEntity, String.class);
//		
//		return response;
//	}
	
	//게시글 이미지 저장 후 이미지 이름 가져오기
    public ResponseEntity<String> uploadFiles(List<MultipartFile> files, int bp_category_no, int bp_writer_no) {
        
    	try {
    		
    		log.info("uploadFiles()");
    		log.info("files: {}", files);
    		
    		// Request Header 설정
    		HttpHeaders headers = new HttpHeaders();
    		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    		
    		// Request body 설정	(파일 1개만 보낼 때)
//    		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
//		
//    		requestBody.add("file", file.getResource());
    		
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
    		
    		requestBody.add("bp_category_no", bp_category_no);
    		requestBody.add("bp_writer_no", bp_writer_no);
    		
    		// Request Entity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            // API 호출
            String serverURL = "http://localhost:8091/upload_file"; //local
            ResponseEntity<String> response = restTemplate.postForEntity(serverURL, requestEntity, String.class);

            return response;
			
		} catch (IOException e) {
			log.error("파일 업로드 중 오류 발생: {}", e.getMessage());
			
			return null;
		}
  	        
    }
    
    // 게시글 DB에 저장 후 결과 값 가져오기
	public Boolean createConfirm(List<String> savedFileNames, int bp_category_no, int bp_writer_no, String bp_title,
			String old_bp_body, String bp_dir_name) {
		log.info("createConfirm()");
		
		String bp_body = old_bp_body;
		
		if(savedFileNames != null) {
			
			// 정규 표현식 패턴
			Pattern pattern = Pattern.compile("img src=\"[^\"]*\"");
			Matcher matcher = pattern.matcher(old_bp_body);
			
			StringBuilder new_bp_body = new StringBuilder();
			int index = 0;
			
			while (matcher.find()) {
				String oldSrc = matcher.group();
				String newSrc = "img src=\"" + savedFileNames.get(index++) + "\"";
				matcher.appendReplacement(new_bp_body, newSrc);
			}
			matcher.appendTail(new_bp_body);
			
			bp_body = new_bp_body.toString();
			
		}
		
 		BoardPostsDto boardPostsDto = new BoardPostsDto();
 		
 		boardPostsDto.setBp_category_no(bp_category_no);
 		boardPostsDto.setBp_writer_no(bp_writer_no);
 		boardPostsDto.setBp_title(bp_title);
 		boardPostsDto.setBp_body(bp_body);
 		boardPostsDto.setBp_dir_name(bp_dir_name);
		
		int result = boardMapper.createConfirm(boardPostsDto);
        
		if(result <= 0) {
			log.info("createConfirm() insert fail!!");
			return false;
		}else {
			log.info("createConfirm() insert success!!");
			
			int bc_item_cnt = boardItemCntUpdater.selectCountBoardPostsByBcNo(bp_category_no);
			log.info("bc_item_cnt: "+bc_item_cnt);
			Boolean upDateResult = boardItemCntUpdater.updateBoardCategoryForBcItemCntByBcNo(bp_category_no, bc_item_cnt);
			
			return upDateResult;
		}
				
	}
	
	// 특정 게시판 페이지 번호에 따른 게시물 리스트들 가져오기
	public Map<String, Object> getBoardPostsListWithPage(int bp_category_no, int page, String sortValue, String order) {
		log.info("getBoardPostsListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sortValue", sortValue);
		pagingParams.put("order", order);
		pagingParams.put("bp_category_no", bp_category_no);
		
		List<BoardPostsDto> boardPostsDtos = boardMapper.getBoardPostsListWithPage(pagingParams);
		pagingList.put("boardPostsDtos", boardPostsDtos);
		
		return pagingList;
	}
	
	// 특정 게시판 게시물 총 페이지 개수 가져오기
	public Map<String, Object> getBoardPostsListPageNum(int bp_category_no, int page) {
		
		Map<String, Object> boardPostsLisByCategoryPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int boardPostsListByCategoryCnt = boardMapper.getBoardPostsByCategoryCnt(bp_category_no);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) boardPostsListByCategoryCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		boardPostsLisByCategoryPageNum.put("boardPostsListCnt", boardPostsListByCategoryCnt);
		boardPostsLisByCategoryPageNum.put("page", page);
		boardPostsLisByCategoryPageNum.put("maxPage", maxPage);
		boardPostsLisByCategoryPageNum.put("startPage", startPage);
		boardPostsLisByCategoryPageNum.put("endPage", endPage);
		boardPostsLisByCategoryPageNum.put("blockLimit", blockLimit);
		boardPostsLisByCategoryPageNum.put("pageLimit", pageLimit);
		
		return boardPostsLisByCategoryPageNum;
		
	}
	
	//게시판 순서 변경
	public int modifyCategoryIdx(int bc_no, int current_bc_idx, int bc_idx) {
		log.info("modifyCategoryIdx()");
		
		Map<String, Object> modifyParams = new HashMap<>();
		
		modifyParams.put("bc_no", bc_no);
		modifyParams.put("current_bc_idx", current_bc_idx);
		modifyParams.put("bc_idx", bc_idx);
		
		int result = 0;
		
		result = boardMapper.matchingModifyCategoryIdx(modifyParams);
		
		if(result > 0) {
			
			result = boardMapper.targetModifyCategoryIdx(modifyParams);
			
			if(result <= 0) {
				log.info("targetModifyCategoryIdx() error!");
			}
			
		}else {
			log.info("matchingModifyCategoryIdx() error!");
		}
		
		return result;
	}
	
	// 페이지에 따른 게시판 카테고리 가져오기(검색한 게시판 카테고리)
	public Map<String, Object> getSearchBoardCategoryListWithPage(String searchPart, String searchString, int page) {
		log.info("getSearchBoardCategoryListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		List<BoardCategoryDto> searchBoardCategoryDtos = boardMapper.getSearchBoardCategory(pagingParams);
				
		pagingList.put("boardCategoryDtos", searchBoardCategoryDtos);
		
		return pagingList;
	}
	
	// 게시판 카테고리의 총 페이지 개수 구하기(검색한 게시판 카테고리)
	public Map<String, Object> getSearchBoardCategoryListPageNum(String searchPart, String searchString, int page) {
		log.info("getSearchBoardCategoryListPageNum()");
		
		Map<String, Object> searchBoardCategoryListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		// 전체 리스트 개수 조회
		int searchBoardCategoryListCnt = boardMapper.getSearchBoardCategoryListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchBoardCategoryListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchBoardCategoryListPageNum.put("searchBoardCategoryListCnt", searchBoardCategoryListCnt);
		searchBoardCategoryListPageNum.put("page", page);
		searchBoardCategoryListPageNum.put("maxPage", maxPage);
		searchBoardCategoryListPageNum.put("startPage", startPage);
		searchBoardCategoryListPageNum.put("endPage", endPage);
		searchBoardCategoryListPageNum.put("blockLimit", blockLimit);
		searchBoardCategoryListPageNum.put("pageLimit", pageLimit);
		
		return searchBoardCategoryListPageNum;
	}
	
	//게시판 카테고리 정보 수정
	public boolean modifyCategoryConfirm(BoardCategoryDto boardCategoryDto, int current_bc_idx) {
		log.info("getSearchBoardCategoryListPageNum()");
		
		Map<String, Object> modifyParams = new HashMap<>();
		
		modifyParams.put("current_bc_idx", current_bc_idx);
		modifyParams.put("bc_idx", boardCategoryDto.getBc_idx());
		
		int result = 0;
		
		result = boardMapper.matchingModifyCategoryIdx(modifyParams);
		
		if(result > 0) {
			
			result = boardMapper.modifyCategoryConfirm(boardCategoryDto);
			
			if(result <= 0) {
				log.info("modifyCategoryConfirm() error!");
				return false;
			}else {				
				return true;
			}			
			
		}else {
			log.info("matchingModifyCategoryIdx() error!");
			return false;
		}
		
	}
	

	

}
