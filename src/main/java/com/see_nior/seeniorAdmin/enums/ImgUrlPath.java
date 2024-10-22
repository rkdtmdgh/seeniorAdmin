package com.see_nior.seeniorAdmin.enums;

public enum ImgUrlPath {

	BOARD_PATH(""),
	ADVERTISEMENT_PATH("127.0.0.1:8091/seeniorUploadImg/advertisement/"),
	USER_PROFILE_PATH("");
	
	private String value;
	
	ImgUrlPath(String value) {
		this.value = value;
	}
	
	public String getPath() {
		return value;
	}
	
}
