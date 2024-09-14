package com.see_nior.seeniorAdmin.account.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@Mapper
public interface AccountMapper {

	public int insertNewAdmin(AdminAccountDto adminAccountDto);

	public boolean isAccount(String a_id);
	
	public AdminAccountDto selectAdminAccountById(String a_id);
	
	public AdminAccountDto selectAdminAccountByNo(int a_no);

	public int updateMyAdminInfo(AdminAccountDto adminAccountDto);

	public int updateAdminIsDeletedByNo(int a_no);

	public List<AdminAccountDto> selectAdminList(Map<String, Object> pagingParams);

	public void updateAdminRoleByNo(int a_no);

	public int selectAllAccountListCnt();

	public List<AdminAccountDto> selectSearchAdminList(Map<String, Object> pagingParams);

	public int selectSearchAdminListCnt(Map<String, Object> searchParams);

	public int updateAdminInfoFromSuper(AdminAccountDto adminAccountDto);

	public int updateAdminPwReset(AdminAccountDto adminAccountDto);


}
