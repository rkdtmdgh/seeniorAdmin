package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaCategoryDto {

	private int bqc_no;
	private String bqc_name;
	private int bqc_is_deleted;
	private int bqc_item_cnt;
	private String bqc_reg_date;
	private String bqc_mod_date;
	
	private int bqc_unanswered_cnt;
	
}
