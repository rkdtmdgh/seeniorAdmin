package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportCategoryDto {

	private int brc_no;
	private String brc_name;
	private boolean brc_is_deleted;
	private int brc_item_cnt;
	private String brc_reg_date;
	private String brc_mod_date;
	private int brc_unresulted_cnt;
	
}
