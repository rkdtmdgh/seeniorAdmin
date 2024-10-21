package com.see_nior.seeniorAdmin.sse;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.see_nior.seeniorAdmin.dto.QnaDto;
import com.see_nior.seeniorAdmin.qna.QnaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Service
public class SseService {

	final private QnaService qnaService;
	
	public SseEmitter subcribe(long a_id) {
		log.info("subscribe()");
		
		
		return null;
	}
	
	public SseEmitter getUnansweredQuestionsNotifications() {
		log.info("getUnansweredQuestionsNotifications()");
		
		// 타임 아웃 설정
		SseEmitter emitter = new SseEmitter(60_000L);
		
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				
				List<QnaDto> unansweredQnaDtos = qnaService.getUnansweredQuestions();
				
				if (!unansweredQnaDtos.isEmpty()) {
					
					emitter.send(SseEmitter.event()
							.data("새로운 질문이 " + unansweredQnaDtos.size() + "개 있습니다."));
					
				}
				
				TimeUnit.SECONDS.sleep(10);
				
				
			} catch (Exception e) {
				
				emitter.completeWithError(e);
				
			}
			
		});
		
		return emitter;
		
	}


	
}
