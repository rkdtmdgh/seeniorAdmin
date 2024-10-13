package com.see_nior.seeniorAdmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementCategoryDto {
	
	private int ac_no;
	private String ac_name;
	private int ac_is_deleted;
	private int ac_item_cnt;
	private String ac_reg_date;
	private String ac_mod_date;

}
