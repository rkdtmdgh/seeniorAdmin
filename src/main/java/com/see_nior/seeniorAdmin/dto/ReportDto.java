package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDto {

	private int br_no;
	private int br_category_no;
	private int br_post_no;
	private String br_title;
	private String br_reason;
	private int br_reporter_no;
	private int br_result_no;
	private boolean br_state;
	private boolean br_is_deleted;
	private String br_reg_date;
	private String br_mod_date;	
	
	private BoardPostsDto boardPostsDto;
	
	private ReportCategoryDto reportCategoryDto;
	
	private ReportResultDto reportResultDto;
	
	private UserAccountDto userAccountDto;
	
}
