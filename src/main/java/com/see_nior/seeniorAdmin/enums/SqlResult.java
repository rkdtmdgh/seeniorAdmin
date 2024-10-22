package com.see_nior.seeniorAdmin.enums;

public enum SqlResult {
	
	SUCCESS(true),
	FAIL(false);

	// 반환 값 저장할 필드
	private boolean value;
	
	// 생성자 (싱글톤)
	SqlResult(boolean value) {
		this.value = value;
	}
	
	// getter
	public boolean getValue() {
		return value;
	}
	
}
