// 회원 가입 폼
async function signUpForm(event, formName) {
	event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.id;
	if(!(await validateEmail(input, true))) { // 제출 전 한번 더 중복 확인
		input.focus();
		return false;
	}
	
	input = form.pw;
	if(!validatePw(input)) { 
		input.focus();
		return false;
	}
	
	input = form.name;
	if(!validateName(input)) {
		input.focus();
		return false;
	}
	
	input = form.phone;
	if(!validatePhone(input)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_up_confirm"; 
    form.method = "post"; 
    form.submit();
}

// 로그인 폼
function signInForm(event, formName) {
	event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.id;
	if(!input.value.trim().length > 0) { 
		alert('이메일을 입력해 주세요.');
		input.focus();
		return false;
	}
	
	input = form.pw;
	if(!input.value.trim().length > 0) { 
		alert('비밀번호를 입력해 주세요.');
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/account/sign_in_confirm"; 
    form.method = "post"; 
    form.submit();
}