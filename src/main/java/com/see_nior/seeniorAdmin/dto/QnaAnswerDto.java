package com.see_nior.seeniorAdmin.dto;

import lombok.Data;

@Data
public class QnaAnswerDto {

	private int bqa_no;
	private String bqa_answer;
	private boolean bqa_is_deleted;
	private String bqa_reg_date;
	private String bqa_mod_date;
	
	private QnaDto qnaDto;
	
	private AdminAccountDto adminAccountDto;
	
}
