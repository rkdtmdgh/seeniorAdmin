// ajax 요청
async function putSubmitForm(apiUrl, formData, successMessage, errorMessage) {
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
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} postSubmitForm() error:`, error);
		alert(errorMessage);
		
	} finally {
		location.reload(true);
	}
}

// 본인 계정 정보 수정 폼
async function putModifyForm(formName) {
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
	
	const formData = new FormData(form);
	const successMessage = '정보가 수정되었습니다';
	const errorMessage = '정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await putSubmitForm('/account/info/modify_confirm', formData, successMessage, errorMessage);
}

// 관리자 계정 정보 수정(SUPER_ADMIN) 폼
async function putAdminModifyForm(formName) {
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
	
	const formData = new FormData(form);
	const successMessage = `"${form.a_id.value}" 정보가 수정되었습니다`;
	const errorMessage = `"${form.a_id.value}" 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm('/account/list/admin_modify_confirm', formData, successMessage, errorMessage);
}

// 관리자 계정 비밀번호 초기화
async function putResetPassword(a_no, a_id) {
	logger.info('putResetPassword():', a_no, a_id);
	
	const isConfirm = confirm(`${a_id} 계정 비밀번호를 초기화하시겠습니까?`);
	if(!isConfirm) {
		return false;	
	}
		
	const formData = new FormData();
	formData.append('a_no', a_no);
	
	const successMessage = `"${a_id}" 비밀번호가 초기화되었습니다.`;
	const errorMessage = `"${a_id}" 비밀번호 초기화에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await putSubmitForm('/account/list/reset_password', formData, successMessage, errorMessage);
}

// 질환 / 질병 수정
async function putDiseaseModifyForm(formName, d_nameDefaultValue) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(input.value.trim() !== d_nameDefaultValue) { // 수정이 되었을 경우
		if(!(await usedInputValueCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
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
	const successMessage = `"${form.d_name.value}" 질환/질병 정보가 수정되었습니다`;
	const errorMessage = `"${form.d_name.value}" 질환/질병 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm('/disease/info/modify_confirm', formData, successMessage, errorMessage);
}

// 질환 / 질병 분류 수정
async function putDiseaseCategoryModifyForm(formName, dc_nameDefaultValue) {
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(input.value.trim() !== dc_nameDefaultValue) { // 수정이 되었을 경우
		if(!(await usedInputValueCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			input.focus();
			return false;
		}
		
	} else {
		alert('수정된 내용이 없습니다');
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${input.value}" 질환/질병 분류명이 수정되었습니다`;
	const errorMessage = `"${input.value}" 질환/질병 분류명 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm('/disease/cate_info/modify_category_confirm', formData, successMessage, errorMessage);
}