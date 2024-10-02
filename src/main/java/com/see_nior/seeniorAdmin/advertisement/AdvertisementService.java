package com.see_nior.seeniorAdmin.advertisement;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.advertisement.mapper.AdvertisementMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdvertisementService {

	final private AdvertisementMapper advertisementMapper;
	
}
