package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResultDto {

	private int brr_no;
	private int brr_report_no;
	private String brr_result;
	private int brr_answer_a_no;
	private int brr_is_deleted;
	private String brr_reg_date;
	private String brr_mod_date;
	
	private AdminAccountDto adminAccountDto;
	
}
