package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseCategoryDto {

	private int dc_no;
	private String dc_name;
	private boolean dc_is_deleted;
	private String dc_reg_date;
	private String dc_mod_date;
	
//	@JsonIgnore				
	// 직렬화나 역직렬화시 이 필드는 무시하게 설정하였음. 필요할때만 사용할 수 있는 필드
//	private String dc_name;
	
}