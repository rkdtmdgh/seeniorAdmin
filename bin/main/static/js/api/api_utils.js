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

// 질환 / 질병명 중복 검사
function usedDiseaseCheck(dc_no, d_name) {
	logger.info('usedDiseaseCheck() dc_no:', dc_no);
	logger.info('usedDiseaseCheck() d_name:', d_name);
	
	return $.ajax({
		url: '/disease/is_disease',
		method: 'GET',
		data: {
			dc_no: dc_no,
			d_name: d_name,
		},
	})
	.then(response => {
		logger.info('usedDiseaseCheck() result:', response);
		return response;
	})
	.catch(error => {
		logger.error('Error during disease check:', error);
		throw error;
	});
}