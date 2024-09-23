// 콘텐츠 리스트 요청
async function getList(apiUrl, sortValue, order, page) {
	setAllcheck(); // all_check 체크박스 초기화
	
	// 검색 인풋 벨류 삭제
	const searchStringInput = document.forms['search_form'].search_string;
	if(searchStringInput.value.trim().length > 0) searchStringInput.value = ''; // 검색 이력이 남았을 경우에만 삭제
	
	const urlParams = new URLSearchParams(window.location.search);
	const infoNo = urlParams.get('infoNo') || undefined;
	
	const intPage = page || 1
	let params = `?page=${intPage}`;
	if(sortValue) params = `${params}&sortValue=${sortValue}&order=${order}`;
	if(infoNo) params = `${params}&infoNo=${infoNo}`;
	
	logger.info('apiUrl:', apiUrl + params);
	
	try {
		const response = await $.ajax({
			url: apiUrl + params,
			method: 'GET',
		});
		
		logger.info(`${apiUrl} getList() response:`, response);
		
		const { getListDtos, getListPage, getListCnt } = setParseResponseByCommand(apiUrl, response);
		const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
		$contentTable.html('');
		$pagination.html('');
		
		if(response && getListDtos) {
			// 쿼리스트링 조건 추가
			setListQueryString(sortValue, order, getListPage.page); // page, sortValue, order
			
			if(response.reg_date) setContentSubInfo(response.reg_date); // 타이틀 옆 서브내용 표시(예: 업데이트 날짜 등)
					
			const paging = setPagination(getListPage, sortValue, order, apiUrl, false); // 페이징벨류값, sortValue, order, 커맨드, isSearch
			$pagination.html(paging);
			
			let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
			let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
			
			if(listIndex > 0) {
				getListDtos.forEach((data) => { 
					$contentTable[0].insertAdjacentHTML('beforeend', setDataList(apiUrl, data, listIndex));
					listIndex --;
				});
				
			} else {
				const maxCols = setTableColumnsNum();
				$contentTable.html(`
					<tr>
	                    <td colspan="${maxCols}">
	                        <p class="table_info">목록이 없습니다.</p>
	                    </td>
	                </tr>
				`);
			}
			
		} else {
			logger.info('데이터가 없거나 유효하지 않습니다.');
			const maxCols = setTableColumnsNum();
			$contentTable.html(`
				<tr>
                    <td colspan="${maxCols}">
                        <p class="table_info">목록이 없습니다.</p>
                    </td>
                </tr>
			`);
		}
		
	} catch(error) {
		logger.error(apiUrl + ' error:', error);
	}
}

// 버튼으로 정렬된 리스트 요청
function getSortList(event, sortValue) {
    const sortBtn = event.currentTarget.closest('.sort'); // 클릭된 요소가 가장 가까운 부모 요소 중 클래스가 "sort"인 요소를 찾음
	if(!sortBtn) return; // 만약 sort 요소가 없다면 아무 작업도 하지 않음
	
    const apiUrl = setSortCommand(sortValue); // 커맨드 가져오기
    const currentSortValue = sortBtn.getAttribute('data-current-sort-value'); // 현재 정렬 값 가져오기 default all
    const order = currentSortValue === 'asc' ? 'desc' : 'asc'; // 정렬 값 토글
    sortBtn.setAttribute('data-current-sort-value', order); // 버튼의 data-sort-value 속성 값 업데이트
	
	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 0); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
    getList(apiUrl, sortValue, order, 1); // 변경된 정렬 값으로 getList 호출
}

// 셀렉트로 정렬된 리스트 요청
function getSelectList(event) {
	const sortBtn = event.target; // 클릭된 버튼 요소
	const sortValue = sortBtn.parentElement.getAttribute('data-sort-value'); // 정렬 종류 가져오기
	const order = sortBtn.getAttribute('data-order'); // 정렬할 값
	const apiUrl = setSelectGetListCommand(sortValue);
	
	logger.info('getSelectList() apiUrl:', apiUrl);
	logger.info('getSelectList() sortValue:', sortValue);
	logger.info('getSelectList() order:', order);

	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 1); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
	getList(apiUrl, sortValue, order, 1);
}

// 콘텐츠 정렬 셀렉트 옵션 리스트 요청
async function getOptionList(ele, isForm, selectedValue) {
	const $selectEle = $(`#${ele}`); // 셀렉트 요소가 생성될 table th
	const { apiUrl, getListDtos, dataNo, dataName } = setSelectCategoryCommand(ele);
	
	if($selectEle.length > 0) {
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'GET',
			});
			
			const categoryDto = response[getListDtos];
			logger.info(`${apiUrl} categoryDto:`, categoryDto);
			
			if(categoryDto && categoryDto.length > 0) {
				if(isForm) {
					categoryDto.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let selected = selectedValue ? data[dataNo] === selectedValue ? 'selected' : '' : '';
						let option = `<option value="${data[dataNo]}" ${selected}>${data[dataName]}</option>`;
						
						if(selected) {
							$selectEle[0].insertAdjacentHTML('afterbegin', option);
							
						} else {
							$selectEle[0].insertAdjacentHTML('beforeend', option);
						}
					});
					
				} else {
					const ceateSelect = `<ul data-sort-value="${dataNo}" class="select_option_list sc"></ul>`;
			        $selectEle[0].insertAdjacentHTML('beforeend', ceateSelect);
			        const $selectOptionlist = $('ul.select_option_list');
					
					categoryDto.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let option = `<li data-order="${data[dataNo]}" class="option" onclick="getSelectList(event);">${data[dataName]}</li>`;
						$selectOptionlist[0].insertAdjacentHTML('beforeend', option);
					});
				}
				
			} else {
				$selectEle.removeClass('select');
			}
			
		} catch(error) {
			logger.error(apiUrl + ' error:', error);
		}
	}
}

// 검색 리스트 요청
async function getSearchList(event, apiUrl, page) {
	if(event) event.preventDefault();
	const form = document.forms['search_form'];
	let input;
	
	input = form.search_string;
	if(!checkEmpty(input, '검색어를', true, true)) { // 요소, text, alert 여부, 에러메세지 미노출 여부
		input.focus();
		return false;
	}
	
	if(input.value.trim().length < 2) {
		alert('검색어는 2자 이상 입력해 주세요.');
		input.focus();
		return false;
	}
	
	if(apiUrl) {		
		setAllcheck(); // all_check 체크박스 초기화
		
		const urlParams = new URLSearchParams(window.location.search);
		const infoNo = urlParams.get('infoNo') || undefined;
	
		let intPage = page || 1;
		logger.info('searchForm() searchPart:', form.search_part.value);
		logger.info('searchForm() searchString:', input.value.trim());
		
		let params = `?searchPart=${form.search_part.value}&searchString=${input.value.trim()}&page=${intPage}`;
		if(infoNo) params = `${params}&infoNo=${infoNo}`;
				
		try {
			const response = await $.ajax({
				url: apiUrl + params,
				method: 'GET',
			});
			
			logger.info(`${apiUrl} searchForm() response:`, response);
			
			const { getListDtos, getListPage, getListCnt } = setParseResponseByCommand(apiUrl, response);
			const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
			const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
			$contentTable.html('');
			$pagination.html('');
			
			if(response && getListDtos) {
				// 쿼리스트링 조건 추가
				setSearchQueryString(getListPage.page, response.searchPart, response.searchString); // page, searchPart, searchString
							
				const paging = setPagination(getListPage, null, null, apiUrl, true); // 페이징벨류값, sortValue, order, 커맨드, isSearch
				$pagination.html(paging);
				
				let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
				let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
											
				if(listIndex > 0) {
					getListDtos.forEach((data) => { 
						$contentTable[0].insertAdjacentHTML('beforeend', setDataList(apiUrl, data, listIndex)); // jQuery 선택자에서 DOM 요소가져오기[0]
						listIndex --;
					});
					
				} else {
		            // 테이블의 전체 열 수 계산하기
					const maxCols = setTableColumnsNum();
					$contentTable.html(`
						<tr>
	                        <td colspan="${maxCols}">
	                            <p class="table_info">검색된 내용이 없습니다.</p>
	                        </td>
	                    </tr>
					`);
				}
				
			} else {
				logger.info('데이터가 없거나 유효하지 않습니다.');
				// 테이블의 전체 열 수 계산하기
				const maxCols = setTableColumnsNum();
				$contentTable.html(`
					<tr>
                        <td colspan="${maxCols}">
                            <p class="table_info">검색된 내용이 없습니다.</p>
                        </td>
                    </tr>
				`);
			}
			
		} catch(error) {
			logger.error(apiUrl + ' searchForm() error:', error);
		}
	}
}

// 로그인 유저 데이터 요청
async function getAccountInfo(modify=false) {
	try {
		const response = await $.ajax({
			url: '/account/info/get_account_info',
			method: 'GET',
		});
		
		logger.info('/account/info/get_account_info getAccountInfo() response:', response);
		
		if(response && modify) {
			const $contentInfoWrap = $('.content_info_wrap');
			$contentInfoWrap.html(setAccountModifyForm(response)); // account/modifyForm SET
		}
		
		return response;
	
	} catch(error) {
		logger.error('/account/info/get_account_info getAccountInfo() error:', error);
		throw new error('계정 정보를 불러오는 중 오류가 발생했습니다.');
	}
}

// 특정 게시판 데이터 요청
async function getBoardInfo(infoNo) {
	try {
		const response = await $.ajax({
			url: `/board/info/get_board_info?infoNo=${infoNo}`,
			method: 'GET',
		});
		
		logger.info(' getBoardInfo() response:', response);
		
		if(response) {
			return response;
		}
		
	} catch(error) {
		logger.error(' getBoardInfo() error:', error);
		throw new error('게시판 정보를 불러오는 중 오류가 발생했습니다.');
	}
}