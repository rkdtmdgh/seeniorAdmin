// 계정 삭제
function deleteAccount(a_no, a_id) {
	logger.info('deleteAccount()', a_no, a_id);
	const isConfirm = confirm(a_id + ' 계정을 삭제하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	location.replace('/account/delete_confirm?a_no=' + a_no);
}

// 체크 리스트 삭제
function deleteCheckBoxList(apiUrl, checkName) {
	const checkBoxs = document.querySelectorAll(`input[type="checkbox"][name="${checkName}"]:checked`);
	logger.info('deleteCheckBoxList():', checkBoxs);
	
	if(checkBoxs.length === 0) {
		alert('항목을 선택해 주세요.');
		return false;
	} 
	
	const deleteData = Array.from(checkBoxs).map(checkbox => checkbox.value);
	
	$.ajax({
		url: apiUrl,
		method: 'POST',
		data: {
			deleteData: deleteData, // 체크된 항목 배열
		},
	})
	.then(response => {
		logger.info(apiUrl + ' deleteCheckBoxList() response:', response);
		
		if(response && response.deleteResult) {
			location.reload(true);
		} else {
			alert('삭제에 실패하였습니다. 다시 시도해 주세요.');
			location.reload(true);
		}
	})
	.catch((error) => {
		alert('삭제에 실패하였습니다. 다시 시도해 주세요.');
		logger.error(apiUrl + ' error:', error);
	});
}