// 이메일 중복 검사(정규표현식 검사가 필요한 경우)
async function requestEmailDuplicateCheck(email) {
	logger.info('usedEmailChekc():', email);

	try {
		const response = await $.ajax({
			url: '/account/is_account',
			method: 'GET',
			data: {
				a_id: email
			},
		});
		
		logger.info('requestEmailDuplicateCheck() result:', response);
		return response;
		
	} catch (error) {
		logger.error('Error checking for duplicates:', error);
		throw error;
	}
}

// 정규표현식 검사가 필요없는 중복 검사(즉시 에러 메세지 노출)
async function requestDuplicateCheck(input, nullCheck, defaultValue,  alertMsg) { // 요소, 기본값, alert 여부, 빈값 체크 여부
	const inputName = input.name;
	const inputValue = input.value.trim();
	input.value = inputValue; // 앞뒤 공백 제거 적용
	
	let errorMessage;
	logger.info('requestDuplicateCheck() input:', inputName, inputValue);
	
	const { word, apiUrl } = mapDuplicateCheckObject(inputName);
	
	if(!inputValue.length) {
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

// 중복 확인에 필요한 객체 설정
function mapDuplicateCheckObject(value) {
	let word;
	let apiUrl;
	
	switch(value) {
		case 'dc_name':
			word = '질환/질병 분류명';
			apiUrl = '/disease/cate_info/is_disease_category';
			break;
			
		case 'd_name':
			word = '질환/질병명';
			apiUrl = '/disease/info/is_disease';
			break;
			
		case 'bc_name':
			word = '게시판명';
			apiUrl = '/board/cate_info/is_board_category';
			break;

		case 'ac_name':
			word = '광고 위치명';
			apiUrl = '/advertisement/cate_info/is_advertisement_category';
			break;
			
		default:
			logger.error('setWordAndCommand() value:', value);
			return false;
	}
	
	return { word, apiUrl };
}
