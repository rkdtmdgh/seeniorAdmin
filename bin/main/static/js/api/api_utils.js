// 이메일 중복 검사(정규표현식 검사도 필요한 경우)
async function usedEmailCheck(email) {
	logger.info('usedEmailChekc():', email);

	try {
		const response = await $.ajax({
			url: '/account/is_account',
			method: 'GET',
			data: {
				a_id: email
			},
		});
		
		logger.info('usedEmailCheck() result:', response);
		return response;
		
	} catch (error) {
		logger.error('Error checking for duplicates:', error);
		throw error;
	}
}

// 정규표현식 검사가 필요없는 중복 검사(즉시 에러 메세지 노출)
async function usedInputValueCheck(input, nullCheck, defaultValue,  alertMsg) { // 요소, 기본값, alert 여부, 빈값 체크 여부
	const inputName = input.name;
	const inputValue = input.value.trim();
	let errorMessage;
	logger.info('usedInputValueCheck() input:', inputName, inputValue);
	
	const { word, apiUrl } = setWordAndCommand(inputName);
	
	if(inputValue.length === 0) {
		if(!nullCheck) {
			setClearErrorMessage(input);
			
		} else {
			errorMessage = `${word}을(를) 입력해 주세요.`;
			setAddErrorMessage(input, errorMessage);
			
			if(alertMsg) alert(errorMessage);			
		}
		
		return false;
	}
	
	if(defaultValue && input.value.trim() === defaultValue) {
		setClearErrorMessage(input);
		return false;
	}
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'GET',
			data: {
				[inputName]: inputValue,
			},
		});
		
		logger.info('usedDiseaseCategoryCheck() result:', response);
		
		if(!response) { // true=중복, false=정상 반환
			setClearErrorMessage(input);
			return true;
			
		} else {
			errorMessage = `이미 등록된 ${word}입니다.`;
			if(alertMsg) {
				alert(errorMessage);
			}
			
			setAddErrorMessage(input, errorMessage);
			return false;
		}
		
	} catch(error) {
		logger.error('Error checking for duplicates:', error);
		alert(`${word} 중복 확인 중 오류가 발생했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`);
		location.reload(true);
	}
}
