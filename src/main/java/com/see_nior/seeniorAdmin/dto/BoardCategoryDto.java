package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardCategoryDto {
	
	private int bc_no; 
	private String bc_name;
	private int bc_idx;
	private int bc_item_cnt;
	private boolean bc_is_deleted;
	private String bc_reg_date;
	private String bc_mod_date;
	
}
