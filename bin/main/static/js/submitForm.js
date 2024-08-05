function signUpForm(formName) {
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.id;
	if(!validateId(input)) {
		input.focus();
		return false;
	}
	
	input = form.password;
	if(!isValidPw(input)) {
		input.focus();
		return false;
	}
	
	input = form.name;
	if(!isValidName(input)) {
		input.focus();
		return false;
	}
	
	input = form.tel;
	if(!isValidTel(input)) {
		input.focus();
		return false;
	}
	
	input = form.email;
	if(!isValidEmail(input)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	form.action = "/sign-up"; // 서버에서 폼을 처리할 URL 설정
    form.method = "post"; // HTTP 메서드 설정
    form.submit(); // 폼 제출
    return true; // 유효성 검사 통과 시 true 반환하여 폼 제출
}