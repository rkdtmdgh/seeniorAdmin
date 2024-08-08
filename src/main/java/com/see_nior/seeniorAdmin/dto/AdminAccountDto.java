package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminAccountDto {

	private int no; 
	private String id;
	private String pw;
	private String name;
	private String phone;
	private String department;
	private String level;
	private String position;
	private String authority_role;
	private boolean isaccountnonexpired;
	private boolean isaccountnonlocked;
	private boolean iscredentialsnonexpired;
	private boolean isenabled;
	private boolean is_deleted;
	private String reg_date;
	private String mod_date;
	
}
