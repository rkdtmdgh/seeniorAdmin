package com.see_nior.seeniorAdmin.board;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.see_nior.seeniorAdmin.board.mapper.BoardMapper;
import com.see_nior.seeniorAdmin.board.util.BoardItemCntUpdater;
import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;
import com.see_nior.seeniorAdmin.dto.BoardPostsDto;
import com.see_nior.seeniorAdmin.dto.DeleteBoardPostsDto;
import com.see_nior.seeniorAdmin.dto.DiseaseDto;
import com.see_nior.seeniorAdmin.util.ImageFileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {
	
	// 이미지 서버 경로
	private String imgServerPath = "127.0.0.1:8091/seeniorUploadImg/";
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	//필드 선언
	final private BoardMapper boardMapper;
	final private RestTemplate restTemplate;
	final private BoardItemCntUpdater boardItemCntUpdater;
	final private ImageFileService imageFileService;
	
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
	
	//게시글 이미지 저장 후 이미지 이름 가져오기
    public Boolean createConfirm(List<MultipartFile> files, BoardPostsDto boardPostsDto) {
    	log.info("createConfirm()");
    	
    	//admin에서 게시물 작성하면 bp_acccount 값은 무조건 "admin"으로 서버에서 설정함
    	boardPostsDto.setBp_account("admin");
    	
    	if(files != null) {
    		
    		//이미지 서버에 요청할 파일 저장 경로 생성
    		Date now = new Date();	      
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    		String date = dateFormat.format(now);
    		
    		String filePath = "\\board\\"
    				+boardPostsDto.getBp_category_no()
    				+"\\"+boardPostsDto.getBp_writer_no()
    				+"\\"+date;
    		//이미지 저장 요청
    		ResponseEntity<String> savedFiles = imageFileService.uploadFiles(files, filePath);
    	
    		if(savedFiles != null) {
    			log.info("uploadFiles succuess!");					
    			
    			ObjectMapper objectMapper = new ObjectMapper();
    			
    			try {
    				Map<String,Object> savedFileObj = objectMapper.readValue(savedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
    				log.info("savedFiles(string) to savedFileNames(object) success!");
    				
    				@SuppressWarnings("unchecked") //(List<String>) 강제 캐스팅 에러
    				List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
    				log.info("savedFileNames : {}",savedFileNames);
    				
    				String bp_body = boardPostsDto.getBp_body();
    				
    				if(savedFileNames != null) {
    					
    					// 정규 표현식 패턴
    					Pattern pattern = Pattern.compile("img src=\"[^\"]*\"");
    					Matcher matcher = pattern.matcher(boardPostsDto.getBp_body());
    					
    					StringBuilder new_bp_body = new StringBuilder();
    					int index = 0;
    					
    					while (matcher.find()) {
    						
    						String newSrc = "img src=\"http://" 
    								+ imgServerPath 
    								+"board/" 
    								+ boardPostsDto.getBp_category_no() 
    								+"/"
    								+ boardPostsDto.getBp_writer_no() 
    								+"/"
    								+ date 
    								+"/"
    								+ savedFileNames.get(index++) + "\"";
    						matcher.appendReplacement(new_bp_body, newSrc);
    					}
    					matcher.appendTail(new_bp_body);
    					
    					bp_body = new_bp_body.toString();
    					
    				}
    				    				
    				boardPostsDto.setBp_body(bp_body);
    				boardPostsDto.setBp_dir_name(date);
    				
    				int result = boardMapper.createConfirm(boardPostsDto);
    				
    				if(result <= 0) {
    					log.info("createConfirm() insert fail!!");
    					return false;
    				}else {
    					log.info("createConfirm() insert success!!");
    					
    					int bc_item_cnt = boardItemCntUpdater.selectCountBoardPostsByBcNo(boardPostsDto.getBp_category_no());
    					log.info("bc_item_cnt: "+bc_item_cnt);
    					Boolean upDateResult = boardItemCntUpdater.updateBoardCategoryForBcItemCntByBcNo(boardPostsDto.getBp_category_no(), bc_item_cnt);
    					
    					return upDateResult;
    				}
    				
    			} catch (Exception e) {
    				log.info("savedFiles(string) to savedFileNames(array) fail!");
    				e.printStackTrace();
    			}
    			
    			return true;
    		}else {
    			log.info("uploadFiles fail!");
    			
    			return false;
    		}
    		
    	}else {
    		
    		int result = boardMapper.createConfirm(boardPostsDto);
    		if(result <= 0) {
				log.info("createConfirm() insert fail!!");
				return false;
			}else {
				log.info("createConfirm() insert success!!");
				
				int bc_item_cnt = boardItemCntUpdater.selectCountBoardPostsByBcNo(boardPostsDto.getBp_category_no());
				log.info("bc_item_cnt: "+bc_item_cnt);
				Boolean upDateResult = boardItemCntUpdater.updateBoardCategoryForBcItemCntByBcNo(boardPostsDto.getBp_category_no(), bc_item_cnt);
				
				return upDateResult;
			}
		}
    	
		  	        
    }//createConfirm() END
    
    // 게시글 DB에 저장 후 결과 값 가져오기
//	public Boolean createConfirm(List<String> savedFileNames, int bp_category_no, int bp_writer_no, String bp_title,
//			String old_bp_body, String bp_dir_name, String bp_writer_id) {
//		log.info("createConfirm()");
//		
//		String bp_body = old_bp_body;
//		
//		if(savedFileNames != null) {
//			
//			// 정규 표현식 패턴
//			Pattern pattern = Pattern.compile("img src=\"[^\"]*\"");
//			Matcher matcher = pattern.matcher(old_bp_body);
//			
//			StringBuilder new_bp_body = new StringBuilder();
//			int index = 0;
//			
//			while (matcher.find()) {
//				
//				String newSrc = "img src=\"http://" 
//						+ imgServerPath 
//						+"board/" 
//						+ bp_category_no 
//						+"/"
//						+ bp_writer_no 
//						+"/"
//						+ bp_dir_name 
//						+"/"
//						+ savedFileNames.get(index++) + "\"";
//				matcher.appendReplacement(new_bp_body, newSrc);
//			}
//			matcher.appendTail(new_bp_body);
//			
//			bp_body = new_bp_body.toString();
//			
//		}
//		
// 		BoardPostsDto boardPostsDto = new BoardPostsDto();
// 		
// 		boardPostsDto.setBp_category_no(bp_category_no);
// 		boardPostsDto.setBp_writer_no(bp_writer_no);
// 		boardPostsDto.setBp_writer_id(bp_writer_id);
// 		boardPostsDto.setBp_account("admin");
// 		boardPostsDto.setBp_title(bp_title);
// 		boardPostsDto.setBp_body(bp_body);
// 		boardPostsDto.setBp_dir_name(bp_dir_name);
//		
//		int result = boardMapper.createConfirm(boardPostsDto);
//        
//		if(result <= 0) {
//			log.info("createConfirm() insert fail!!");
//			return false;
//		}else {
//			log.info("createConfirm() insert success!!");
//			
//			int bc_item_cnt = boardItemCntUpdater.selectCountBoardPostsByBcNo(bp_category_no);
//			log.info("bc_item_cnt: "+bc_item_cnt);
//			Boolean upDateResult = boardItemCntUpdater.updateBoardCategoryForBcItemCntByBcNo(bp_category_no, bc_item_cnt);
//			
//			return upDateResult;
//		}
//				
//	}
	
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
		
		boolean result = false;
		
		if( current_bc_idx != boardCategoryDto.getBc_idx()) {
			log.info("current_bc_idx != boardCategoryDto");
			
			Map<String, Object> parm = new HashMap<>();
			
			parm.put("current_bc_idx", current_bc_idx);
			parm.put("bc_idx", boardCategoryDto.getBc_idx());
			
			if(current_bc_idx > boardCategoryDto.getBc_idx()) {				
				result = boardMapper.modifyCategoryIdxByBetweenAdd(parm);
			}else{
				result = boardMapper.modifyCategoryIdxByBetweenSub(parm);
			}
			
			
			if(!result) {
				log.info("modifyCategoryIdxByBetween() fail!!");
			}else {
				result = boardMapper.modifyCategoryConfirm(boardCategoryDto);
				
				if(!result) {
					log.info("modifyCategoryConfirm() fail!!");
				}
			}
			
		}else {	
			log.info("current_bc_idx == boardCategoryDto");
			result = boardMapper.modifyCategoryConfirm(boardCategoryDto);
			
			if(!result) {
				log.info("modifyCategoryConfirm() fail!!");
			}
		}
					
		return result;
		
	}
	
	//게시판 카테고리 삭제 요청
	public boolean deleteCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("deleteCategoryConfirm()");
		
		int result = -1;
		
		result = boardMapper.subBoardCategoryIdxForDelete(boardCategoryDto);
				
		if(result < 0) {
			log.info("subBoardCategoryIdxForDelete fail!!");
			return false;
		}else {		
			result = boardMapper.deleteCategoryConfirm(boardCategoryDto);
			
			if(result <= 0) {
				log.info("deleteCategoryConfirm fail!!");
				return false;
			}else {				
				return true;
			}
			
		}
		
	}
	
	//특정 게시물 정보 가져오기
	public BoardPostsDto modifyForm(int bp_no) {
		log.info("modifyForm()");
		
		//특정 게시물 정보 가져오기 select
		List<BoardPostsDto> boardPostsDtos = boardMapper.modifyForm(bp_no);
		log.info("boardPostsDtos: {}",boardPostsDtos);
		
		if(boardPostsDtos.size() == 0) {
			log.info("modifyForm() fail!!");
			return null;
		}
		
		return boardPostsDtos.get(0);
		
	}
	
	// 페이지 번호에 따른 검색한 게시물 리스트들 가져오기
	public Map<String, Object> getSearchPostsListWithPage(int bc_no, String searchPart, String searchString, int page) {
		log.info("getSearchPostsListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("bc_no", bc_no);
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		
		List<DiseaseDto> searchBoardPostsDtos = boardMapper.getSearchBoardPosts(pagingParams);
		pagingList.put("boardPostsDtos", searchBoardPostsDtos);
		
		return pagingList;
	}
	
	// 검색 게시물 총 페이지 개수 가져오기
	public Map<String, Object> getSearchPostsListPageNum(int bc_no, String searchPart, String searchString, int page) {
		log.info("getSearchPostsListPageNum()");
		
		Map<String, Object> searchDiseaseListPageNum = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("searchPart", searchPart);
		pagingParams.put("searchString", searchString);
		pagingParams.put("bc_no", bc_no);
		
		// 전체 리스트 개수 조회
		int searchBoardPostsListCnt = boardMapper.getSearchBoardPostsListCnt(pagingParams);
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) searchBoardPostsListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		searchDiseaseListPageNum.put("searchBoardPostsListCnt", searchBoardPostsListCnt);
		searchDiseaseListPageNum.put("page", page);
		searchDiseaseListPageNum.put("maxPage", maxPage);
		searchDiseaseListPageNum.put("startPage", startPage);
		searchDiseaseListPageNum.put("endPage", endPage);
		searchDiseaseListPageNum.put("blockLimit", blockLimit);
		searchDiseaseListPageNum.put("pageLimit", pageLimit);
		
		return searchDiseaseListPageNum;
	}
	
	//특정 게시물 수정 요청
	public Boolean modifyConfirm(List<MultipartFile> files, BoardPostsDto boardPostsDto, List<String> deleteFileNames) {
		log.info("modifyConfirm()");
		log.info("getBp_dir_name: {}",boardPostsDto.getBp_dir_name());
		String filePath = "";
		if( files != null && boardPostsDto.getBp_dir_name() == null ) {
			//날짜시간생성
			log.info("dir_name is null new make dir!");
    		Date now = new Date();	      
    	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    		String date = dateFormat.format(now);
    		boardPostsDto.setBp_dir_name(date);
		}
		//이미지 서버에 요청할 파일 저장 경로 생성
		filePath = "\\board\\"
					+boardPostsDto.getBp_category_no()
					+"\\"+boardPostsDto.getBp_writer_no()
					+"\\"+boardPostsDto.getBp_dir_name();
		
		//새로운 이미지 저장이 필요 없는 경우
		if(files == null) {
			
			//DB에 업데이트 요청
			int result = boardMapper.modifyConfirm(boardPostsDto);
			
			if(result <= 0) {
				log.info("boardMapper.modifyConfirm() fail!");
				return false;
			}else {
				//삭제할 이미지가 있는 경우
				if(deleteFileNames.size() != 0) {
					
					//ImageFileService클래스 deleteFiles()요청
					//파라미터 값 = 문자열 배열(삭제할 파일 이름들), 삭제할 파일이 있는 폴더 경로(filePath)
					ResponseEntity<String> deletedFiles = imageFileService.deleteFiles(deleteFileNames, filePath);
					
					if(deletedFiles != null){
						ObjectMapper objectMapper = new ObjectMapper();
						
						try {
							Map<String, Object> deletedFileObj = objectMapper.readValue(deletedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
							log.info("deleteFiles(string) to deleteFileNames(object) success!");
							
							@SuppressWarnings("unchecked") //(List<String>) 강제 캐스팅 에러
							List<String> deletedFileNames = (List<String>) deletedFileObj.get("deletedFileNames");
							log.info("deletedFileNames : {}",deletedFileNames);
							
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}			
						
					}else {
						log.info("ImageFileService.deleteFiles() fail!");
						return false;
					}
				}
				return true;
			}
			
		}
				
		//이미지 서버에 새로운 이미지 저장 저장 요청
		ResponseEntity<String> savedFiles = imageFileService.uploadFiles(files, filePath);
		
		//이미지 서버에 새 이미지가 정상적으로 저장 되었다면
		if(savedFiles != null) {
			log.info("modifyConfirm() uploadFiles succuess!");					
			
			//이미지 서버에서 받은 response값을 Map형식으로 맵핑 할 클래스 생성
			ObjectMapper objectMapper = new ObjectMapper();
			
			try {
				Map<String,Object> savedFileObj = objectMapper.readValue(savedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
				log.info("savedFiles(string) to savedFileNames(object) success!");
				
				@SuppressWarnings("unchecked") //(List<String>) 강제 캐스팅 에러
				List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
				log.info("savedFileNames : {}",savedFileNames);
				
				String bp_body = boardPostsDto.getBp_body();
				
				//이미지 서버에 정상적으로 새 이미지가 저장 되었다면
				if(savedFileNames != null) {
					
					// 정규 표현식 패턴
					Pattern pattern = Pattern.compile("img src=\"blob:[^\"]*\"");
					Matcher matcher = pattern.matcher(boardPostsDto.getBp_body());
					
					StringBuilder new_bp_body = new StringBuilder();
					int index = 0;
					
					while (matcher.find()) {
						
						String newSrc = "img src=\"http://" 
								+ imgServerPath 
								+"board/" 
								+ boardPostsDto.getBp_category_no() 
								+"/"
								+ boardPostsDto.getBp_writer_no() 
								+"/"
								+ boardPostsDto.getBp_dir_name() 
								+"/"
								+ savedFileNames.get(index++) + "\"";
						matcher.appendReplacement(new_bp_body, newSrc);
					}
					matcher.appendTail(new_bp_body);
					
					bp_body = new_bp_body.toString();
					
				}
				
		 		BoardPostsDto newBoardPostsDto = new BoardPostsDto();
		 		
		 		newBoardPostsDto.setBp_no(boardPostsDto.getBp_no());
		 		newBoardPostsDto.setBp_title(boardPostsDto.getBp_title());
		 		newBoardPostsDto.setBp_dir_name(boardPostsDto.getBp_dir_name());
		 		newBoardPostsDto.setBp_body(bp_body);
				
		 		//DB에 업데이트 요청
				int result = boardMapper.modifyConfirm(newBoardPostsDto);
				
				if(result <= 0) {
					log.info("boardMapper.modifyConfirm() fail!");
					return false;
				}
				
				if(deleteFileNames.size() != 0) {
					//ImageFileService클래스 deleteFiles()요청
					//파라미터 값 = 문자열 배열(삭제할 파일 이름들), 삭제할 파일이 있는 폴더 경로(filePath)
					ResponseEntity<String> deletedFiles = imageFileService.deleteFiles(deleteFileNames, filePath);
					if(deletedFiles == null){
						
						log.info("ImageFileService.deleteFiles() fail!");
						return false;
					}
					
					Map<String,Object> deletedFileObj = objectMapper.readValue(deletedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
					log.info("deleteFiles(string) to deleteFileNames(object) success!");
					
					@SuppressWarnings("unchecked") //(List<String>) 강제 캐스팅 에러
					List<String> deletedFileNames = (List<String>) deletedFileObj.get("deletedFileNames");
					log.info("deletedFileNames : {}",deletedFileNames);
				
				}
				
				
			} catch (Exception e) {
				log.info("savedFiles(string) to savedFileNames(array) fail!");
				e.printStackTrace();
			}
							
			return true;
		}else {
			log.info("uploadFiles fail!");
			
			return false;
		}
		
		
	}//modifyConfirm() END
	
	//게시글 삭제 요청
	@Transactional
	public boolean deleteConfirm(List<Integer> bp_nos) {
		log.info("deleteConfirm()");
		   try {
			   
			   for (int bp_no : bp_nos) {
				   
				   int deleteResult = boardMapper.deleteConfirm(bp_no);
				   
				   if (deleteResult <= 0) {
					   log.info("delete fail to database bp_no: {}", bp_no);
					   
					   throw new RuntimeException();
					   
				   }
				   
			   }
			
		   } catch (Exception e) {
			   log.error("deleteConfirm Error : {}",e);
			   
			   return false;
			   
		   }
		   
		   return true;
	}//deleteConfirm() END
	
	//게시물 삭제 요청 30일 경과 후 이미지 삭제요청
	//초 분 시 일 월 요일 년 (각 자리에 *는 모든 값을 의미)
	@Scheduled(cron = "0 1 0 * * ?") // 매일 자정 실행	
	public void deleteFolderRequest() {
		log.info("deleteFolderRequest()");
		
		List<DeleteBoardPostsDto> deleteBoardPostsDtos = boardMapper.getDeleteBoardPostsValid();  
		
		if(deleteBoardPostsDtos.size() != 0) {//최 상위 조건
			log.info("deleteBoardPostsDtos: {}",deleteBoardPostsDtos);
			
			List<String> deleteFolderPaths = new ArrayList();
			
			for(int i = 0; i < deleteBoardPostsDtos.size(); i++){
				String folderPath = "\\board\\"
				+deleteBoardPostsDtos.get(i).getDbp_category_no()
				+"\\"+deleteBoardPostsDtos.get(i).getDbp_writer_no()
				+"\\"+deleteBoardPostsDtos.get(i).getDbp_dir_name();
				deleteFolderPaths.add(folderPath);
			}
			
			//deletedFolders.getBody() = "1"(성공), "0"(실패 - 폴더 경로가 없음), "-1"(실패 - 이미지 서버 오류)
			ResponseEntity<String> deletedFolders = imageFileService.deleteFolders(deleteFolderPaths);
						
			if(deletedFolders.getBody().equals("1")) {//두번째 조건
				log.info("DELETEDFOLDERS SUCCESS!");
				
				boolean result = true;
				int UpdateResult = 0;
				
				for(int i = 0; i < deleteBoardPostsDtos.size(); i++) {//반복문 시작
					
					if(result) {//반복문 안쪽 첫번째 조건
						
						UpdateResult = boardMapper.updateDeleteBoardPostsIsDeleted(deleteBoardPostsDtos.get(i).getDbp_no());
						
						if(UpdateResult <= 0) {//반복문 안쪽 두번째 조건
							result = false;
						}
						
					}else {
						log.info("updateDeleteBoardPostsIsDeleted() fail!");
					}//반복문 안쪽 첫번째 조건 끝	
					
				}//반복문 끝
				
			}else if(deletedFolders.getBody().equals("0")){
				log.info("FOLDER NAME OR PATH NOT FOUND!!");
				log.info("response value: {}",deletedFolders.getBody());
			}else{
				log.info("FOLDER DELETE FAIL!!");
				log.info("response value: {}",deletedFolders.getBody());
			}//두번째 조건 끝
			
		}else {
			log.info("deleteBoardPostsDtos is null: {}",deleteBoardPostsDtos);
		}//최 상위 조건 끝
		
		
	}//deleteFolderRequest() END
	
		
	
}
