// input focus
function setInputFocus(ele) {
	const $input = $(`input[name="${ele}"]`);
	if ($input.length) {
		logger.info('focus input:', $input.attr('name'));
		$input.focus();
		
	} else {
		logger.info(`No input found with name: ${ele}`);
	}
}

// 비밀번호 노출 설정
function setViewPw(ele) {
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

// 휴대폰 번호 형식으로 변환
function setReplacePhone(input) {
	const phoneValue = input.value.replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, ""); // (01x-xxxx-xxxx)
	input.value = phoneValue;
	return phoneValue;
}

// 숫자만 입력 가능하도록 변환 및 YYYY-MM-DD 형식으로 변환
function setReplaceBirth(input) {
    let birthValue = input.value.replace(/[^0-9]/g, ""); // 입력 값에서 숫자 이외의 모든 문자를 제거

    // 자동으로 하이픈 삽입: 5번째와 8번째 자리에 삽입
    if (birthValue.length > 4) {
        birthValue = birthValue.slice(0, 4) + '-' + birthValue.slice(4);
    }
    
    if (birthValue.length > 7) {
        birthValue = birthValue.slice(0, 7) + '-' + birthValue.slice(7);
    }

    input.value = birthValue;
    return birthValue;
}

// 날짜 포맷팅
function setFormatDate(dateString) { // yyyy-mm-dd 형식
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
}

// 체크박스 일괄 체크 및 해제
function setAllCheckBox(ele) {	
	const isChecked = ele.checked;
	const $checkboxList = $(`input[name="${ele.value}"]`);
	$checkboxList.prop('checked', isChecked);
}

// 검색 폼 데이터인지, 정렬된 데이터인지 확인하여 초기화
function setFormValuesFromUrl(part) {
	const urlParams = new URLSearchParams(window.location.search);
    const $sForm = $('form[name="search_form"]');
	const searchPart = urlParams.get('searchPart') || part;
    const searchString = urlParams.get('searchString') || '';
    const page = urlParams.get('page') || 1;
	const sortType = urlParams.get('sortType') || undefined;
	const sort = urlParams.get('sort') || undefined;
	const sortValue = urlParams.get(`${sort}`) || undefined;
	
	$sForm.find('select[name="search_part"]').val(searchPart);
	
	// 검색어가 있을 경우 검색 폼 사용으로 새로고침 시 재적용
	if(sortType === '2') { // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
		$sForm.find('input[name="search_string"]').val(searchString);
	}
	
	return { sortType, sort, sortValue, page };
}

// 페이지 유지를 위한 쿼리 스트링 제어(검색 이력 제거)
function setListQueryString(page, sort, sortValue) {
	const url = new URL(window.location); // 현재 url
	const sortType = url.searchParams.get('sortType') || undefined; // sortType이 있을 경우 값 가지고 있기
    url.search = ''; // 파라미터 비우기
	
	// 파라미터 추가
	url.searchParams.set('page', page); 
	 
    if (sort) {
		if(sortType) url.searchParams.set('sortType', sortType); 
		url.searchParams.set('sort', sort); 
		url.searchParams.set(`${sort}`, sortValue); 
		
    } else {
		// sort 버튼 기본값으로 초기화
		$('.sort').attr('data-current-sort', 'all');
	}
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 검색 후 페이지 유지를 위한 쿼리 스트링 제어(검색 파트, 스트링 재입력)
function setSearchQueryString(page, searchPart, searchString) {
	const url = new URL(window.location);
    url.search = '';
	url.searchParams.set('sortType', 2); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	url.searchParams.set('page', page); 
    url.searchParams.set('searchPart', searchPart);
    url.searchParams.set('searchString', searchString);
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 카테고리에 맞도록 객체 선택 
function setParseResponseByCommand(command, response) {
	let getListDtos;
	let getListPage;
	let getListCnt;
	
	switch(command) {
		case '/account/get_admin_list': // 관리자 계정 관리
			getListDtos = response.adminAccountDtos;
			getListPage = response.adminListPage;
			getListCnt = response.adminListPage.accountListCnt;
			break;
			
		case '/account/search_admin_list': // 관리자 계정 관리 검색
			getListDtos = response.adminAccountDtos;
			getListPage = response.searchAdminListPage;
			getListCnt = response.searchAdminListPage.searchAdminListCnt;
			break;
			
		case '/disease/get_all_disease_list_with_page': // 질환 / 질병 정보 관리
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListPageNum;
			getListCnt = response.diseaseListPageNum.diseaseListCnt;
			break;
			
		case '/disease/search_disease_list': // 질환 / 질병 정보 관리 검색
			getListDtos = response.diseaseDtos;
			getListPage = response.searchDiseaseListPageNum;
			getListCnt = response.searchDiseaseListPageNum.searchDiseaseListCnt;
			break;
		
		case '/disease/get_disease_list_by_category_with_page': // 질환 / 질병 정보 관리 질환명 sort
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListByCategoryPageNum;
			getListCnt = response.diseaseListByCategoryPageNum.diseaseListCnt;
			break;
	}
	
	return { getListDtos, getListPage, getListCnt }
}

// 페이지네이션 생성
function setPagination(pagingValues, sort, sortValue, command, isSearch) { // 페이징벨류값, 핸들러,  sort, sortValue, 커맨드, isSearch
	const blockLimit = pagingValues.blockLimit; // 한 블럭에 포함되는 페이지 수
	const startPage = pagingValues.startPage; // 현재 블럭의 시작 페이지
	const endPage = pagingValues.endPage; // 현재 블럭의 마지막 페이지
	const currentPage = pagingValues.page; // 현재 페이지
	const maxPage = pagingValues.maxPage; // 마지막 페이지
	const totalBlocks = Math.ceil(maxPage / blockLimit); // 전체 블록 수
	const currentBlock = Math.ceil(currentPage / blockLimit); // 현재 블록
	const handlerFunction = isSearch ? 'searchForm' : 'getList';
	// 검색폼일 경우 event 값 null 적용 검색폼이 아닐 경우 getList 커맨드
	const params1 = isSearch ? null : `'${command}'`; 
	// 검색폼일 경우 커맨드 검색폼이 아닐 경우 sort, sortValue 값 입력
	const isSort = sort ? sort : '';
	const isSortValue = sortValue ? sortValue : '';
	const params2 = isSearch ? `'${command}'` : `'${isSort}', '${isSortValue}'`;
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
function setDataList(api, data, index) {
	let innerContent = '';
	
	switch(api) {
		case '/account/get_admin_list': 
		case '/account/search_admin_list':
			innerContent = `
				<tr>
		            <td>
		                <p class="table_info">${index}</p>
		            </td>
		            <td>
		                <a href="/account/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_id || 'N/A'}</a>
		            </td>
		            <td>
		                <a href="/account/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_authority_role == 'SUB_ADMIN' ? '완료' : '대기'}</a>
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
			
		case '/disease/get_all_disease_list_with_page':
		case '/disease/search_disease_list':
		case '/disease/get_disease_list_by_category_with_page':
			innerContent = `
				<tr>
		            <td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="d_no" class="d_no" value="${data.d_no}"></div>
		            </td>
		            <td>
		                <a href="/disease/modify_form?d_no=${data.d_no}" class="table_info">${index}</a>
		            </td>
		            <td>
		                <a href="/disease/modify_form?d_no=${data.d_no}" class="table_info">${data.diseaseCategoryDto.dc_name}</a>
		            </td>
		            <td>
		                <a href="/disease/modify_form?d_no=${data.d_no}" class="table_info">${data.d_name || 'N/A'}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.d_reg_date) || 'N/A'}</p>
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

$(document).on('click', (event) => {
	// 커스텀 셀렉트
	const $openSelectEle = $('.select_option_list.active'); // 열려 있는 셀렉트 옵션 요소
	const isTriggerClick = event.target.closest('.table_title.select'); // 클릭한 요소가 커스텀 셀렉트 버튼인지 확인
	if(!isTriggerClick) { // 클릭한 요소가 커스텀 셀렉트 버튼이 아닐 경우
		$openSelectEle.removeClass('active'); // 열려 있는 셀렉트 옵션 닫기
	}
});