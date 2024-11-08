package com.see_nior.seeniorAdmin.notice;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.dto.NoticeDto;
import com.see_nior.seeniorAdmin.notice.mapper.NoticeMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class NoticeService {

	private final NoticeMapper noticeMapper;

	// 전체 공지사항 정보 가져오기 by no
	public NoticeDto getNoticeInfoByNo(int n_no) {
		log.info("getNoticeInfoByNo()");
		
		return noticeMapper.selectNoticeInfoByNo(n_no);
		
	}
	
	
	
	
}
