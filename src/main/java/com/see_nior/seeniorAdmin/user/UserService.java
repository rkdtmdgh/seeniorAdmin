package com.see_nior.seeniorAdmin.user;

import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService {
	
	public Map<String, Object> getUserPagingList(int page) {
		log.info("getUserPagingList()");
		
		return null;
	}

}
