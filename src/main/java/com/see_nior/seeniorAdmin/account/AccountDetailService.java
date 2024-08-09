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
		
		if (adminAccountDto == null) 
			throw new AccountException(String.format("%s는 잘못된 아이디입니다.", username));
		else if (!adminAccountDto.is_deleted()) 
			throw new AccountException(String.format("%s는 탈퇴한 아이디입니다.", username));
		
		return new AccountDetails(adminAccountDto);
		
	}
	
}
