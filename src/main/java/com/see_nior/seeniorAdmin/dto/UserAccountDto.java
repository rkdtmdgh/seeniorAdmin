package com.see_nior.seeniorAdmin.dto;

import lombok.Data;

@Data
public class UserAccountDto {

	private int no;
	private String id;
	private String pw;
	private String name;
	private String phone;
	private String nickname;
	private String gender;
	private String birth;
	private String address;
	private String profile_img;
	private String company;
	private boolean is_personal;				// true = 개인, false = 기관
	private String social_id;
	private boolean is_blocked;					// 계정 정지 여부. 		true = 정지 X, false = 정지
	private boolean isaccountnonexpired;		// 계정 만료 유무. 		true = 만료 X, false = 만료
	private boolean isaccountnonlocked;			// 계정 잠김 유무. 		true = 잠김 X, false = 만료
	private boolean iscredentialsnonexpired;	// 계정 자격 증명 유무. true = 만료 X, false = 만료
	private boolean isenabled;					// 계정 사용 가능 유무. true = 사용 가능, false = 사용 X
	private boolean is_deleted;					// 계정 삭제 유무. 		true = 삭제 X, false = 삭제
	private String reg_date;
	private String mod_date;
	
}
