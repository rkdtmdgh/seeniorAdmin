// 이메일 중복 검사 
function usedEmailCheck(email) {
	logger.info('usedEmailChekc:', email);
	return $.ajax({
		url: '/account/is_account',
		method: 'GET',
		data: {
			a_id: email
		},
	})
	.then(response => {
		logger.info('Ajax usedEmailCheck result:', response);
		return response;
	})
	.catch(error => {
		logger.error('Error during email check:', error);
		throw error;
	});
}