package com.see_nior.seeniorAdmin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.see_nior.seeniorAdmin.account.AdminAccessDeniedHandler;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");
		
		return new BCryptPasswordEncoder();
		
	}

	@Bean SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.cors(cors -> cors.disable())
			.csrf(csrf -> csrf.disable());
		
		http
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(
							"/css/**",
							"/image/**",
							"/js/**",
							"/quillEditor/**",
							"/error/**",
							"/account/sign_up_form",
							"/account/sign_up_confirm",
							"/account/sign_in_form",
							"/account/sign_in_confirm",
							"/account/sign_in_result/**",
							"/account/sign_in_ok",
							"/account/sign_in_ng",
							"/account/access_denied_page",
							"/account/is_account"
							).permitAll()
					.requestMatchers(
							"/account/admin_list_form",
							"/account/get_admin_list",
							"/account/search_admin_list",
							"/account/admin_modify_form",
							"/account/admin_modify_confirm",
							"/account/is_approval"
							).hasRole("SUPER_ADMIN")
					.anyRequest().hasAnyRole("SUPER_ADMIN", "SUB_ADMIN"));
		
		http
			.exceptionHandling(exceoptionConfig -> exceoptionConfig
					.accessDeniedHandler(new AdminAccessDeniedHandler()));
		
		http
			.formLogin(login -> login
					.loginPage("/account/sign_in_form")
					.loginProcessingUrl("/account/sign_in_confirm")
					.usernameParameter("a_id")
					.passwordParameter("a_pw")
					.successHandler((request, response, authentication) -> {
						log.info("admin sign in success handler");
						
						/*
						 * 로그인 전 URI 가져오기 
						 * RequestCache requestCache = new HttpSessionRequestCache(); SavedRequest
						 * savedRequest = requestCache.getRequest(request, response);
						 * 
						 * if (savedRequest != null) {
						 * 
						 * targetURI = savedRequest.getRedirectUrl();
						 * requestCache.removeRequest(request, response);
						 * 
						 * }
						 */
						
						response.sendRedirect("/account/sign_in_result?result=" + true);
						
					})
					.failureHandler(new CustumAuthenticationFailureHandler()));
		
		http
			.logout(logout -> logout
					.logoutUrl("/account/sign_out_confirm")
					.logoutSuccessHandler((request, response, authentication) -> {
						log.info("sign out success handler");

						HttpSession session = request.getSession(false);
						
						if (session != null) session.invalidate();
						
						response.sendRedirect("/");
						
					}));
		
		http
			.sessionManagement(sess -> sess
				.maximumSessions(1)		
				.maxSessionsPreventsLogin(false))
			.sessionManagement(sess -> sess
				.sessionFixation().newSession());
		
		return http.build();
	
	}
	
}