package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardReplyDto {
	
	private int br_no;
	private int br_category_no;
	private int br_post_no;
	private boolean br_state;
	private String br_text;
	private String br_writer_no;
	private boolean br_is_deleted;
	private String br_reg_date;
	private String br_mod_date;
	
	private BoardPostsDto boardPostsDto;
}
