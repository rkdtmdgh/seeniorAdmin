package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBoardPostsDto {
	
	private int dbp_no;
	private int dbp_post_no;						
	private int dbp_category_no;		
	private int dbp_writer_no;
	private String dbp_dir_name;
	private boolean dbp_is_valid;
	private boolean dbp_is_deleted;
	private boolean dbp_is_recovered;	
	private String dbp_request_time;
	
}
