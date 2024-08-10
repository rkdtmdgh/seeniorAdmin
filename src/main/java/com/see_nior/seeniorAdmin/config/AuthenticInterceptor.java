package com.see_nior.seeniorAdmin.config;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
        response.setHeader(HttpHeaders.EXPIRES, "0");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, post-check=0, pre-check=0, max-age=0");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
		
		return true;
		
	}
	
}
