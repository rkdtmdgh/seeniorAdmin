package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaDto {

	private int bq_no;
	private String bq_title;
	private String bq_body;
	private int bq_view_cnt;
	private boolean bq_state;
	private boolean bq_is_deleted;
	private String bq_reg_date;
	private String bq_mod_date;
	
	private QnaCategoryDto qnaCategoryDto;
	
	private QnaAnswerDto qnaAnswerDto;
	
	private UserAccountDto userAccountDto;
	
}
