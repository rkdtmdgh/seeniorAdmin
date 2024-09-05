package com.see_nior.seeniorAdmin.disease;

import com.see_nior.seeniorAdmin.dto.DiseaseCategoryDto;

import lombok.Getter;

@Getter
public class DiseaseCategoryWrapper {

	private DiseaseCategoryDto diseaseCategoryDto;
	private int itemCnt;
	
	public DiseaseCategoryWrapper(DiseaseCategoryDto diseaseCategoryDto, int itemCnt) {
		this.diseaseCategoryDto = diseaseCategoryDto;
		this.itemCnt = itemCnt;
		
	}
	
	
	
}
