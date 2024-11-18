package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteNoticeDto {
	
	private int dn_no;
	private int dn_notice_no;						
	private String dn_dir_name;
	private boolean dn_is_valid;
	private boolean dn_img_deleted;
	private String dn_request_time;
	
}
