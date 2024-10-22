package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QnaNoticeDto {

	private int bqn_no;
	private String bqn_title;
	private String bqn_body;
	private int bqn_view_cnt;
	private boolean bqn_state;
	private boolean bqn_is_deleted;
	private String bqn_reg_date;
	private String bqn_mod_date;
	
	private AdminAccountDto adminAccountDto;
	
}
