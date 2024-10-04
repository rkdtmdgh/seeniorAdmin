// put ajax 요청
async function putSubmitForm(apiUrl, formData, successMessage, errorMessage) {
	for (const [key, value] of formData.entries()) {
		logger.info('putSubmitForm() formData:', key, value);
	};
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: formData,
			processData: false,  // FormData가 자동으로 Content-Type 설정
			contentType: false,  // FormData를 문자열로 변환하지 않음
		});
		
		logger.info(`${apiUrl} putSubmitForm() response:`, response);
		
		if(response) {
			if(successMessage) alert(successMessage);
			
		} else {
			if(errorMessage) alert(errorMessage);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} putSubmitForm() error:`, error);
		if(errorMessage) alert(errorMessage);
		
	} finally {
		location.reload(true);
	}
}

// 본인 계정 정보 수정 폼
async function putModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!validateEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateEmpty(input, '생년월일을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(input.value.trim().length) {
		const isConfirm = confirm('비밀번호를 변경하시겠습니까?\n변경하지 않을 경우 입력한 값을 삭제 후 다시 저장해 주세요.');
		if(!isConfirm) return false;	
		
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
	
	const formData = new FormData(); // 비동기로 추가된 html로 폼 요소들이 DOM에 제대로 반영되지 않을 수 있어 append로 삽입
	formData.append('a_no', form.a_no.value);
	formData.append('a_id', form.a_id.value);
	formData.append('a_pw', form.a_pw.value);
	formData.append('a_name', form.a_name.value);
	formData.append('a_birth', form.a_birth.value);
	formData.append('a_phone', form.a_phone.value);
	formData.append('a_department', form.a_department.value);
	formData.append('a_level', form.a_level.value);
	formData.append('a_position', form.a_position.value);
	
	const successMessage = '정보가 수정되었습니다';
	const errorMessage = '정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	logger.info('내 정보 관리 폼 데이터:', formData);
	await putSubmitForm(
		'/account/info/modify_confirm', // apiUrl
		formData, 						// data
		successMessage, 				// 성공 메세지
		errorMessage					// 실패 메세지
	);
}

// 관리자 계정 정보 수정(SUPER_ADMIN) 폼
async function putAdminModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.a_name;
	if(!validateEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateEmpty(input, '생년월일을', true)) {
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

	await putSubmitForm(
		'/account/list/admin_modify_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 관리자 계정 비밀번호 초기화
async function putResetPassword(a_no, a_id) {
	logger.info('putResetPassword():', a_no, a_id);
	
	const isConfirm = confirm(`${a_id} 계정 비밀번호를 초기화하시겠습니까?`);
	if(!isConfirm) return false;	
		
	const formData = new FormData();
	formData.append('a_no', a_no);
	
	const successMessage = `"${a_id}" 비밀번호가 초기화되었습니다.`;
	const errorMessage = `"${a_id}" 비밀번호 초기화에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await putSubmitForm(
		'/account/list/reset_password', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 질환 / 질병 수정
async function putDiseaseModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(input.value !== form.current_d_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			input.focus();
			return false;
		}
	}
	
	input = form.d_good_food;
	if(!validateEmpty(input, '추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
		
	input = form.d_bad_food;
	if(!validateEmpty(input, '비추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
	
	input = form.d_info;
	if(!validateEmpty(input, '질환 / 질병 정보를', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${form.d_name.value}" 질환/질병 정보가 수정되었습니다`;
	const errorMessage = `"${form.d_name.value}" 질환/질병 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm(
		'/disease/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 영상 정보 수정
async function putVideoModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.v_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
	
	input = form.v_link;
	if(!validateEmpty(input, 'URL 주소를', true)) {
		input.focus();
		return false;
	}
	
	input = form.v_text;
	if(!validateEmpty(input, '내용을', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${form.v_title.value}" 영상 정보가 수정되었습니다`;
	const errorMessage = `"${form.v_title.value}" 영상 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm(
		'/video/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 질환 / 질병 분류 수정
async function putDiseaseCategoryModifyForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(input.value !== form.current_dc_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
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

	await putSubmitForm(
		'/disease/cate_info/modify_category_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 게시판 수정
async function putBoardCategoryModifyForm(formName) {
	const form = document.forms[formName];
	const bc_name = form.bc_name;
	const bc_idx = form.bc_idx;
	const current_bc_name = form.current_bc_name;
	const current_bc_idx = form.current_bc_idx;
	
	if(bc_name.value === current_bc_name.value && bc_idx === current_bc_idx.value) {
		alert('수정된 내용이 없습니다');
		return false;		
	}
	
	if(bc_name.value !== current_bc_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(bc_name, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			bc_name.focus();
			return false;
		}
	} 
	
	const formData = new FormData(form);
	const successMessage = `"${bc_name.value}" 이(가) 수정되었습니다`;
	const errorMessage = `"${bc_name.value}" 이(가) 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putSubmitForm(
		'/board/cate_info/modify_category_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}

// 게시판 순번 수정
async function putBoardCategoryModifyButton(event, bc_idx) {
    const infoEle = event.target.closest('.table_info'); // 클릭된 요소의 부모 요소 찾기
    const bc_no = infoEle.getAttribute('data-bc-no'); 
    const bc_name = infoEle.getAttribute('data-bc-name'); 
    const current_bc_idx = infoEle.getAttribute('data-bc-idx'); 
	
	const formData = new FormData();
	formData.append('bc_no', bc_no);
	formData.append('bc_name', bc_name);
	formData.append('current_bc_idx', current_bc_idx);
	formData.append('bc_idx', bc_idx);

	await putSubmitForm(
		'/board/cate_info/modify_category_confirm', 
		formData
	);
}

// 공지사항 수정
async function putNoticeModifyForm(formName) {
	const form = document.forms[formName];
	let input;
		
	input = form.n_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
		
	input = form.n_body;
	if(!validateEmpty(input, '내용을', true)) {
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = '수정되었습니다';
	const errorMessage = '수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';

	await putSubmitForm(
		'/notice/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage
	);
}