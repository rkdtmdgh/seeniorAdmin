// 계정 삭제
async function delSingleData(key, noValue, dataName) {
	logger.info('deleteAccount()', key, noValue, dataName);
	
	const isConfirm = confirm(`${dataName} 을(를) 삭제하시겠습니까?`);
	if(!isConfirm) return false;
	
	const { apiUrl, replace } = setDelCommand(key); // 커맨드와 경로 설정
	const errorMessage = `${dataName} 삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: {
				[key]: noValue,
			},
		});
		
		logger.info(`${apiUrl} delSingleData() response:`, response);
		
		if(response) {
			alert(`${dataName} 이(가) 삭제되었습니다.`);
			location.replace(replace);
			
		} else {
			alert(errorMessage);
			location.reload(true);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} delSingleData() error:`, error);
		alert(errorMessage);
		location.reload(true);
	}
}

// 체크 리스트 삭제
async function delListData(name, isCheckList) {
	let deleteData; // 삭제할 데이터의 배열이 들어갈 변수
	
	if(isCheckList) { // 삭제 처리 페이지가 체크리스트인 경우
		const $delDataElement = $(`input[type="checkbox"][name="${name}"]:checked`);
		
		if($delDataElement.length === 0) {
			alert('항목을 선택해 주세요.');
			return false;
		}
		
		if(!confirm('선택한 항목을 삭제하시겠습니까?')) return false;
		
		deleteData = Array.from($delDataElement, checkbox => $(checkbox).val());
		
	} else { // 리스트중 디테일 페이지에서 단일 데이터 삭제 시
		const $delData = $(`input[name="${name}"]`);
		
		if(!confirm('삭제하시겠습니까?')) return false;
		
		deleteData = [$delData.val()];
	}
	
	logger.info('delListData() deleteData:', deleteData);
	
	const { apiUrl, replace } = setDelCommand(name); // 커맨드와 경로 설정
	const errorMessage = '삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: {
				deleteData: deleteData, // 항목 배열
			},
		});
		
		logger.info(apiUrl + ' delListData() response:', response);
		
		if(response) {
			alert('삭제되었습니다.');
			
		} else {
			alert(errorMessage);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} error:`, error);
		alert(errorMessage);
		
	} finally {
		isCheckList ? location.reload(true) : location.replace(replace);
	}
}