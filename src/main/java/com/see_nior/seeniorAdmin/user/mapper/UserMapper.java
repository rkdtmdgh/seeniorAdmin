package com.see_nior.seeniorAdmin.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;
import com.see_nior.seeniorAdmin.dto.UserAccountDto;

@Mapper
public interface UserMapper {

	public List<AdminAccountDto> selectUserList(Map<String, Object> pagingParams);

	public int selectAllUserListCnt();

	public int selectSearchUserListCnt(Map<String, Object> searchParams);

	public List<AdminAccountDto> selectSearchUserList(Map<String, Object> pagingParams);

	public UserAccountDto selectUserAccountByNo(int u_no);

	public int updateUserAccountInfo(UserAccountDto userAccountDto);

	public int updateUserIsDeletedByNo(int u_no);

	public int updateUserIsBlockedByNo(Map<String, Object> params);

}
