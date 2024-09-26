// input focus
function setInputFocus(ele) {
	const $input = $(`input[name="${ele}"]`);
	if ($input.length) {
		$input.focus();
		
	} else {
		logger.info(`No input found with name: ${ele}`);
	}
}

// loading set
function setAddLoading(loading) {
	const $loadingElement = $('<span class="loading_wrap"></span>');
	if(loading) {
		$('.content_info_wrap').append($loadingElement); // 해당 컨텐츠에 로딩 요소로 변경
		
	} else {
		$('.loading_wrap').remove();
	}
}

// login user info input value set
async function setLoginUserInfoInputValue(name, key) {
	const $input = $(`input[name="${name}"]`);
	
	if($input.length > 0) {
		const loginUserInfo = await getAccountInfo();
		$input.val(loginUserInfo[key]);
		
	} else {
		logger.error(`No input found with name: ${name}`);
	}
}

// submit 이벤트 막기(form 에서 enter키 입력 되지 않고 버튼으로 submit만 가능하도록)
// 예외 사항으로 form 내부에 disabled 되어 있는 인풋이 있는 경우에는 사용하지 않아도 됨
function setFormSendFalse(event) {
	event.preventDefault(); // 폼의 기본 제출 동작 방지
    return false; // 폼 제출 방지
}

// 콘텐츠 타이틀 설정
async function setContentTitle(infoNo) {
	if(infoNo) {
		const info = await getBoardInfo(infoNo);
		
		if(info) {
			const $title = $('.categoty_title');
			$title.text(info.boardCategoryDto.bc_name);
		}
		
	} else {
		logger.error('Not found infoNo');
	}
}

// 콘텐츠 서브 내용 설정
async function setContentSubInfo(txt) {
	const $title = $('.categoty_title');
	let $subInfo = $('.title_other_info_text');
	
	if($subInfo.length === 0) {
		$subInfo = $('<span class="title_other_info_text">');
	}
	
	$subInfo.text(`마지막 업데이트 ${txt}`);		
	$title.append($subInfo);	
}

// 레시피 레이아웃 설정
function setRecipeContentInfo(recipeDto) {
	logger.info('setRecipeContentInfo():', recipeDto);
	
 	if(recipeDto) {
		for (let i = 1; i <= 20; i++) {
	        let imgField = recipeDto['manual_img' + (i < 10 ? '0' + i : i)];
	        let manualField = recipeDto['manual' + (i < 10 ? '0' + i : i)];

		    if (imgField || manualField) { // 둘 중 하나라도 존재할 경우
		        const $manualInfoDiv = $('<div class="manual_info">');
	            const $imgTag = $(`<img src="${imgField}" alt="조리방법" class="sub_thumbnail">`);
	            const $pTag = $(`<p class="manual_info_text">${manualField}</p>`);
	            $manualInfoDiv.append($imgTag);
	            $manualInfoDiv.append($pTag);
		
		        $('.recipe_content').append($manualInfoDiv); // 필요한 위치에 삽입
		    }
		}
	}   
}

// 본인 확인 페이지 세션스토리지 저장 값 확인하여 요청 처리
function setSessionIdentityCheck(loginUser) {
	const sessionLogId = sessionStorage.getItem('loginedId') || '';
	const sessionCheckDate = sessionStorage.getItem('checkDate') || '';
	
	if(!sessionLogId || !sessionCheckDate) {
		return false;
	}
	
	if(loginUser != sessionLogId) {
		return false;
	}
	
	// 현재 시간과 비교
	const currentTime = new Date();
	const checkTime = new Date(sessionCheckDate);
	const sessionTime = 10 * 60 * 1000; // 10분
	
	if(currentTime - checkTime > sessionTime) { // sessionTime 경과
		sessionStorage.removeItem('loginedId');
		sessionStorage.removeItem('checkDate');
		alert('세션이 만료되었습니다. 다시 확인해 주세요.');
		return false;
		
	}
	
	return true;
}

// all_check 체크박스 초기화
function setAllcheck() {
	const $allCheckBox = $('input[type="checkbox"][name="all_check"]');
	if($allCheckBox && $allCheckBox.prop('checked')) $allCheckBox.prop('checked', false);
}

// 체크박스 일괄 체크 및 해제
function setAllCheckBox(ele) {	
	const isChecked = ele.checked;
	const $checkboxList = $(`input[name="${ele.value}"]`);
	$checkboxList.prop('checked', isChecked);
}

// 비밀번호 노출 설정
function setPwViewToggle(ele) {
	const $icon = $(ele).find('.icon');
	const $parentEle = $(ele).parent();
	const $passwordInput = $parentEle.find('input');
	
	if($passwordInput.length) {
		if($passwordInput.attr('type') === "password") {
			$passwordInput.attr('type', 'text');
			$icon.attr('src', '/image/icons/eye_open.svg');
			
		} else {
			$passwordInput.attr('type', 'password');
			$icon.attr('src', '/image/icons/eye_off.svg');
		}
	}
}

// 에러 메세지 요소 생성
function setCreateErrorElement($ele) { // jQeury 객체를 받음을 명시
    const $parentEle = $ele.parent();
    let $errorEle = $parentEle.find('.input_error'); // 부모 요소 내에 에러 요소를 찾음
    
    // 에러 메세지 요소가 없으면 생성
    if (!$errorEle.length) {
        $errorEle = $('<span>').addClass('input_error'); // 요소 생성 및 클래스 추가
        $parentEle.append($errorEle); // 부모 요소에 추가
    }

    return $errorEle;
}

// 에러 메세지 노출
function setAddErrorMessage(input, message) {
	const $input = $(input); // DOM 요소를 jQuery 객체로 변환
	const $errorEle = setCreateErrorElement($input);
	$errorEle.text(message);
	$input.parent().addClass('error');
}

// 에러 메세시 제거
function setClearErrorMessage(input) {
	const $input = $(input); // DOM 요소를 jQuery 객체로 변환
	const $errorEle = setCreateErrorElement($input);
	$errorEle.remove();
	$input.parent().removeClass('error');
}

// 날짜 포맷팅
function setFormatDate(dateString) { // yyyy-mm-dd 형식
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
}

// 검색 폼 데이터인지, 정렬된 데이터인지 확인하여 초기화
function setFormValuesFromUrl() {
	const urlParams = new URLSearchParams(window.location.search);
    const $sForm = $('form[name="search_form"]');
	const searchPart = urlParams.get('searchPart') || undefined;
    const searchString = urlParams.get('searchString') || '';
	const sortType = urlParams.get('sortType') || undefined;
	const sortValue = urlParams.get('sortValue') || undefined;
	const order = urlParams.get('order') || undefined;
    const page = urlParams.get('page') || 1;
	
	// 검색어가 있을 경우 검색 폼 사용으로 새로고침 시 재적용
	if(sortType === '2') { // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
		$sForm.find('select[name="search_part"]').val(searchPart)
		$sForm.find('input[name="search_string"]').val(searchString);
	}
	
	return { sortType, sortValue, order, page };
}

// 페이지 유지를 위한 쿼리 스트링 제어(검색 이력 제거)
function setListQueryString(sortValue, order, page) {
	const url = new URL(window.location); // 현재 url
	const infoNo = url.searchParams.get('infoNo') || undefined; // cateNor가 있을 경우 값 가지고 있기
	const sortType = url.searchParams.get('sortType') || undefined; // sortType이 있을 경우 값 가지고 있기
    url.search = ''; // 파라미터 비우기
	
	// 파라미터 추가
	if(infoNo) url.searchParams.set('infoNo', infoNo); 
    if (sortValue) {
		if(sortType) url.searchParams.set('sortType', sortType); 
		url.searchParams.set('sortValue', sortValue); 
		url.searchParams.set('order', order); 
		
    } else {
		// sort 버튼 기본값으로 초기화
		$('.sort').attr('data-current-sort-value', 'all');
	}
	
	url.searchParams.set('page', page);
	
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 검색 후 페이지 유지를 위한 쿼리 스트링 제어(검색 파트, 스트링 재입력)
function setSearchQueryString(page, searchPart, searchString) {
	const url = new URL(window.location);
	const infoNo = url.searchParams.get('infoNo') || undefined; // cateNor가 있을 경우 값 가지고 있기
    url.search = '';
    if(infoNo) url.searchParams.set('infoNo', infoNo); 
	url.searchParams.set('sortType', 2); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
    url.searchParams.set('searchPart', searchPart);
    url.searchParams.set('searchString', searchString);
	url.searchParams.set('page', page); 
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 중복 확인 커맨드, 메세지 설정
function setWordAndCommand(value) {
	let word;
	let apiUrl;
	
	switch(value) {
		case 'dc_name':
			word = '질환/질병 분류명';
			apiUrl = '/disease/cate_info/is_disease_category';
			break;
			
		case 'd_name':
			word = '질환/질병명';
			apiUrl = '/disease/info/is_disease';
			break;
			
		case 'bc_name':
			word = '게시판명';
			apiUrl = '/board/cate_info/is_board_category';
			break;
			
		default:
			logger.error('setWordAndCommand() value:', value);
			return false;
	}
	
	return { word, apiUrl };
}

// 분류 리스트 요청 커맨드 설정
function setSelectCategoryCommand(value) {
	let apiUrl;
	let getListDtos;
	let dataNo;
	let dataName;
	
	switch(value) {
		case 'dc_name': // 질환/질병 관련 페이지
		case 'd_category_no':
			apiUrl = '/disease/cate_info/get_category_list';
			getListDtos = 'diseaseCategoryDto';
			dataNo = 'dc_no';
			dataName = 'dc_name';
			break;
			
		case 'rcp_pat2': // 식단 관련 페이지
			apiUrl = '/recipe/cate_info/get_category_list';
			getListDtos = 'recipeCategoryDto';
			dataNo = 'rcp_pat2';
			dataName = 'rcp_pat2';
			break;
		
		default:
			logger.error('setSelectCategoryCommand() value:', value);
			return false;
	}
	
	return { apiUrl, getListDtos, dataNo, dataName };
}

// 분류별 리스트 요청 커맨드 설정
function setSelectGetListCommand(value) {
	let apiUrl;
	
	switch(value) {
		case 'dc_no': // 질환/질병 정보 리스트 페이지 분류명별 필터
			apiUrl = '/disease/info/get_disease_list_by_category_with_page';
			break;
			
		case 'rcp_pat2': // 식단 정보 리스트 페이지 분류명별 필터
			apiUrl = '/recipe/info/get_recipe_list_by_category_with_page';
			break;
			
		default:
			logger.error('setSelectCommand() value:', value);
			return false;
	}
	
	return apiUrl;
}

// 정렬 리스트 요청 커맨드 설정
function setSortCommand(value) {
	let apiUrl;
	
	switch(value) {
		case 'a_authority_role': // 관리자 리스트 페이지 승인 정렬
			apiUrl = '/account/list/get_admin_list';
			break;
			
		case 'dc_name': // 질환/질병 분류 리스트 페이지 분류명 정렬
			apiUrl = '/disease/cate_info/get_category_list_with_page';
			break;
			
		case 'd_name': // 질환/질병 정보 리스트 페이지 질환/질병명 정렬
			apiUrl = '/disease/info/get_all_disease_list_with_page';
			break;
			
		case 'rcp_nm': // 식단 정보 리스트 페이지 메뉴명 정렬
			apiUrl = '/recipe/info/get_all_recipe_list_with_page';
			break;
			
		case 'bc_name': // 게시판 관리 페이지 게시판명 정렬
			apiUrl = '/board/cate_info/get_list';
			break;
			
		default:
			logger.error('setSortCommand() value:', value);
			return false;
	}
	
	return apiUrl;
}

// 삭제 커맨드, 메세지 설정
function setDelCommand(value) {
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
		
		case 'bc_no': // 게시판
			apiUrl = '/board/cate_info/delete_category_confirm';
			replace = '/board/cate_info/category_list_form';
			break;
			
		case 'n_no': // 공지사항
			apiUrl = '/notice/info/delete_confirm';
			replace = '/notice/info/notice_list_form';
			break;
			
		default:
			logger.error('setDelCommand() value:', value);
			return false;
	}
	
	return { apiUrl, replace };
}

// 카테고리에 맞도록 객체 선택 
function setParseResponseByCommand(apiUrl, response) {
	let getListDtos;
	let getListPage;
	let getListCnt;
	
	switch(apiUrl) {
		case '/account/list/get_admin_list': // 관리자 계정 관리
			getListDtos = response.adminAccountDtos;
			getListPage = response.adminListPage;
			getListCnt = response.adminListPage.accountListCnt;
			break;
			
		case '/account/list/search_admin_list': // 관리자 계정 관리 검색
			getListDtos = response.adminAccountDtos;
			getListPage = response.searchAdminListPage;
			getListCnt = response.searchAdminListPage.searchAdminListCnt;
			break;
			
		case '/disease/info/get_all_disease_list_with_page': // 질환 / 질병 정보 관리
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListPageNum;
			getListCnt = response.diseaseListPageNum.diseaseListCnt;
			break;
			
		case '/disease/info/search_disease_list': // 질환 / 질병 정보 관리 검색
			getListDtos = response.diseaseDtos;
			getListPage = response.searchDiseaseListPageNum;
			getListCnt = response.searchDiseaseListPageNum.searchDiseaseListCnt;
			break;	
			
		case '/disease/info/get_disease_list_by_category_with_page': // 질환 / 질병 정보 관리 분류별 데이터
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListByCategoryPageNum;
			getListCnt = response.diseaseListByCategoryPageNum.diseaseListCnt;
			break;	
			
		case '/disease/cate_info/get_category_list_with_page': // 질환 / 질병 정보 분류 관리
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.diseaseCategoryListPageNum;
			getListCnt = response.diseaseCategoryListPageNum.diseaseCategoryListCnt;
			break;
			
		case '/disease/cate_info/search_disease_category_list': // 질환 / 질병 정보 분류 검색
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.searchDiseaseCategoryListPageNum;
			getListCnt = response.searchDiseaseCategoryListPageNum.searchDiseaseCategoryListCnt;
			break;
			
		case '/recipe/info/get_all_recipe_list_with_page': // 식단 정보 관리
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListPageNum;
			getListCnt = response.recipeListPageNum.recipeListCnt;
			break;
			
		case '/recipe/info/search_recipe_list': // 식단 정보 관리 검색
			getListDtos = response.recipeDtos;
			getListPage = response.searchRecipeListPageNum;
			getListCnt = response.searchRecipeListPageNum.searchRecipeListCnt;
			break;	
			
		case '/recipe/info/get_recipe_list_by_category_with_page': // 식단 정보 관리 분류별 데이터
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListByCategoryPageNum;
			getListCnt = response.recipeListByCategoryPageNum.recipeListByCategoryCnt;
			break;
			
		case '/board/cate_info/get_list': // 게시판 관리
			getListDtos = response.boardCategoryDtos;
			getListPage = response.boardCategoryListPageNum;
			getListCnt = response.boardCategoryListPageNum.boardCategoryListCnt;
			break;
			
		case '/board/cate_info/search_board_list': // 게시판 관리 검색
			getListDtos = response.boardCategoryDtos;
			getListPage = response.searchBoardCategoryListPageNum;
			getListCnt = response.searchBoardCategoryListPageNum.searchBoardCategoryListCnt;
			break;
			
		case '/board/info/get_all_posts_list_with_page': // 게시물
			getListDtos = response.postsDtos;
			getListPage = response.postsListPageNum;
			getListCnt = response.postsListPageNum.postsListCnt;
			break;
			
		case '/board/info/search_posts_list': // 게시물 검색
			getListDtos = response.postsDtos;
			getListPage = response.searchPostsListPageNum;
			getListCnt = response.searchPostsListPageNum.searchPostsListCnt;
			break;	
			
		case '/notice/info/get_all_notice_list_with_page': // 공지 사항
			getListDtos = response.noticeDtos;
			getListPage = response.noticeListPageNum;
			getListCnt = response.noticeListPageNum.noticeListCnt;
			break;
			
		case '/notice/info/search_notice_list': // 공지 사항 검색
			getListDtos = response.noticeDtos;
			getListPage = response.searchNoticeListPageNum;
			getListCnt = response.searchNoticeListPageNum.searchNoticeListCnt;
			break;	
			
		case '/qna/info/get_all_qna_list_with_page': // 질문과 답변
			getListDtos = response.qnaDtos;
			getListPage = response.qnaListPageNum;
			getListCnt = response.qnaListPageNum.qnaListCnt;
			break;
			
		case '/qna/info/search_qna_list': // 질문과 답변 검색
			getListDtos = response.qnaDtos;
			getListPage = response.searchQnaListPageNum;
			getListCnt = response.searchQnaListPageNum.searchQnaListCnt;
			break;		
						
	}
		
	return { getListDtos, getListPage, getListCnt }
}

// 페이지네이션 생성
function setPagination(pagingValues, sortValue, order, apiUrl, isSearch) { // 페이징벨류값, sortValue, order, 커맨드, isSearch
	const blockLimit = pagingValues.blockLimit; // 한 블럭에 포함되는 페이지 수
	const startPage = pagingValues.startPage; // 현재 블럭의 시작 페이지
	const endPage = pagingValues.endPage; // 현재 블럭의 마지막 페이지
	const currentPage = pagingValues.page; // 현재 페이지
	const maxPage = pagingValues.maxPage; // 마지막 페이지
	const totalBlocks = Math.ceil(maxPage / blockLimit); // 전체 블록 수
	const currentBlock = Math.ceil(currentPage / blockLimit); // 현재 블록
	const handlerFunction = isSearch ? 'getSearchList' : 'getList';
	// 검색폼일 경우 event 값 null 적용 검색폼이 아닐 경우 getList 커맨드
	const params1 = isSearch ? null : `'${apiUrl}'`; 
	// 검색폼일 경우 커맨드 검색폼이 아닐 경우 sortValue, order 값 입력
	const isSortValue = sortValue || '';
	const isOrder = order || '';
	const params2 = isSearch ? `'${apiUrl}'` : `'${isSortValue}', '${isOrder}'`;
	let paging = '';
	
	if(totalBlocks > 1 && currentBlock > 1) { // 블럭이 1개 이상일 경우 2번째 블럭 부터 노출
		paging += `
			<div onclick="${handlerFunction}(${params1}, ${params2}, 1)" class="first func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
	             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${params1}, ${params2}, ${startPage - 1})" class="prev func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="15,0 5,10 15,20 "/>
	            </svg>
	        </div>
		`;
	}
	
	for(let i = startPage; i <= endPage; i++) {
		if(i === currentPage) {
			paging += `<div class="current">${i}</div>`;
		} else {
			paging += `<div class="num" onclick="${handlerFunction}(${params1}, ${params2}, ${i})">${i}</div>`;
		}
	}
	
	if(totalBlocks > 1 && currentBlock < totalBlocks) { // 마지막 전 블럭까지 노출
		paging += `
			<div onclick="${handlerFunction}(${params1}, ${params2}, ${endPage + 1})" class="next func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="15,0 5,10 15,20 "/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${params1}, ${params2}, ${maxPage})" class="last func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
	             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
	            </svg>
	        </div>
		`;
	}
				
	return paging;
}

// 콘텐츠 리스트 오브젝트 생성
function setDataList(apiUrl, data, index) {
	let innerContent = '';
	
	switch(apiUrl) {
		case '/account/list/get_admin_list':  // 관리자 계정 리스트 테이블
		case '/account/list/search_admin_list': // 관리자 계정 검색 리스트 테이블
			innerContent = `
				<tr>
		            <td>
		                <p class="table_info">${index}</p>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_id || 'N/A'}</a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_authority_role == 'SUB_ADMIN' ? '완료' : '대기'}</a>
		            </td>
		            <td>
		                <p class="table_info">${data.a_phone || 'N/A'}</p>
		            </td>
		            <td>
		                <p class="table_info">${data.a_name || 'N/A'}</p>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.a_reg_date || 'N/A')}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/disease/info/get_all_disease_list_with_page': // 질환/질병 정보 관리 리스트 테이블
		case '/disease/info/search_disease_list':            // 질환/질병 정보 관리 검색 리스트 테이블
		case '/disease/info/get_disease_list_by_category_with_page': // 질환/질병 정보 관리 분류선택 리스트 테이블
			innerContent = `
				<tr>
		            <td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="d_no" class="d_no" value="${data.d_no}"></div>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${data.diseaseCategoryDto.dc_name}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${data.d_name || 'N/A'}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.d_reg_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/disease/cate_info/get_category_list_with_page': // 질환/질병 분류 관리 리스트 테이블
		case '/disease/cate_info/search_disease_category_list': // 질환/질병 분류 관리 검색 리스트 테이블
			innerContent = `
				<tr>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info">${data.dc_name}</a>
		            </td>
		            <td>
		                <a href="/disease/info/disease_list_form?sortType=1&sortValue=dc_no&order=${data.dc_no}" class="table_info">${data.dc_item_cnt}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.dc_reg_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/recipe/info/get_all_recipe_list_with_page': // 식단 정보 관리 리스트 테이블
		case '/recipe/info/search_recipe_list':            // 식단 정보 관리 검색 리스트 테이블
		case '/recipe/info/get_recipe_list_by_category_with_page': // 식단 정보 관리 분류선택 리스트 테이블
			innerContent = `
				<tr>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_pat2}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_nm || 'N/A'}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_way2 || 'N/A'}</a>
		            </td>
		            <td class="ta_l">
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info info_data_list">
		                	${data.info_wgt ? `<span>중량(${data.info_wgt}g)</span>` : ''}
		                	${data.info_eng ? `<span>열량(${data.info_eng}kcal)</span>` : ''}
		                	${data.info_car ? `<span>탄수화물(${data.info_car}g)</span>` : ''}
		                	${data.info_pro ? `<span>단백질(${data.info_pro}g)</span>` : ''}
		                	${data.info_fat ? `<span>지방(${data.info_fat}g)</span>` : ''}
		                	${data.info_na ? `<span>나트륨(${data.info_na}mg)</span>` : ''}
		                </a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/cate_info/get_list': // 게시판 관리 리스트 테이블
		case '/board/cate_info/search_board_category_list': // 게시판 관리 검색 리스트 테이블
			innerContent = `
				<tr>
		            <td>
		                <a href="/board/cate_info/modify_category_form?bc_no=${data.bc_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/board/cate_info/modify_category_form?bc_no=${data.bc_no}" class="table_info">${data.bc_name}</a>
		            </td>
		            <td>
		                <a href="" class="table_info">${data.bc_item_cnt}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.bc_reg_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/info/get_all_posts_list_with_page': // 게시물 리스트 테이블
		case '/board/info/search_posts_list': // 게시물 검색 리스트 테이블
			innerContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="bp_no" class="d_no" value="${data.bp_no}"></div>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${data.bp_title}(댓글 수)</a>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${data.bp_view_cnt}(조회수)</a>
		            </td>
					<td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">
							${data.bp_report_state === 0 ? '처리완료' : data.bp_report_state === 1 ? '정상' : data.bp_report_state === 2 ? '처리중' : 'N/A'}
						</a>
		            </td>
					<td>
		                <a href="" class="table_info">${data.bp_writer_no} 회원 이름, href 회원 정보 페이지</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.bp_mod_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/notice/info/get_notice_list_with_page': // 공지 사항 리스트 테이블
		case '/notice/info/search_notice_category_list': // 공지 사항 검색 리스트 테이블
			innerContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="d_no" class="d_no" value="${data.n_no}"></div>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${data.n_title}(댓글 수)</a>
		            </td>
					<td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${data.n_view_cnt}(조회수)</a>
		            </td>
					<td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${data.n_writer_no} 작성자 아이디</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.n_mod_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/info/get_all_qna_list_with_page': // 질문과 답변 리스트 테이블
		case '/qna/info/search_qna_list': // 질문과 답변 검색 리스트 테이블
			innerContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="bq_no" class="bq_no" value="${data.bq_no}"></div>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${data.n_title}(댓글 수)</a>
		            </td>
					<td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${data.n_writer_no} 작성자 아이디</a>
		            </td>
					<td>
		                <a href="" class="table_info">${data.bq_user_no}(작성자 아이디)</a>
		            </td>
					<td>
		                <p class="table_info">${setFormatDate(data.bq_reg_date) || 'N/A'}</p>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.bq_mod_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
		
		default:
			innerContent = '';
	}
	
	return innerContent;
}

// 테이블의 전체 열 수 계산하기
function setTableColumnsNum() {
	const maxCols = $('table thead tr').find('th').length;
	return maxCols;
}

// 테이블 셀렉트 옵션 노출 토글 버튼
function setSelectOptionTopggle(event) {
	const $selectEle = $(event.currentTarget).find('.select_option_list'); // 클릭한 요소 내의 커스텀 셀렉트 요소 찾기
	const $allSelectEle = $('.select_option_list'); // 모든 커스텀 셀렉트 요소
	$allSelectEle.not($selectEle).removeClass('active');
	$selectEle.toggleClass('active');
}

// textarea 입력 시 자동으로 높이값 조정
function setTextareaAutoHeight(ele) {
	const $textarea = $(ele);
	$textarea.height('auto'); // 요소의 높이를 초기화 하고 시작

	const scrollHeight = $textarea[0].scrollHeight; // 자바스크립트로 이벤트가 일어난 DOM요소의 scrollHeight 원시 속성 값 가져오기
	const clientHeight = $textarea[0].clientHeight; // 현재 textarea의 높이 (padding 포함, 스크롤 바 제외)
	
    if (scrollHeight <= clientHeight) { // 스크롤 높이가 클라이언트 높이와 작서나 같다면 내용이 한 줄임
        return false; // 한 줄만 입력된 경우에는 높이를 auto로 유지하고 종료
    }
	
	const maxHeight = parseInt($textarea.css('max-height')); // css로 설정한 max-height 속성 값 가져오기
	const newHeight = Math.min(scrollHeight, maxHeight); // 스크롤 높이와 max-height 중 작은 값ㅇ르 높이로 설정	
	$textarea.height(newHeight + 'px');
}

// ","구분이 필요한 textarea에서 enter입력 시 자동으로 "," 추가
function setTextareaAddCommaBeforeEnter(ele, event) {
	if(event.key === 'Enter') { // enter 키를 누를 때 동작
		let currentValue = ele.value;
		
		if(currentValue.trim() !== "" && !currentValue.trim().endsWith(',')) { // 빈값이 아니면서 마지막 글자가 ','가 아닐 경우 ','추가
			ele.value = currentValue.trim() + ',\n';
		}	
		
		event.preventDefault();	// 기본 enter 동작 방지
	}
}

// 본인 확인 전 account/modify_form SET 
function setIdentityCheckForm() {
	const $contentInfoWrap = $('.content_info_wrap');
	
	let dataFormContent;
	dataFormContent = `
		<div class="content_top">
	        <div class="content_top_info">
	            <h2 class="title">본인확인</h2>
	        </div>
            <p class="sub_title">본인확인을 위해 비밀번호를 입력해 주세요</p>
	    </div>
	    
	    <div class="sign_form" id="password_check_form">
	    	<form name="modify_check_form" onsubmit="postIdentityCheckForm(event, 'modify_check_form')">
            	<div class="input_list_container">
	                <div class="input_list" id="input_list_info">					
	                    <label for="pw" class="border_input">
	                        <span class="input_title">비밀번호</span>
	                        <input type="password" name="a_pw" id="pw" placeholder="비밀번호" class="input_txt" autocomplete="off">
	                        <span class="input_icon" onclick="setPwViewToggle(this)">
	                        	<img src="/image/icons/eye_off.svg" alt="toggle password visibility" class="icon">
	                        </span>
	                    </label>
	                </div>
	                
                    <div class="btn_list col">
                        <button type="submit" class="btns">확인</button>
                    </div>
                </div>
            </form>
	    </div>
	`;
	
	$contentInfoWrap.html(dataFormContent); // account/modifyForm SET
}

// 본인 확인 후 account/modify_form SET
function setAccountModifyForm(data) {
	let dataFormContent;
	dataFormContent = `
		<div class="content_top">
	        <div class="content_top_info">
	            <h2 class="title">내 정보 관리</h2>
	        </div>
            <p class="sub_title">내 정보 수정</p>
	    </div>
							
		<form name="modify_form">					    	
		    <div class="table_wrap">
                <table class="content_edit_table">
                    <colgroup>
                        <col class="col_row_title">
                        <col>
                        <col class="col_row_title">
                        <col>
                    </colgroup>

                    <tbody>
                        <tr>
                        	<th><p class="table_title">아이디</p></th>
                        	<td class="disabled">
                        		<input type="hidden" name="a_no" value="${data.a_no}">
                        		<input type="text" name="a_id" id="id" class="table_info disabled"
                                	value="${data.a_id}" disabled>
                            </td>

                            <th><p class="table_title">비밀번호</p></th>
                        	<td>
                        		<label class="cont_info">
                        			<input type="password" name="a_pw" id="pw" class="table_info" placeholder="변경 시 입력" autocomplete="off">
                        			<span class="input_icon" onclick="setPwViewToggle(this)">
			                        	<img src="/image/icons/eye_off.svg" alt="toggle password visibility" class="icon">
			                        </span>
                        		</label>
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">이름</p></th>
                        	<td>
                                <input type="text" name="a_name" id="name" class="table_info" placeholder="이름"
                                	oninput="validateEmpty(this, '이름을')" onblur="validateEmpty(this, '이름을')"
                                	value="${data.a_name}">
                            </td>

                        	<th><p class="table_title">생년월일</p></th>
                        	<td>
                                <input type="date" name="a_birth" id="birth" max="9999-12-31" min="1900-01-01" class="table_info"
                                	onchange="validateEmpty(this, '생년월일을')" onblur="validateEmpty(this, '생년월일을')"
                                	value="${data.a_birth}">
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">연락처</p></th>
                        	<td>
                                <input type="text" name="a_phone" id="phone" maxlength="13" class="table_info" placeholder="연락처"
                                	onkeydown="replacePhone(this)" onkeyup="validatePhone(this)" onblur="validatePhone(this)"
                                	value="${data.a_phone}">
                            </td>
                        	<th><p class="table_title">부서</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_department" id="department" class="table_info" placeholder="부서"
                                	value="${data.a_department || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">직위</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_level" id="level" class="table_info" placeholder="직위"
                                	value="${data.a_level || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        	<th><p class="table_title">직책</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_position" id="position" class="table_info" placeholder="직책"
                                	value="${data.a_position || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="btn_list right">
                    <div class="btn_list">
                        <div onclick="putModifyForm('modify_form')" class="btns">수정</div>
                    </div>
                </div>
            </div>
        </form>
	`;
	
	return dataFormContent;
}

// 문서 클릭 이벤트
$(document).on('click', function(event) {
	// 커스텀 셀렉트 open, close 기능
	const $openSelectEle = $('.select_option_list.active'); // 열려 있는 셀렉트 옵션 요소
	const isTriggerClick = event.target.closest('.table_title.select'); // 클릭한 요소가 커스텀 셀렉트 버튼인지 확인
	if(!isTriggerClick) { // 클릭한 요소가 커스텀 셀렉트 버튼이 아닐 경우
		$openSelectEle.removeClass('active'); // 열려 있는 셀렉트 옵션 닫기
	}
});

// 문서가 준비된 후 실행
$(function() {
	// textarea 입력된 값으로 높이값 조절
	$('.table_textarea.small').each(function() {
		setTextareaAutoHeight(this);
	});
});