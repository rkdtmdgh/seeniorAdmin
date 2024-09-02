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
function postSignInForm(event, formName) {
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

// 본인 확인 폼
function postIdentityCheckForm(event, formName) {
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

// 질환/질병 분류 등록 폼
async function postDiseaseCategoryCreateForm(event, formName) {
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
async function postDiseaseCreateForm(formName) {
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