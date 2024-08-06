package com.see_nior.seeniorAdmin.config;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

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
			.authorizeHttpRequests((request) -> request
					.requestMatchers(
							"/css/**",
							"/image/**",
							"/js/**",
							"/error/**",
							"/account/sign_up_form",
							"/account/sign_up_confirm",
							"/account/sign_in_form",
							"/account/sign_in_confirm",
							"/account/sign_in_result/**",
							"/account/sign_in_ok",
<<<<<<< HEAD
							"/account/sign_in_ng",
							"/disease/create_category_form",
							"/disease/create_category_confirm",
							"/disease/create_form",
							"/disease/create_confirm",
							"/disease/disease_list",
							"/disease/get_all_disease_list",
							"/disease/get_category_list"
=======
							"/account/sign_in_ng"
>>>>>>> 3446125e7e791a9394bf252b9348a01913420f3a
							).permitAll()
					.requestMatchers(
							"/account/get_admin_list",
							"/account/is_approval"
							).hasRole("SUPER_ADMIN")
					.requestMatchers(
							"/",
							"/account/modify_form",
							"/account/modify_confirm",
							"/account/delete_confirm",
							"/user/**",
							"/disease/**",
							"/board/**",
							"/video/**",
							"/meal_providor/**"
							).hasAnyRole("SUPER_ADMIN", "SUB_ADMIN"));
		
		http
			.formLogin(login -> login
					.loginPage("/account/sign_in_form")
					.loginProcessingUrl("/account/sign_in_confirm")
					.usernameParameter("id")
					.passwordParameter("pw")
					.successHandler((request, response, authentication) -> {
						log.info("admin sign in success handler");
						
						String targetURI = "/account/sign_in_result?logined=" + true;
						
						RequestCache requestCache = new HttpSessionRequestCache();
						SavedRequest savedRequest = requestCache.getRequest(request, response);
						
						if (savedRequest != null) {
							
							targetURI = savedRequest.getRedirectUrl();
							requestCache.removeRequest(request, response);
							
						}
						
						response.sendRedirect(targetURI);
						
					})
					.failureHandler((request, response, exception) -> {
						log.info("sign in fail handler");
						
						String encodedValue = URLEncoder.encode(exception.getMessage(), StandardCharsets.UTF_8.toString());
						
						response.sendRedirect("/account/sign_in_result?logined=" + false + "&errMsg=" + encodedValue);
						
					}));
		
		http
			.logout(logout -> logout
					.logoutUrl("/account/sign_out_confirm")
					.logoutSuccessHandler((request, response, authentication) -> {
						log.info("sign out success handler");

						HttpSession session = request.getSession();
						session.invalidate();
						
						response.sendRedirect("/");
						
					}));
		
		return http.build();
	
	}
	
}
