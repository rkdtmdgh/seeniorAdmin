package com.see_nior.seeniorAdmin.account.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@Mapper
public interface AccountMapper {

	public int insertNewAdmin(AdminAccountDto adminAccountDto);

	public boolean isAccount(String id);
	
	public AdminAccountDto getAdminAccountById(String id);

	public void updateMyAdminInfo(AdminAccountDto adminAccountDto);

	public int updateAdminIsDeleted(String id);

	public ArrayList<AdminAccountDto> selectAdminList(Map<String, Integer> pagingParams);

	public void updateAdminRoleByNo(int no);

	public int selectAllAccountListCnt();

	public List<AdminAccountDto> selectSearchAdminList(Map<String, Object> pagingParams);

	public int selectSearchAdminListCnt(String searchPart, String searchString);

	public int selectSearchAdminListCnt(Map<String, Object> searchParams);

	public void updateAdminInfoFromSuper(AdminAccountDto adminAccountDto);

}
