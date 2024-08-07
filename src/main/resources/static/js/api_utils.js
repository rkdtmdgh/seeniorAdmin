// 이메일 중복 검사 
function usedEmailCheck(email) {
	// console.log('usedEmailChekc:', email);
	
	// Promise 사용으로 명확하게 처리
	return new Promise((resolve, reject) => { // resolve=해결, reject=거부 여부
		$.ajax({
			dataType: 'json',
			url: '/account/is_account',
			type: 'GET',
			data: {
				id: email
			},
		})
		.done(response => {
			console.log('Ajax usedEmailCheck result:', response);
			resolve(response); // 성공 시 resolve 호출하여 Promise를 이행 상태로 변경
		})
		.fail((xhr, status, error) => {
			console.error('Error during email check:', error);
			// console.log('Response text:', xhr.responseText); // 응답 내용 확인
			reject(error); // 실패 시 reject 호출하여 Promise 거부 상태로 변경
		});
	});	
}