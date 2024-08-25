// 회원 가입 폼
export async function postSignUpForm(event, formName) {
	if(event) event.preventDefault();
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
	
	input = form.a_birth;
	if(!validateBirth(input)) {
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
	if(event) event.preventDefault();
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
	if(event) event.preventDefault();
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
		logger.info('searchForm() searchPart:', form.search_part.value);
		logger.info('searchForm() searchString:', input.value.trim());
				
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
			logger.info(apiUrl + ' searchForm() response:', response);
			
			const contentTable = document.querySelector('.content_table tbody'); // 데이터가 나열될 테이블 요소
			const pagination = document.querySelector('.pagination_wrap'); // 페이지 네이션 요소
			contentTable.innerHTML = '';
			pagination.innerHTML = '';
			
			const { getListDtos, getListPage } = setParseResponseByCommand(apiUrl, response);
			if(response && getListDtos) {
				// 쿼리스트링 조건 추가
				setSearchQueryString(getListPage.page, response.searchPart, response.searchString); // page, searchPart, searchString
							
				const paging = setPagination(getListPage, null, null, apiUrl, true); // 페이징벨류값, sort, sortValue, 커맨드, isSearch
				pagination.innerHTML = paging;
				
				let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
				let totalCnt = getListPage.searchAdminListCnt; // 총 리스트 합계
				let adminListCnt = totalCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
											
				if(adminListCnt > 0) {
					getListDtos.forEach((data) => { 
						contentTable.insertAdjacentHTML('beforeend', setDataList(apiUrl, data, adminListCnt));
						adminListCnt --;
					});
				} else {
		            // 테이블의 전체 열 수 계산하기
					const maxCols = setTableColumnsNum();
					contentTable.innerHTML = `
						<tr>
	                        <td colspan="${maxCols}">
	                            <p class="table_info">검색된 내용이 없습니다.</p>
	                        </td>
	                    </tr>
					`;
				}
				
			} else {
				logger.info('데이터가 없거나 유효하지 않습니다.');
				// 테이블의 전체 열 수 계산하기
				const maxCols = setTableColumnsNum();
				contentTable.innerHTML = `
					<tr>
                        <td colspan="${maxCols}">
                            <p class="table_info">검색된 내용이 없습니다.</p>
                        </td>
                    </tr>
				`;
			}
		})
		.catch((error) => {
			logger.error(apiUrl + ' searchForm() error:', error);
		});
	}
}

// 본인 확인
export function modifyCheckForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를')) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/modify_check";
    form.method = "post"; 
    form.submit();
}

// 관리자 계정 정보 수정(SUPER_ADMIN)
export function adminModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을')) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateBirth(input)) {
		input.focus();
		return false;
	}
	
	input = form.a_phone;
	if(!validatePhone(input)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/admin_modify_confirm";
    form.method = "post"; 
    form.submit();
}

// 본인 계정 정보 수정
export function modifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을')) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateBirth(input)) {
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(input.value.trim().length > 0) {
		const isConfirm = confirm('비밀번호를 변경하시겠습니까?\n변경하지 않을 경우 입력한 값을 삭제 후 다시 저장해 주세요.');
		if(!isConfirm) {
			return false;	
		}
		
		if(!validatePw(input)) { 
			input.focus();
			return false;
		}
	}
	
	input = form.a_phone;
	if(!validatePhone(input)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/modify_confirm";
    form.method = "post"; 
    form.submit();
}