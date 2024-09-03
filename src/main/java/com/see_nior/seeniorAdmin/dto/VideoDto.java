package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
	
	private int v_no;
	private int v_disease_no;
	private String v_title;
	private String v_text;
	private String v_link;
	private boolean v_is_deleted;
	private String v_reg_date;
	private String v_mod_date;

}
