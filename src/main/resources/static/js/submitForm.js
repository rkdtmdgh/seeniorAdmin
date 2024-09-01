// 회원 가입 폼
async function signUpForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.a_id;
	if(!(await validateEmail(input, true, true))) { // 제출 전 한번 더 중복 확인
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!validatePw(input, true)) { 
		input.focus();
		return false;
	}
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateBirth(input, true)) {
		input.focus();
		return false;
	}
	
	input = form.a_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_up_confirm"; 
    form.method = "post"; 
    form.submit()
}

// 로그인 폼
function signInForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_id;
	if(!checkEmpty(input, '이메일을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를', true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_in_confirm"; 
    form.method = "post"; 
    form.submit();
}

// 검색 폼
async function searchForm(event, apiUrl, page) {
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
		resetAllcheck(); // all_check 체크박스 초기화
		
		let intPage = page || 1;
		logger.info('searchForm() searchPart:', form.search_part.value);
		logger.info('searchForm() searchString:', input.value.trim());
				
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'GET',
				data: {
					searchPart: form.search_part.value, // 검색 분류 select
					searchString: input.value.trim(), // 검색 입력값
					page: intPage,
				},
			});
			
			logger.info(apiUrl + ' searchForm() response:', response);
			
			const { getListDtos, getListPage, getListCnt } = setParseResponseByCommand(apiUrl, response);
			const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
			const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
			$contentTable.html('');
			$pagination.html('');
			
			if(response && getListDtos) {
				// 쿼리스트링 조건 추가
				setSearchQueryString(getListPage.page, response.searchPart, response.searchString); // page, searchPart, searchString
							
				const paging = setPagination(getListPage, null, null, apiUrl, true); // 페이징벨류값, sort, sortValue, 커맨드, isSearch
				$pagination.html(paging);
				
				let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
				let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
				logger.info('getListCnt:', getListCnt);
				logger.info('pageLimit:', pageLimit);
				logger.info('getListPage.page:', getListPage.page);
				logger.info('listCnt:', listIndex);
											
				if(listIndex > 0) {
					getListDtos.forEach((data) => { 
						$contentTable[0].insertAdjacentHTML('beforeend', setDataList(apiUrl, data, listIndex)); // jQuery 선택자에서 DOM 요소가져오기[0]
						listIndex --;
					});
					
				} else {
		            // 테이블의 전체 열 수 계산하기
					const maxCols = setTableColumnsNum();
					$contentTable.html(`
						<tr>
	                        <td colspan="${maxCols}">
	                            <p class="table_info">검색된 내용이 없습니다.</p>
	                        </td>
	                    </tr>
					`);
				}
				
			} else {
				logger.info('데이터가 없거나 유효하지 않습니다.');
				// 테이블의 전체 열 수 계산하기
				const maxCols = setTableColumnsNum();
				$contentTable.html(`
					<tr>
                        <td colspan="${maxCols}">
                            <p class="table_info">검색된 내용이 없습니다.</p>
                        </td>
                    </tr>
				`);
			}
			
		} catch(error) {
			logger.error(apiUrl + ' searchForm() error:', error);
		}
	}
}

// 본인 확인 폼
function modifyCheckForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를', true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/modify_check";
    form.method = "post"; 
    form.submit();
}

// 본인 계정 정보 수정 폼
function modifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateBirth(input, true)) {
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(input.value.trim().length > 0) {
		const isConfirm = confirm('비밀번호를 변경하시겠습니까?\n변경하지 않을 경우 입력한 값을 삭제 후 다시 저장해 주세요.');
		if(!isConfirm) {
			return false;	
		}
		
		if(!validatePw(input, true)) { 
			input.focus();
			return false;
		}
	}
	
	input = form.a_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/modify_confirm";
    form.method = "post"; 
    form.submit();
}

// 관리자 계정 정보 수정(SUPER_ADMIN) 폼
function adminModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!checkEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateBirth(input, true)) {
		input.focus();
		return false;
	}
	
	input = form.a_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/account/admin_modify_confirm";
    form.method = "post"; 
    form.submit();
}

// 질환/질병 분류 등록 폼
async function diseaseCategoryRegForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(!checkEmpty(input, '분류명을', true)) { // 인풋요소, 메세지, alert 사용 여부
		input.focus();
		return false;
	}
	
	try {
		const response = await $.ajax({
			url: '/disease/create_category_confirm',
			method: 'POST',
			data: {
				dc_name: input.value.trim(),
			},
		});
		
		logger.info('/disease/create_category_confirm diseaseCategoryRegForm() response:', response);
		
		if(response) {
			switch(response) {
				case 1:
					alert('"' + input.value + '" 분류가 등록되었습니다');
					location.reload(true);
					break;
				
				case -1:
					alert('"' + input.value + '" 분류는 이미 등록되어 있습니다\n확인 후 다시 시도해 주세요.');
					input.focus();
					break;
				
				case 0:
					alert('분류 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
					location.reload(true);
					break;			
					
				default:
					logger.error('diseaseCategoryRegForm():', response);
					return;
			}
		}
		
	} catch(error) {
		logger.error('/disease/create_category_confirm diseaseCategoryRegForm() error:', error);
		alert('분류 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
		location.reload(true);
	}
}

// 질환 / 질병 등록
async function diseaseRegForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(!(await usedDiseaseCheck(input, null, true))) {
		input.focus();
		return false;
	}
	
	input = form.d_good_food;
	if(!checkEmpty(input, '추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
		
	input = form.d_bad_food;
	if(!checkEmpty(input, '비추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
	
	input = form.d_info;
	if(!checkEmpty(input, '질환 / 질병 정보를', true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출	
	form.action = "/disease/create_confirm";
    form.method = "post"; 
    form.submit();
}

// 질환 / 질병 수정
async function diseaseModifyForm(formName, d_nameDefaultValue) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(input.value.trim() !== d_nameDefaultValue) { // 수정이 되었을 경우
		const isCheck = await usedDiseaseCheck(input, null, true);
		if(!isCheck) {
			input.focus();
			return false;
		}
	}
	
	input = form.d_good_food;
	if(!checkEmpty(input, '추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
		
	input = form.d_bad_food;
	if(!checkEmpty(input, '비추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
	
	input = form.d_info;
	if(!checkEmpty(input, '질환 / 질병 정보를', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	
	try {
		const response = await $.ajax({
			url: '/disease/modify_confirm',
			method: 'POST',
			data: formData,
			processData: false,  // FormData가 자동으로 Content-Type 설정
			contentType: false,  // FormData를 문자열로 변환하지 않음
		});
		
		logger.info('/disease/modify_confirm diseaseModifyForm() response:', response);
		
		if(response) {
			alert('저장되었습니다');
			
		} else {
			alert('저장에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
		}
		
	} catch(error) {
		logger.error('/disease/modify_confirm diseaseModifyForm() error:', error);
		alert('저장에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
		
	} finally {
		location.reload(true);
	}
}