package com.see_nior.seeniorAdmin.advertisement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.AdvertisementDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequiredArgsConstructor
@RequestMapping("/advertisement")
public class AdvertisementController {

	final private AdvertisementService advertisementService;
	
	// 광고 등록 양식
	@GetMapping("/info/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "advertisement/create_form";
		
		return nextPage;
		
	}
	
	
	// 광고 등록 확인
	@ResponseBody
	@PostMapping("/info/create_confirm")
	public boolean createConfirm(AdvertisementDto advertisementDto) {
		log.info("createConfirm()");
		
		boolean createResult = advertisementService.createConfirm(advertisementDto);
		
		return createResult;
		
	}
	
}
