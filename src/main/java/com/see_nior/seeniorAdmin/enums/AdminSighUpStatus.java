package com.see_nior.seeniorAdmin.enums;

public enum AdminSighUpStatus {

	ALREADY(-1),
	SUCCESS(1),
	FAIL(0);
	
	// 반환 값 저장할 필드
	private int value;
	
	// 생성자 (싱글톤)
	AdminSighUpStatus(int value) {
		this.value = value;
	}
	
	// getter
	public int getValue() {
		return value;
	}
	
}
