function signUpForm(formName) {
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.id;
	if(!validateEmail(input)) {
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
	form.action = "/sing_up_confirm"; // 서버에서 폼을 처리할 URL 설정
    form.method = "post"; // HTTP 메서드 설정
    return true;
}