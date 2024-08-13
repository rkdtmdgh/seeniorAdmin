package com.see_nior.seeniorAdmin.dto;

import lombok.Data;

@Data
public class UserAccountDto {

	private int u_no;
	private String u_id;
	private String u_pw;
	private String u_name;
	private String u_phone;
	private String u_nickname;
	private String u_gender;
	private String u_birth;
	private String u_address;
	private String u_profile_img;
	private String u_company;
	private boolean u_is_personal;					// true = 개인, false = 기관
	private String u_social_id;
	private boolean u_is_blocked;					// 계정 정지 여부. 		true = 정지 X, false = 정지
	private boolean u_isaccountnonexpired;			// 계정 만료 유무. 		true = 만료 X, false = 만료
	private boolean u_isaccountnonlocked;			// 계정 잠김 유무. 		true = 잠김 X, false = 만료
	private boolean u_iscredentialsnonexpired;		// 계정 자격 증명 유무. true = 만료 X, false = 만료
	private boolean u_isenabled;					// 계정 사용 가능 유무. true = 사용 가능, false = 사용 X
	private boolean u_is_deleted;					// 계정 삭제 유무. 		true = 삭제 X, false = 삭제
	private String u_reg_date;
	private String u_mod_date;
	
}
