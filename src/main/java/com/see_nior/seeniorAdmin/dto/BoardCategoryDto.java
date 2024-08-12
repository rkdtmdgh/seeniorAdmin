package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCategoryDto {
	
	private int no; 
	private String name;
	private int idx;
	private boolean is_deleted;
	private String reg_date;
	private String mod_date;
	
}
