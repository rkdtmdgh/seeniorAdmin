// 계정 삭제
async function delAccount(a_no, a_id) {
	logger.info('deleteAccount()', a_no, a_id);
	
	const isConfirm = confirm(`${a_id} 계정을 삭제하시겠습니까?`);
	if(!isConfirm) {
		return false;	
	}
	
	const errorMessage = `${a_id} 계정 삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	try {
		const response = await $.ajax({
			url: '/account/list/delete_confirm',
			method: 'POST',
			data: {
				a_no: a_no,
			},
		});
		
		logger.info('/account/list/delete_confirm delAccount() response:', response);
		
		if(response) {
			alert(`${a_id} 계정이 삭제되었습니다.`);
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error('/account/list/delete_confirm delAccount() error:', error);
		alert(errorMessage);
		
	} finally {
		location.replace('/account/list/admin_list_form');
	}
}

// 체크 리스트 삭제
async function delCheckBoxList(apiUrl, checkName) {
	const $checkBoxs = $(`input[type="checkbox"][name="${checkName}"]:checked`);
	
	if($checkBoxs.length === 0) {
		alert('항목을 선택해 주세요.');
		return false;
	} 
	
	const isConfirm = confirm('선택한 항목을 삭제하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	
	const deleteData = Array.from($checkBoxs, checkbox => $(checkbox).val());
	logger.info('deleteCheckBoxList() deleteData:', deleteData);
	
	const errorMessage = '삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: {
				deleteData: deleteData, // 체크된 항목 배열
			},
		});
		
		logger.info(apiUrl + ' deleteCheckBoxList() response:', response);
		
		if(response) {
			alert('삭제되었습니다.');
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} error:`, error);
		alert(errorMessage);
		
	} finally {
		location.reload(true);
	}
}