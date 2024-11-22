package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteQnaoticeDto {
	
	private int dbqn_no;
	private int dbqn_notice_no;						
	private String dbqn_dir_name;
	private boolean dbqn_is_valid;
	private boolean dbqn_img_deleted;
	private String dbqn_request_time;
	
}
