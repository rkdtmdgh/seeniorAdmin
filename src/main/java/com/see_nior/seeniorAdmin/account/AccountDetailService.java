package com.see_nior.seeniorAdmin.account;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
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
		
		if (adminAccountDto.getAuthority_role().equals("NOT_APPROVED")) {
			throw new RuntimeException(String.format("%s은(는) 승인 대기 중입니다.", username));
			
		} else if(adminAccountDto.is_deleted() == false){
			throw new RuntimeException(String.format("%s은(는) 탈퇴한 ID 입니다.", username));
			
		} else {
			
			return User
					.builder()
					.username(adminAccountDto.getId())
					.password(adminAccountDto.getPw())
					.roles(adminAccountDto.getAuthority_role())
					.build();
			
		}
		
	}
	
}
