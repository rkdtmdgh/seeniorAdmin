// 로그아웃
function signOut() {
	location.replace('/account/sign_out_confirm');
}

// 날짜 포맷팅
function formatDate(dateString) { // yyyy-mm-dd 형식
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
}