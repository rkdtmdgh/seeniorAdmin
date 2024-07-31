package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseCategoryDto {

	private int no;
	private String name;
	private boolean is_delete;
	private String reg_date;
	private String mod_date;
	
}