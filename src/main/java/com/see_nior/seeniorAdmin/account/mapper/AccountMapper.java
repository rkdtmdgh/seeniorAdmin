package com.see_nior.seeniorAdmin.account.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@Mapper
public interface AccountMapper {

	public int insertNewAdmin(AdminAccountDto adminAccountDto);

	public boolean isAccount(String a_id);
	
	public AdminAccountDto getAdminAccountById(String a_id);

	public void updateMyAdminInfo(AdminAccountDto adminAccountDto);

	public int updateAdminIsDeleted(String a_id);

	public ArrayList<AdminAccountDto> selectAdminList(Map<String, Object> pagingParams);

	public void updateAdminRoleByNo(int a_no);

	public int selectAllAccountListCnt();

	public List<AdminAccountDto> selectSearchAdminList(Map<String, Object> pagingParams);

	public int selectSearchAdminListCnt(Map<String, Object> searchParams);

	public void updateAdminInfoFromSuper(AdminAccountDto adminAccountDto);

}
