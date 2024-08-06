// 이메일 중복 검사
function usedEmailCheck(email) {
    return $.ajax({
        url: '/account/is_account',
        type: 'GET',
        data: { 
			email: email 
		},
    })
    .then(function(response) {
        return response; 
    })
    .catch(function(xhr, status, error) {
        console.error('Error during email check:', error);
        return false;
    });
}