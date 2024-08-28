// 이메일 중복 검사 
function usedEmailCheck(email) {
	logger.info('usedEmailChekc():', email);
	return $.ajax({
		url: '/account/is_account',
		method: 'GET',
		data: {
			a_id: email
		},
	})
	.then(response => {
		logger.info('usedEmailCheck() result:', response);
		return response;
	})
	.catch(error => {
		logger.error('Error during email check:', error);
		throw error;
	});
}

// 질환/질병명 중복 검사(정규표현식 검사가 필요없으므로 즉시 에러 메세지 노출)
function usedDiseaseCheck(input, dc_no, d_name, alertMsg) {
	logger.info('usedDiseaseCheck() dc_no:', dc_no);
	logger.info('usedDiseaseCheck() d_name:', d_name);
	
	$.ajax({
		url: '/disease/is_disease',
		method: 'GET',
		data: {
			dc_no: dc_no,
			d_name: d_name,
		},
	})
	.then(response => {
		logger.info('usedDiseaseCheck() result:', response);
		if(!response) { // true=중복, false=정상 반환
			clearErrorMessage(input);
		} else {
			if(alertMsg) {
				alert("이미 등록된 질환/질병명입니다.");
			}
			addErrorMessage(input, "이미 등록된 질환/질병명입니다.");
			return false;
		}
	})
	.catch(error => {
		logger.error('Error during disease check:', error);
		if(alertMsg) {
			alert("질환/질병명 중복 확인 중 오류가 발생했습니다.");
		}
		addErrorMessage(input, "질환/질병명 중복 확인 중 오류가 발생했습니다.");
		return false;
	});
}