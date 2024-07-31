package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDto {

	private int no;
	private String name;
	private String info;
	private String good_food;
	private String bad_food;
	private boolean is_deleted;
	private String reg_date;
	private String mod_date;
	
	DiseaseCategoryDto diseaseCategoryDto;
	
}