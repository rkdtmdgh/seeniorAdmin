// 함수 디바운싱 적용 // 함수, key명
const deleteData = debounceAsync(deleteDataProcess, 'deleteDataProcess'); // 삭제 처리

// 삭제 처리
async function deleteDataProcess(deleteConfig, data, dataName, errorMessage) {
	logger.info('deleteData()', deleteConfig.apiUrl, data);
	
	if(setLoading(true, 'content_inner')) { // 로딩 추가 함수 실행이 성공하면 요청 진행
		try {
			const response = await $.ajax({
				url: deleteConfig.apiUrl,
				method: 'POST',
				data: data,
			});
			
			logger.info(`${deleteConfig.apiUrl} deleteData() response:`, response);
			
		if(response) {
				alert(`${dataName} 삭제되었습니다.`);
				location.replace(deleteConfig.replace);
				
			} else {
				alert(errorMessage);
				location.reload(true);
			}
			
		} catch(error) {
			logger.error(`${deleteConfig.apiUrl} error:`, error);
			alert(errorMessage);
			location.reload(true);
		}
	}
}

// 개별 삭제
async function delSingleData(dataName, key, noValue, additionalData = {}) { // 추가 인자가 필요할 경우 {} 객체로 additionalData위치에 인자 전달
	logger.info('delSingleData()', key, noValue, dataName);
	
	const isConfirm = confirm(`${dataName} 을(를) 삭제하시겠습니까?`);
	if(!isConfirm) return false;
	
	const data = { [key]: noValue };
	Object.assign(data, additionalData); // 추가 데이터가 필요 시 data에 추가
	
	const deleteConfig = mapDeleteObject(key); // 커맨드와 경로 설정
	const errorMessage = `${dataName} 삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	const delName = dataName + ' 이(가)';
		
	deleteData(deleteConfig, data, delName, errorMessage);
}

// 리스트 삭제
async function delListData(key, isCheckList) {
	let deleteArray = []; // 삭제할 데이터의 배열이 들어갈 변수
	
	if(isCheckList) { // 삭제 처리 페이지가 체크리스트인 경우
		const $delDataElement = $(`input[type="checkbox"][name="${key}"]:checked`);
		
		if(!$delDataElement.length) {
			alert('항목을 선택해 주세요.');
			return false;
		}
		
		if(!confirm('선택한 항목을 삭제하시겠습니까?')) return false;
		deleteArray = Array.from($delDataElement, checkbox => $(checkbox).val());
		
	} else { // 리스트중 디테일 페이지에서 단일 데이터 삭제 시
		const $delData = $(`input[name="${key}"]`);
		if(!confirm('삭제하시겠습니까?')) return false;
		deleteArray = [$delData.val()];
	}
	
	logger.info(`delListData() ${key}s:`, deleteArray);
	
	const data = { [`${key}s`]: deleteArray };
	const deleteConfig = mapDeleteObject(key); // 커맨드와 경로 설정
	const errorMessage = '삭제에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	deleteData(deleteConfig, data, '항목이', errorMessage);
}

// 삭제 요청에 필요한 객체 설정
function mapDeleteObject(value) { 
	let apiUrl = null;
	let replace = null;
	
	switch(value) {
		case 'a_no': // 관리자 계정
			apiUrl = '/account/list/delete_confirm';
			replace = '/account/list/admin_list_form';
			break;
			
		case 'u_no': // 회원 계정
			apiUrl = '/user_account/info/delete_confirm';
			replace = '/user_account/list/user_account_list_form';
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
			apiUrl = '/board/noti_info/delete_board_notice_confirm';
			replace = '/board/noti_info/board_notice_list_form';
			break;
		
		case 'bp_no': // 게시물
			apiUrl = '/board/info/delete_confirm';
			replace = '/board/info/posts_list_form';
			break;
			
		case 'ac_no': // 광고 분류
			apiUrl = '/advertisement/cate_info/delete_category_confirm';
			replace = '/advertisement/cate_info/category_list_form';
			break;
			
		case 'ad_no': // 광고
			apiUrl = '/advertisement/info/delete_confirm';
			replace = '/advertisement/info/advertisement_list_form';
			break;
			
		default:
			logger.error('mapDeleteObject() value:', value);
			return false;
	}
	
	return { apiUrl, replace };
}