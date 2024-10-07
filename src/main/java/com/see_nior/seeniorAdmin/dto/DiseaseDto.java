package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseDto {

	private int d_no;
	private int d_category_no;
	private String d_name;
	private String d_info;
	private String d_good_food;
	private String d_bad_food;
	private boolean d_is_deleted;
	private String d_reg_date;
	private String d_mod_date;
	
	DiseaseCategoryDto diseaseCategoryDto;
	
}