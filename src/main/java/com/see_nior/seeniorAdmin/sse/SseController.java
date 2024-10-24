package com.see_nior.seeniorAdmin.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class SseController {

	final private SseService sseService;
	
	// SSE 는 텍스트 기반 이벤트를 전송하기 때문에 TEXT_EVENT_STREAM_VALUE 적용
	@GetMapping(path = "/subscribe/{a_id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(@PathVariable(value = "a_id") long a_id) {
		log.info("subscribe()");
		
		return sseService.subcribe(a_id);
	}
	
}
