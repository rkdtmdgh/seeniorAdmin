package com.see_nior.seeniorAdmin.account;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SuppressWarnings("serial")
public class AccountDetails implements UserDetails {

	private AdminAccountDto  adminAccountDto;
	
	public AccountDetails(AdminAccountDto adminAccountDto) {
		this.adminAccountDto = adminAccountDto;
		
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		log.info("getAuthorities()");
		
		Collection<GrantedAuthority> collection = new ArrayList<>();
		
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_".concat(adminAccountDto.getA_authority_role());
			}
		});
		
		return collection;
	}

	@Override
	public String getPassword() {
		return adminAccountDto.getA_pw();
	}

	@Override
	public String getUsername() {
		return adminAccountDto.getA_id();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return adminAccountDto.isA_isaccountnonexpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return adminAccountDto.isA_isaccountnonlocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return adminAccountDto.isA_iscredentialsnonexpired();
	}

	@Override
	public boolean isEnabled() {
		return adminAccountDto.isA_isenabled();
	}
	
}
