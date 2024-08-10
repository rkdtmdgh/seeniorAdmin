package com.see_nior.seeniorAdmin.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class CustumAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.info("onAuthenticationFailure()");
		
		String errMsg;
		
		if (exception instanceof BadCredentialsException) {
			
			errMsg = "아이디 또는 비밀번호가 잘못되었습니다.";
			
		} else if (exception.getCause() instanceof AccountException) {
			
			errMsg = exception.getCause().getMessage();
			
		} else {
			
			errMsg = "로그인 시도 중 오류가 발생했습니다.";
			
		}
		
		String encodedValue = URLEncoder.encode(errMsg, StandardCharsets.UTF_8.toString());
		response.sendRedirect("/account/sign_in_ng?errMsg=" + encodedValue);
		
	}
	
}
