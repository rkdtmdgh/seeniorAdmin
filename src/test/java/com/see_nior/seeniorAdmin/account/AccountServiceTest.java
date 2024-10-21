package com.see_nior.seeniorAdmin.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AccountServiceTest {
	
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountService accountService;
	
	@Test
	void testGetAdminList() throws Exception {
	    // Given: AccountService가 반환할 Mock 데이터 설정
	    int page = 1;
	    Map<String, Object> mockAdminList = new HashMap<>();
	    mockAdminList.put("adminData", "someAdminData");
	    
	    Map<String, Object> mockAccountListPage = new HashMap<>();
	    mockAccountListPage.put("pageInfo", "somePageInfo");
	    
	    mockAdminList.put("accountListPage", mockAccountListPage);

	    // AccountService 메서드 동작을 Mock으로 정의

	    // When: MockMvc를 사용해 GET 요청을 보냄
	    mockMvc.perform(get("/get_admin_list")
	            .param("page", String.valueOf(page)))
	        .andExpect(status().isOk())  // HTTP 200 상태 코드 확인
	        .andExpect(jsonPath("$.adminData").value("someAdminData"))  // 반환된 JSON에서 adminData 확인
	        .andExpect(jsonPath("$.accountListPage.pageInfo").value("somePageInfo")) // 반환된 JSON에서 accountListPage 확인
	        .andDo(print()); // 요청과 응답을 콘솔에 출력 (선택 사항)
	}
	
	
	
}
