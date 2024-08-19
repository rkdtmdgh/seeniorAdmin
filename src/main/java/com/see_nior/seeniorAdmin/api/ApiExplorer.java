package com.see_nior.seeniorAdmin.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiExplorer {
	
	public ApiExplorer() throws IOException {
		log.info("ApiExplorer()");
		
		StringBuilder urlBuilder = new StringBuilder("http://openapi.foodsafetykorea.go.kr/api"); 	// open API URL
		urlBuilder.append("/" + URLEncoder.encode("04745747c876457c8d98", "UTF-8"));				// 인증키 (sample 사용시에는 호출이 제한됨)
		urlBuilder.append("/" + URLEncoder.encode("seenior", "UTF-8"));								// 서비스명 (대소문자 구분 필수)
		urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8"));								// 요청 파일 타입 (xml, xmlf, xls, json)
		urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8"));									// 요청 시작 위치 (sample 인증키 사용시 5 이내 숫자)
		urlBuilder.append("/" + URLEncoder.encode("5", "UTF-8"));									// 요청 종료 위치 (sample 인증키 사용시 5이상 숫자 선택 안 됨)
		
		/*
		추가 요청 인자. 자세한 내용은 각 API마다 다름. '요청인자' 부분에 자세히 나와있다.
		모든 인자의 데이터 타입 = String
			
			keyId(필수)				인증키							OpenAPI에서 발급된 인증키
			serviceId(필수)			서비스명						요청 대상인 해당 서비스명(대,소문자 구분 필수)
			dataType(필수)			요청 파일 타입					xml : xml파일, json : json파일
			startIdx(필수) 			요청 시작 위치					정수 입력
			endIdx(필수)			요청 종료 위치					정수 입력
			RCP_NM(선택)			메뉴명							추가 파라미터값
			RCP_PARTS_DTLS(선택)	재료 정보1						추가 파라미터값
			CHNG_DT(선택)			변경일자(YYYYMMDD)				변경일자 기준 이후 자료 출력
			RCP_PAT2(선택)			요리종류(ex:반찬, 국, 후식 등)	추가 파라미터값
		*/
		
		// 추가 요청인자들을 입력하였을 경우의 URL 
		// http://openapi.foodsafetykorea.go.kr/api/인증키/서비스명/요청파일타입/요청시작위치/요청종료위치/변수명=값&변수명=값2
		// ex) http://openapi.foodsafetykorea.go.kr/api/sample/COOKRCP01/xml/1/5/RCP_NM=값 &RCP_PARTS_DTLS=값 &CHNG_DT=값 &RCP_PAT2=값
		
		urlBuilder.append("/" + URLEncoder.encode("RCP_NM=" + "", "UTF-8"));			// 메뉴명	
		urlBuilder.append("/" + URLEncoder.encode("&RCP_PARTS_DTLS=" + "", "UTF-8"));	// 재료 정보	
		urlBuilder.append("/" + URLEncoder.encode("&CHNG_DT=" + "", "UTF-8"));			// 변경 일자	
		urlBuilder.append("/" + URLEncoder.encode("&RCP_PAT2=" + "", "UTF-8"));			// 요리 종류	
		
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Cotnet-type", "application/jsom");
		log.info("Response code: " + conn.getResponseCode());	// 연결 자체에 대한 확인
		BufferedReader rd;
		
		// 서비스코드가 정상이면 200 ~ 300 사이의 숫자가 나옴
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			
		}
		
		StringBuilder sb = new StringBuilder();
		
		String line;
		
		while ((line = rd.readLine()) != null) {
			sb.append(line);
			
		}
		
		rd.close();
		conn.disconnect();
		log.info(sb.toString());
		
		
	}

}
