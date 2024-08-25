package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseCategoryDto {

	private int dc_no;
	private String dc_name;
	private boolean dc_is_deleted;
	private String dc_reg_date;
	private String dc_mod_date;
	
}