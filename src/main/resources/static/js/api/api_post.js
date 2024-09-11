// 회원 가입 폼
async function postSignUpForm(event, formName) {
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
	if(!checkEmpty(input, '생년월일을', true)) {
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
function postSignInForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_id;
	if(!checkEmpty(input, '이메일을', true, true)) { // 요소, 텍스트, alert 여부, 메세지 요소 미노출 여부
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를', true, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_in_confirm"; 
    form.method = "post"; 
    form.submit();
}

// 본인 확인 폼
async function postIdentityCheckForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_pw;
	if(!checkEmpty(input, '비밀번호를', true, true)) {
		input.focus();
		return false;
	}
	
	try {
		const response = await $.ajax({
			url: '/account/info/modify_check',
			method: 'POST',
			data: {
				a_pw: input.value.trim(),
			},
		});
		
		logger.info('/account/info/modify_check postIdentityCheckForm() response:', response);
				
		if(response) {
			sessionStorage.setItem('loginedId', response.loginedId);
			sessionStorage.setItem('checkDate', response.checkDate);
			await getAccountInfo(); // get_account_info 요청, account modify form set
			
		} else {
			alert('비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요.');
			return false;
		}
		
	} catch(error) {
		logger.error('/account/info/modify_check postIdentityCheckForm() error:', error);
		alert('본인 확인 오류로 데이터를 불러오는데 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
		location.replace('/account/info/modify_form');
	}
}

// post ajax 요청
async function postSubmitForm(apiUrl, formData, successMessage, errorMessage, redirectUrl = null) {
	for (const [key, value] of formData.entries()) {
		logger.info('postSubmitForm() formData:', key, value);
	};
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: formData,
			processData: false,  // FormData가 자동으로 Content-Type 설정
			contentType: false,  // FormData를 문자열로 변환하지 않음
		});
		
		logger.info(`${apiUrl} postSubmitForm() response:`, response);
		
		if(response) {
			alert(successMessage);
			redirectUrl ? location.replace(redirectUrl) : location.reload(true);
			
		} else {
			alert(errorMessage);
			location.reload(true);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} postSubmitForm() error:`, error);
		alert(errorMessage);
		location.reload(true);
	}
}

// 질환/질병 분류 등록 폼
async function postDiseaseCategoryCreateForm(event, formName, nextPage) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(!(await usedInputValueCheck(input, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	const formData = new FormData();
	formData.append('dc_name', input.value.trim());
	
	const successMessage = `"${input.value}" 분류가 등록되었습니다.`;
	const errorMessage = `"${input.value}" 분류 등록 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postSubmitForm(
		'/disease/cate_info/create_category_confirm', 				// apiUrl
		formData, 													// data
		successMessage, 											// 성공 메세지
		errorMessage,												// 실패 메세지 																
		nextPage ? '/disease/cate_info/category_list_form' : null	// 다음 페이지
	);
}

// 질환 / 질병 등록 폼
async function postDiseaseCreateForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(!(await usedInputValueCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
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
	
	const formData = new FormData(form);
	const successMessage = `"${form.d_name.value}" 질환/질병 정보가 등록되었습니다.`;
	const errorMessage = `"${form.d_name.value}" 질환 / 질병 정보 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postSubmitForm(
		'/disease/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/disease/info/disease_list_form'
	);
}

// 게시판 등록 폼
async function postBoardCategoryCreateForm(formName) {
	const form = document.forms[formName];
	const bc_name = form.bc_name;
	const bc_idx = form.bc_idx;
	
	if(!(await usedInputValueCheck(bc_name, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		bc_name.focus();
		return false;
	}
	
	const formData = new FormData();
	formData.append('bc_name', bc_name.value.trim());
	formData.append('bc_idx', setReplaceNumber(bc_idx)); // 문자열 제외 및 min, max 체크하여 입력값 설정
	
	const successMessage = `"${bc_name.value}" 게시판이 등록되었습니다.`;
	const errorMessage = `"${bc_name.value}" 게시판 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postSubmitForm(
		'/board/cate_info/create_category_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/board/cate_info/category_list_form'
	);
}

// 공지사항 등록 폼
async function postNoticeCreateForm(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	const successMessage = '공지사항이 등록되었습니다.';
	const errorMessage = '공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postSubmitForm(
		'/notice/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/notice/info/notice_list_form'
	);
}

// QNA 공지사항 등록 폼
async function postQnaNoticeCreateForm(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	const successMessage = '질문과 답변 공지사항이 등록되었습니다.';
	const errorMessage = '질문과 답변 공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postSubmitForm(
		'/qna/info/qna_notice_create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/qna/info/qna_list_form'
	);
}