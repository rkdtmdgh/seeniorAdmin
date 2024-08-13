package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountDto {

	private int a_no; 
	private String a_id;
	private String a_pw;
	private String a_name;
	private String a_phone;
	private String a_department;
	private String a_level;
	private String a_position;
	private String a_authority_role;
	private boolean a_isaccountnonexpired;
	private boolean a_isaccountnonlocked;
	private boolean a_iscredentialsnonexpired;
	private boolean a_isenabled;
	private boolean a_is_deleted;
	private String a_reg_date;
	private String a_mod_date;
	
}
