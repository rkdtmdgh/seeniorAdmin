package com.see_nior.seeniorAdmin.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.see_nior.seeniorAdmin.qna.QnaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
public class NotificationsController {

	final private NotificationsService notificationsService;
	final private QnaService qnaService;
	
	@GetMapping(path = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter sendUnansweredQuestionsNotifications() {
		return notificationsService.getUnansweredQuestionsNotifications();
	}
	
}
