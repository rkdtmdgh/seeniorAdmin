package com.see_nior.seeniorAdmin.enums;

public enum PagePath {

	// Admin Account
	ACCOUNT_SIGN_UP_FORM("account/sign_up_form"),
	ACCOUNT_SIGN_UP_RESULT("account/sign_up_result"),
	ACCOUNT_SIGN_IN_FORM("account/sign_in_form"),
	ACCOUNT_SIGN_IN_RESULT("account/sign_in_result"),
	ACCOUNT_MODIFY_FORM("account/modify_form"),
	ACCOUNT_ADMIN_MODIFY_FORM("account/admin_modify_form"),
	ACCOUNT_ADMIN_LIST_FORM("account/admin_list_form"),
	ACCOUNT_ACCESS_DENIED_PAGE("account/access_denied_page"),
	
	// User Account
	USER_ACCOUNT_LIST_FORM("userAccount/user_account_list_form"),
	USER_MODIFY_FORM("userAccount/modify_form"),
	
	// Video
	VIDEO_LIST_FORM("video/video_list_form"),
	VIDEO_CREATE_FORM("video/create_form"),
	VIDEO_MODIFY_FORM("video/modify_form"),
	
	// QnA
	QNA_LIST_FORM("qna/qna_list_form"),
	QNA_CATEGORY_LIST_FORM("qna/category_list_form"),
	QNA_CREATE_CATEGORY_FORM("qna/create_category_form"),
	QNA_MODIFY_CATEGORY_FORM("qna/modify_category_form"),
	QNA_NOTICE_LIST_FORM("qna/notice_list_form"),
	QNA_CREATE_NOTICE_FORM("qna/create_notice_form"),
	QNA_MODIFY_NOTICE_FORM("qna/modify_notice_form"),
	QNA_ANSWER_FORM("qna/answer_form"),
	
	// Disease
	DISEASE_CREATE_CATEGORY_FORM("disease/create_category_form"),
	DISEASE_CATEGORY_LIST_FORM("disease/category_list_form"),
	DISEASE_MODIFY_CATEGORY_FORM("disease/modify_category_form"),
	DISEASE_CREATE_FORM("disease/create_form"),
	DISEASE_LIST_FORM("disease/disease_list_form"),
	DISEASE_MODIFY_FORM("disease/modify_form"),
	
	// Recipe
	RECIPE_LIST_FORM("recipe/recipe_list_form"),
	RECIPE_DETAIL_FORM("recipe/detail_form"),
	
	// Advertisement
	ADVERTISEMENT_CREATE_CATEGORY_FORM("advertisement/create_category_form"),
	ADVERTISEMENT_CATEGORY_LIST_FORM("advertisement/category_list_form"),
	ADVERTISEMENT_MODIFY_CATEGORY_FORM("advertisement/modify_category_form"),
	ADVERTISEMENT_CREATE_FORM("advertisement/create_form"),
	ADVERTISEMENT_LIST_FORM("advertisement/advertisement_list_form"),
	ADVERTISEMENT_MODIFY_FORM("advertisement/modify_form");
	
	
	private String value;
	
	PagePath(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
