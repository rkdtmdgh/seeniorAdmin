package com.see_nior.seeniorAdmin.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class BoardService {
	
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
		
		//DB에서 board category 마지막 idx값 꺼내오기
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
	
    public ResponseEntity<String> uploadFiles(List<MultipartFile> files) {
        
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

}
