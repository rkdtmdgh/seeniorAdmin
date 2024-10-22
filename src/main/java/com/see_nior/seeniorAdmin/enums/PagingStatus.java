package com.see_nior.seeniorAdmin.enums;

import java.util.HashMap;
import java.util.Map;

public enum PagingStatus {

	PAGE_LIMIT(10),
	BLOCK_LIMIT(5);
	
	private int value;
	
	PagingStatus(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public int pagingStart(int page) {
		return (page - 1) * this.value;
	}
	
	public int maxPage(int listCnt) {
		return (int) (Math.ceil((double) listCnt / this.value));
	}
	
	public int startPage(int page) {
		return ((int) (Math.ceil((double) page / this.value)) - 1) * this.value + 1;
	}
	
}
