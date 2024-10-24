package com.see_nior.seeniorAdmin.enums;

public enum PagePath {

	ACCOUNT_SIGN_UP_FORM("account/sign_up_form"),
	ACCOUNT_SIGN_UP_RESULT("account/sign_up_result"),
	ACCOUNT_SIGN_IN_FORM("account/sign_in_form"),
	ACCOUNT_SIGN_IN_RESULT("account/sign_in_result"),
	ACCOUNT_MODIFY_FORM("account/modify_form"),
	ACCOUNT_ADMIN_MODIFY_FORM("account/admin_modify_form"),
	ACCOUNT_ADMIN_LIST_FORM("account/admin_list_form"),
	ACCOUNT_ACCESS_DENIED_PAGE("account/access_denied_page"),
	
	USER_ACCOUNT_LIST_FORM("userAccount/user_account_list_form"),
	USER_MODIFY_FORM("userAccount/modify_form"),
	
	VIDEO_LIST_FORM("video/video_list_form"),
	VIDEO_CREATE_FORM("video/create_form"),
	VIDEO_MODIFY_FORM("video/modify_form"),
	
	QNA_LIST_FORM("qna/qna_list_form"),
	QNA_NOTICE_LIST_FORM("qna/notice_list_form"),
	QNA_NOTICE_CREATE_FORM("qna/notice_create_form"),
	QNA_ANSWER_FORM("qna/answer_form");
	
	
	private String value;
	
	PagePath(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
