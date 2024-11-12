package com.see_nior.seeniorAdmin.util;

import java.io.IOException;
import java.util.List;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class ImageFileService {
	
	final private RestTemplate restTemplate;

    public ResponseEntity<String> uploadFiles(List<MultipartFile> files, String filePath) {
        
    	try {
    		
    		log.info("uploadFiles()");
    		log.info("files: {}", files);
    		log.info("filePath: {}", filePath);
    		
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
    		// 파라미터로 받은 파일 저장 경로를 filePath키 값으로 requestBody에 추가    		
    		requestBody.add("filePath", filePath);
    		
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
    
    public ResponseEntity<String> deleteFiles(List<String> files, String filePath) {
        
    	log.info("deleteFiles()");
		log.info("files: {}", files);
		log.info("filePath: {}", filePath);
		
		// Request Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				
		// Request body 설정 (파일 배열을 보낼 때)
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		
		for(int i = 0; i < files.size(); i++) {
			requestBody.add("files", files.get(i));
		}
		
		// 파라미터로 받은 파일 저장 경로를 filePath키 값으로 requestBody에 추가 
		requestBody.add("filePath", filePath);
		
		// Request Entity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		// API 호출
		String serverURL = "http://localhost:8091/delete_file"; //local
		ResponseEntity<String> response = restTemplate.postForEntity(serverURL, requestEntity, String.class);

		return response;
	}
    
    public ResponseEntity<String> deleteFolders(List<String> deleteDirs) {
        
    	log.info("deleteFolders()");
		log.info("deleteDirs: {}", deleteDirs);
		
		// Request Header 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
				
		// Request body 설정 (파일 배열을 보낼 때)
		MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
		
		// 파라미터로 받은 삭제할 폴더 경로를 deleteDirs키 값으로 requestBody에 추가 
		for(int i = 0; i < deleteDirs.size(); i++) {
			requestBody.add("deleteDirs", deleteDirs.get(i));
		}
				
		// Request Entity
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

		// API 호출
		String serverURL = "http://localhost:8091/delete_folder"; //local
		ResponseEntity<String> response = restTemplate.postForEntity(serverURL, requestEntity, String.class);

		return response;
	}
	
}
