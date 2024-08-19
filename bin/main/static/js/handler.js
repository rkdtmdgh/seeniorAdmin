// input focus
function setInputFocus(ele) {
	const input = document.querySelector(`input[name="${ele}"]`);
	if (input) {
		logger.info('focus input:', input.name);
		input.focus();
	} else {
		logger.info(`No input found with name: ${ele}`);
	}
}

// 비밀번호 노출 설정
function setViewPw(ele) {
	const icon = ele.querySelector('.icon');
	const parentEle = ele.parentElement;
	const passwordInput = parentEle.querySelector('input');
	
	if(passwordInput && passwordInput.type === "password") {
		passwordInput.type = "text";
		icon.src = "/image/icons/eye_open.svg";
	} else if(passwordInput) {
		passwordInput.type = "password";
		icon.src = "/image/icons/eye_off.svg";
	}
}

// 휴대폰 번호 형식으로 변환
function setReplacePhone(input) {
	const phoneValue = input.value.replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, ""); // (01x-xxxx-xxxx)
	input.value = phoneValue;
	return phoneValue;
}

// 검색 폼 데이터인지 확인하여 초기화
function setFormValuesFromUrl(part) {
	const urlParams = new URLSearchParams(window.location.search);
    const sForm = document.forms['search_form'];
	const searchPart = urlParams.get('searchPart') || part;
    const searchString = urlParams.get('searchString') || '';
    const page = urlParams.get('page') || 1;
	const sort = urlParams.get('sort') || undefined;
	const sortValue = urlParams.get('sortValue') || undefined;
	
	const hasSearchString = searchString.trim().length > 0; // 검색어가 있는지 확인
	
	// 검색어가 있을 경우 검색 폼 사용으로 새로고침 시 재적용
	if(hasSearchString) {
		sForm.search_part.value = searchPart;
		sForm.search_string.value = searchString;
	}
	
	return { hasSearchString, page, sort, sortValue };
}

// 페이지 유지를 위한 쿼리 스트링 제어(검색 이력 제거)
function setQueryString(page, sortOrPart, sortValueOrString, isSearch) {
	const url = new URL(window.location);
	if(isSearch) {
        url.searchParams.delete('sort');
        url.searchParams.delete('sortValue');
        url.searchParams.set('searchPart', sortOrPart);
        url.searchParams.set('searchString', sortValueOrString);
    } else {
        url.searchParams.delete('searchPart');
        url.searchParams.delete('searchString');
        if (sortOrPart) url.searchParams.set('sort', sortOrPart);
        if (sortValueOrString) url.searchParams.set('sortValue', sortValueOrString);
    }
	url.searchParams.set('page', page); 
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 페이지네이션 생성
function setPagination(pagingValues, handlerFunction, sort, sortValueORsearchCommend, isSearch) {
	const blockLimit = pagingValues.blockLimit; // 한 블럭에 포함되는 페이지 수
	const startPage = pagingValues.startPage; // 현재 블럭의 시작 페이지
	const endPage = pagingValues.endPage; // 현재 블럭의 마지막 페이지
	const currentPage = pagingValues.page; // 현재 페이지
	const maxPage = pagingValues.maxPage; // 마지막 페이지
	const totalBlocks = Math.ceil(maxPage / blockLimit); // 전체 블록 수
	const currentBlock = Math.ceil(currentPage / blockLimit); // 현재 블록
	// 검색폼일 경우 event 값 null 적용 검색폼이 아닐 경우 sort 기본 값
	const sortStrORsearchEvent = isSearch ? null : sort ? `'${sort}'` : 'null'; 
	// 검색폼일 경우 api 커맨드 검색폼이 아닐 경우 sortValue 
	const sortValueStrORsearchCommend = isSearch ? `'${sortValueORsearchCommend}'` : sortValueORsearchCommend ? `'${sortValueORsearchCommend}'` : 'null'; 
	let paging = '';
	
	if(totalBlocks > 1 && currentBlock > 1) { // 블럭이 1개 이상일 경우 2번째 블럭 부터 노출
		paging += `
			<div onclick="${handlerFunction}(${sortStrORsearchEvent}, ${sortValueStrORsearchCommend}, 1)" class="first func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
	             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${sortStrORsearchEvent}, ${sortValueStrORsearchCommend}, ${startPage - 1})" class="prev func_icon">
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
			paging += `<div class="num" onclick="${handlerFunction}(${sortStrORsearchEvent}, ${sortValueStrORsearchCommend}, ${i})">${i}</div>`;
		}
	}
	
	if(totalBlocks > 1 && currentBlock < totalBlocks) { // 마지막 전 블럭까지 노출
		paging += `
			<div onclick="${handlerFunction}(${sortStrORsearchEvent}, ${sortValueStrORsearchCommend}, ${endPage + 1})" class="next func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="15,0 5,10 15,20 "/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${sortStrORsearchEvent}, ${sortValueStrORsearchCommend}, ${maxPage})" class="last func_icon">
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
		case 'getAdminList': 
		case 'search_admin_list':
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
		                <p class="table_info">${formatDate(data.a_reg_date || 'N/A')}</p>
		            </td>
		        </tr>
			`;
			break;
		
		default:
			innerContent = '';
	}
	
	return innerContent;
}

// 콘텐츠 리스트 정렬 버튼 세팅
function setToggleSort(event, defaultSort, changeSort) {
    const sortBtn = event.target; // 클릭된 버튼 요소
    const currentSort = sortBtn.getAttribute('data-sort'); // 현재 정렬 값 가져오기
    const newSort = currentSort === defaultSort ? changeSort : defaultSort; // 정렬 값 토글
    sortBtn.setAttribute('data-sort', newSort); // 버튼의 data-sort 속성 값 업데이트
    getList(defaultSort, newSort, 1); // 변경된 정렬 값으로 getList 호출
}