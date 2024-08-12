package com.see_nior.seeniorAdmin.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@Mapper
public interface UserMapper {

	public int selectUserListCnt();

	public List<AdminAccountDto> selectUserList(Map<String, Integer> pagingParams);

	public int selectSearchUserListCnt(Map<String, Object> searchParams);

	public List<AdminAccountDto> selectSearchUserList(Map<String, Object> pagingParams);

}
