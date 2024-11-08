package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeDto {

	private int n_no;
	private String n_title;
	private String n_body;
	private int n_view_cnt;
	private boolean n_state;
	private boolean n_is_deleted;	
	private String n_reg_date;
	private String n_mod_date;
	
	private AdminAccountDto adminAccountDto;
	
}
