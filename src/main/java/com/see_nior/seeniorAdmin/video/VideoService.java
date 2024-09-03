package com.see_nior.seeniorAdmin.video;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.video.mapper.VideoMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class VideoService {

	final private VideoMapper videoMapper;
	
	public VideoService(VideoMapper videoMapper) {
		this.videoMapper = videoMapper;
		
	}
	
}
