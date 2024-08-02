package com.see_nior.seeniorAdmin.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.see_nior.seeniorAdmin.account.mapper.AccountMapper;
import com.see_nior.seeniorAdmin.dto.AdminAccountDto;

@SpringBootTest
class AccountServiceTest {
	
	@Mock
	private AccountMapper accountMapper;
	
	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private AccountService accountService;
	
	private AdminAccountDto adminAccountDto;
	
	@BeforeEach
	public void setUp() {
		
		adminAccountDto = new AdminAccountDto();
		
		adminAccountDto.setId("super_admin");
		adminAccountDto.setPw(passwordEncoder.encode("1234"));
		adminAccountDto.setName("super");
		adminAccountDto.setPhone("010-1234-1234");
		adminAccountDto.setDepartment("super");
		adminAccountDto.setLevel("super");
		adminAccountDto.setPosition("super");
		adminAccountDto.setAuthority_role("SUPER_ADMIN");
		
	}

	@Test
	void testSignUpConfirm() {
		
		when(accountMapper.isAccount("super_admin")).thenReturn(false);
		
		int result = accountService.signUpConfirm(adminAccountDto);
		
		assertEquals(AccountService.ADMIN_SIGN_UP_SUCCESS, result);
		
	}

	@Test
	void testGetAdminAccountById() {
		fail("Not yet implemented");
	}

	@Test
	void testModifyConfirm() {
		fail("Not yet implemented");
	}

	@Test
	void testDeleteConfirm() {
		fail("Not yet implemented");
	}

	@Test
	void testGetAdminList() {
		fail("Not yet implemented");
	}

	@Test
	void testIsApproval() {
		fail("Not yet implemented");
	}

}
