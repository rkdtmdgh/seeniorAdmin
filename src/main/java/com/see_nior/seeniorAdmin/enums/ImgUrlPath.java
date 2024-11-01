package com.see_nior.seeniorAdmin.enums;

public enum ImgUrlPath {
	
	SERVER_PATH("127.0.0.1:8091/seeniorUploadImg/"),
	BOARD_PATH(""),
	ADVERTISEMENT_PATH("127.0.0.1:8091/seeniorUploadImg/advertisement/"),
	QNA_NOTICE_PATH("127.0.0.1:8091/seeniorUploadImg/qna/notice/"),
	USER_PROFILE_PATH("");
	
	private String value;
	
	ImgUrlPath(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
