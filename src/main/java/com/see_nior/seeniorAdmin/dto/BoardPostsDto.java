package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostsDto {

	private int bp_no;			//BOARD_REPLY br_post_no값 테이블 조인
	private int bp_category_no;	
	private String bp_title;			
	private String bp_body;				
	private int bp_writer_no;	//ADMIN_ACCOUNT a_no값 테이블 조인
	private String bp_account;	
	private int bp_report_state;		
	private int bp_view_cnt;
	private String bp_dir_name;
	private int bp_reply_cnt;
	private boolean bp_is_deleted;		
	private String bp_reg_date;		
	private String bp_mod_date;
	
	private AdminAccountDto adminAccountDto;
	private UserAccountDto userAccountDto;
	
}
