package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostsDto {

	private int bp_no;				
	private int bp_category_no;	
	private String bp_title;			
	private String bp_body;				
	private int bp_writer_no;		
	private boolean bp_report_state;		
	private int bp_view_cnt;			
	private boolean bp_is_deleted;		
	private String bp_reg_date;		
	private String bp_mod_date;
	
}
