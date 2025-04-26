package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardNoticePostsDto {

	private int bn_no;
	private int bn_category_no;
	private String bn_title;	
	private String bn_body;		
	private int bn_writer_no;	
	private String bn_dir_name;	
	private int bn_view_cnt;	
	private boolean bn_state;		
	private boolean bn_is_deleted;	
	private int bn_reg_date;	
	private int bn_mod_date;
	
}
