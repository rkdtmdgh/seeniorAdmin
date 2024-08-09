package com.see_nior.seeniorAdmin.account;

import java.io.IOException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AdminAccessDeniedHandler implements AccessDeniedHandler {@Override
	
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		log.info("handle()");
		
		// 인가 실패 시 ROLE_NOT_APPROVED 라면 로그 아웃 처리.
		Authentication authentication = (Authentication) request.getUserPrincipal();
		
		if (authentication.getAuthorities().stream()
				.anyMatch(grantedauthority -> grantedauthority.getAuthority().equals("ROLE_NOT_APPROVED"))) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		
		response.sendRedirect("/account/access_denied_page");
		
	}

}
