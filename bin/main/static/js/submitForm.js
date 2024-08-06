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
	form.action = "/sing_up_confirm";
    form.method = "post"; 
    return true;
}