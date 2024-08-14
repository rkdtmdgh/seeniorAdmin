// 회원 가입 폼
export async function postSignUpForm(event, formName) {
	event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.a_id;
	if(!(await validateEmail(input, true))) { // 제출 전 한번 더 중복 확인
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!validatePw(input)) { 
		input.focus();
		return false;
	}
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을')) {
		input.focus();
		return false;
	}
	
	input = form.a_phone;
	if(!validatePhone(input)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_up_confirm"; 
    form.method = "post"; 
    form.submit()
}

// 로그인 폼
export function postSignInForm(event, formName) {
	event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_id;
	if(!checkEmpty(input, '이메일을')) {
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를')) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_in_confirm"; 
    form.method = "post"; 
    form.submit();
}

// 검색 폼
export function searchForm(event, apiUrl, page) {
	event ? event.preventDefault() : null;
	const form = document.forms['search_form'];
	let input;
	
	input = form.search_string;
	if(!checkEmpty(input, '검색어를', true)) { // true: alert으로 메세지 띄우기
		input.focus();
		return false;
	}
	
	if(input.value.trim().length < 2) {
		alert('검색어는 2자 이상 입력해 주세요.');
		input.focus();
		return false;
	}
	
	if(apiUrl) {		
		let intPage = page || 1;
		
		$.ajax({
			url: apiUrl,
			method: 'GET',
			data: {
				searchPart: form.search_part.value, // 검색 분류 select
				searchString: input.value.trim(), // 검색 입력값
				page: intPage,
			},
		})
		.then(response => {
			console.log(apiUrl + ' searchForm() response:', response);
			
			// url 검색 조건 추가
			const url = new URL(window.location);
			url.searchParams.set('searchPart', response.searchPart); // 검색 파트
			url.searchParams.set('searchString', response.searchString); // 검색어
			url.searchParams.set('page', response.searchAdminListPage.page); // 페이지
			window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
			
			const contentTable = document.querySelector('.content_table tbody');
			contentTable.innerHTML = '';
			
			if(response && response.adminAccountDtos) {
				let adminListCnt = response.searchAdminListPage.searchAdminListCnt;
				if(adminListCnt > 0) {
					response.adminAccountDtos.forEach((data) => { 
						let innerContent = `
							<tr>
		                        <td>
		                            <p class="table_info">${adminListCnt}</p>
		                        </td>
		                        <td>
		                            <a href="" class="table_info">${data.a_id}</a>
		                        </td>
		                        <td>
		                            <a href="" class="table_info">${data.a_authority_role}</a>
		                        </td>
		                        <td>
		                            <p class="table_info">${data.a_phone}</p>
		                        </td>
		                        <td>
		                            <p class="table_info">${data.a_name}</p>
		                        </td>
		                        <td>
		                            <p class="table_info">${formatDate(data.a_reg_date)}</p>
		                        </td>
		                    </tr>
						`;
						contentTable.innerHTML += innerContent;
						adminListCnt --;
					});
				} else {
					contentTable.innerHTML = `
					<tr>
                        <td colspan="6">
                            <p class="table_info">검색된 내용이 없습니다.</p>
                        </td>
                    </tr>
					`;
				}
				
			} else {
				console.log('데이터가 없거나 유효하지 않습니다.');
			}
		})
		.catch((error) => {
			console.error('getAdminList() error:', error);
		});
	}
}