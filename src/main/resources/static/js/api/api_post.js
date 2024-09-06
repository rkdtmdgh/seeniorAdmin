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
		//location.replace('/account/info/modify_form');
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
	
	const errorMessage = `"${input.value}" 분류 등록 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	try {
		const response = await $.ajax({
			url: '/disease/cate_info/create_category_confirm',
			method: 'POST',
			data: {
				dc_name: input.value.trim(),
			},
		});
		
		logger.info('/disease/cate_info/create_category_confirm diseaseCategoryRegForm() response:', response);
		
		if(response) {
			alert(`"${input.value}" 분류가 등록되었습니다.`);
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error('/disease/cate_info/create_category_confirm diseaseCategoryRegForm() error:', error);
		alert(errorMessage);
		
	} finally {
		nextPage ? location.replace(nextPage) : location.reload(true);
	}
}

// 질환 / 질병 등록
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
	const errorMessage = `"${form.d_name.value}" 질환 / 질병 정보 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	try {
		const response = await $.ajax({
			url: '/disease/info/create_confirm',
			method: 'POST',
			data: formData,
			processData: false,  // FormData가 자동으로 Content-Type 설정
			contentType: false,  // FormData를 문자열로 변환하지 않음
		});
		
		logger.info('/disease/info/create_confirm postDiseaseCreateForm() response:', response);
		
		if(response) {
			alert(`"${form.d_name.value}" 질환/질병 정보가 등록되었습니다.`);
			location.replace('/disease/info/disease_list_form');
			
		} else {
			alert(errorMessage);
			location.reload(true);
		}
		
	} catch(error) {
		logger.error('/disease/info/create_confirm postDiseaseCreateForm() error:', error);
		alert(errorMessage);
		location.reload(true);
	}
}

// 게시판 등록 
async function postBoardCategoryCreateForm(event, formName, nextPage) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	let bc_idx;
	
	input = form.bc_name;
	if(!(await usedInputValueCheck(input, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	bc_idx = form.bc_idx.value != "" ? form.bc_idx.value : 0; 
	
	const errorMessage = `"${input.value}" 게시판 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	try {
		const response = await $.ajax({
			url: '/board/cate_info/create_board_confirm',
			method: 'POST',
			data: {
				bc_name: form.bc_name.value.trim(),
				bc_idx: bc_idx,
			},
		});
		
		logger.info('/board/cate_info/create_board_confirm postBoardCategoryCreateForm() response:', response);
		
		if(response) {
			alert(`"${input.value}" 게시판이 등록되었습니다.`);
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error('/board/cate_info/create_board_confirm postBoardCategoryCreateForm() error:', error);
		alert(errorMessage);
		
	} finally {
		nextPage ? location.replace(nextPage) : location.reload(true);
	}
}