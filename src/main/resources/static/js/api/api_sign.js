// 이메일 중복 검사 
function usedEmailCheck(email) {
	// console.log('usedEmailChekc:', email);
	return $.ajax({
		headers: {
			'Contents-Type': 'Apllication/json',
		},
		url: '/account/is_account',
		method: 'GET',
		data: {
			a_id: email
		},
	})
	.then(response => {
		console.log('Ajax usedEmailCheck result:', response);
		return response;
	})
	.catch(error => {
		console.error('Error during email check:', error);
		throw error;
	});
}