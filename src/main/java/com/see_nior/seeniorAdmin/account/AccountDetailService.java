package com.see_nior.seeniorAdmin.account;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.config.AccountException;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AccountDetailService implements UserDetailsService {

	final private AccountMapper accountMapper;
	
	public AccountDetailService(AccountMapper accountMapper) {
		this.accountMapper = accountMapper;
		
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("loadUserByUsername()");
		
		AdminAccountDto adminAccountDto = accountMapper.getAdminAccountById(username);
		
		// 계정이 없는 경우 
		if (adminAccountDto == null) 
			throw new AccountException(String.format("아이디 또는 비밀번호가 잘못되었습니다."));
		
		// 탈퇴한 ID인 경우 
		else if (!adminAccountDto.is_deleted()) 
			throw new AccountException(String.format("아이디 또는 비밀번호가 잘못되었습니다."));
		
		return new AccountDetails(adminAccountDto);
		
	}
	
}
