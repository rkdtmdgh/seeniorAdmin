// 개별 삭제
async function delSingleData(key, noValue, dataName) {
	logger.info('deleteAccount()', key, noValue, dataName);
	
	const isConfirm = confirm(`${dataName} 을(를) 삭제하시겠습니까?`);
	if(!isConfirm) return false;
	
	const { apiUrl, replace } = mapDeleteObject(key); // 커맨드와 경로 설정
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

// 리스트 삭제
async function delListData(name, isCheckList) {
	let deleteArray; // 삭제할 데이터의 배열이 들어갈 변수
	
	if(isCheckList) { // 삭제 처리 페이지가 체크리스트인 경우
		const $delDataElement = $(`input[type="checkbox"][name="${name}"]:checked`);
		
		if(!$delDataElement.length) {
			alert('항목을 선택해 주세요.');
			return false;
		}
		
		if(!confirm('선택한 항목을 삭제하시겠습니까?')) return false;
		
		deleteArray = Array.from($delDataElement, checkbox => $(checkbox).val());
		
	} else { // 리스트중 디테일 페이지에서 단일 데이터 삭제 시
		const $delData = $(`input[name="${name}"]`);
		
		if(!confirm('삭제하시겠습니까?')) return false;
		
		deleteArray = [$delData.val()];
	}
	
	logger.info(`delListData() ${name}s:`, deleteArray);
	
	const { apiUrl, replace } = mapDeleteObject(name); // 커맨드와 경로 설정
	const errorMessage = '삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: {
				[`${name}s`]: deleteArray, // 항목 배열
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

// 삭제 요청에 필요한 객체 설정
function mapDeleteObject(value) { 
	let apiUrl;
	let replace;
	
	switch(value) {
		case 'a_no': // 관리자 계정
			apiUrl = '/account/list/delete_confirm';
			replace = '/account/list/admin_list_form';
			break;
			
		case 'dc_no': // 질환/질병 분류
			apiUrl = '/disease/cate_info/delete_category_confirm';
			replace = '/disease/cate_info/category_list_form';
			break;
			
		case 'd_no': // 질환/질병
			apiUrl = '/disease/info/delete_confirm';
			replace = '/disease/info/disease_list_form';
			break;
		
		case 'v_no': // 영상 정보
			apiUrl = '/video/info/delete_confirm';
			replace = '/video/info/video_list_form';
			break;
			
		case 'n_no': // 공지사항
			apiUrl = '/notice/info/delete_confirm';
			replace = '/notice/info/notice_list_form';
			break;
		
		case 'bc_no': // 게시판
			apiUrl = '/board/cate_info/delete_category_confirm';
			replace = '/board/cate_info/category_list_form';
			break;
			
		case 'bn_no': // 게시판 공지 사항
			apiUrl = '/board/info/delete_board_notice_confirm';
			replace = '/board/info/notice_posts_form';
			break;
		
		case 'bp_no': // 게시물
			apiUrl = '/board/info/delete_confirm';
			replace = '/board/info/posts_list_form';
			break;
			
		default:
			logger.error('mapDeleteObject() value:', value);
			return false;
	}
	
	return { apiUrl, replace };
}