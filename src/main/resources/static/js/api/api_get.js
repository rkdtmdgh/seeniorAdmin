// 콘텐츠 리스트 요청
async function getList(command, sort, sortValue, page) {
	setAllcheck(); // all_check 체크박스 초기화
	
	// 검색 인풋 벨류 삭제
	const searchStringInput = document.forms['search_form'].search_string;
	if(searchStringInput.value.trim().length > 0) searchStringInput.value = ''; // 검색 이력이 남았을 경우에만 삭제
	
	const intPage = page || 1;
	let queryParams = `?page=${intPage}`;
	if(sort) queryParams = queryParams + `&${sort}=${sortValue}`;
	const apiUrl = command + queryParams;
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'GET',
		});
		
		logger.info(apiUrl + ' getList() response:', response);
		
		const { getListDtos, getListPage, getListCnt } = setParseResponseByCommand(command, response);
		const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
		$contentTable.html('');
		$pagination.html('');
		
		if(response && getListDtos) {
			// 쿼리스트링 조건 추가
			setListQueryString(getListPage.page, sort, sortValue); // page, sort, sortValue
					
			const paging = setPagination(getListPage, sort, sortValue, command); // 페이징벨류값, sort, sortValue, 커맨드, isSearch
			$pagination.html(paging);
			
			let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
			let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
			
			if(listIndex > 0) {
				getListDtos.forEach((data) => { 
					$contentTable[0].insertAdjacentHTML('beforeend', setDataList(command, data, listIndex));
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
		logger.error(command + ' error:', error);
	}
}

// 버튼으로 정렬된 리스트 요청
function getSortList(event, command, defaultSort, changeSort) {
	if(event) event.preventDefault();
    const sortBtn = event.currentTarget.closest('.sort'); // 클릭된 요소가 가장 가까운 부모 요소 중 클래스가 "sort"인 요소를 찾음
	if(!sortBtn) return; // 만약 sort 요소가 없다면 아무 작업도 하지 않음
	
    const sort = sortBtn.getAttribute('data-sort'); // 정렬 종류 가져오기
    const currentSort = sortBtn.getAttribute('data-current-sort'); // 현재 정렬 값 가져오기
    const newSort = currentSort === defaultSort ? changeSort : defaultSort; // 정렬 값 토글
    sortBtn.setAttribute('data-current-sort', newSort); // 버튼의 data-sort 속성 값 업데이트
	
	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 0); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
    getList(command, sort, newSort, 1); // 변경된 정렬 값으로 getList 호출
}

// 셀렉트로 정렬된 리스트 요청
function getSelectList(event) {
	const sortBtn = event.target; // 클릭된 버튼 요소
	const command = sortBtn.parentElement.getAttribute('data-api'); // 커맨드 가져오기
	const sort = sortBtn.parentElement.getAttribute('data-sort'); // 정렬 종류 가져오기
	const sortValue = sortBtn.getAttribute('data-sort-value'); // 정렬할 값

	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 1); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
	getList(command, sort, sortValue, 1);
}

// 콘텐츠 정렬 셀렉트 옵션 리스트 요청
async function getOptionList(apiUrl, ele, isForm, selectedValue) {
	const selectEle = document.getElementById(ele); // 셀렉트 요소가 생성될 table th
	let getListDtos;
	let dataNo;
	let dataName;
	let command;
	
	if(selectEle) {
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'GET',
			});
			
			switch(apiUrl) {
				case '/disease/get_category_list': // 질환 / 질병 정보 관리 분류명 정렬
					getListDtos = response.diseaseCategoryDto;
					dataNo = 'dc_no';
					dataName = 'dc_name';
					command = '/disease/get_disease_list_by_category_with_page';
					break;
				
				default:
					logger.error('Unknown API URL:', apiUrl);
					return;
			}
			
			logger.info(apiUrl + ' getListDtos:', getListDtos);
			
			if(getListDtos && getListDtos.length > 0) {
				if(isForm) {
					getListDtos.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let selected = selectedValue ? data[dataNo] === selectedValue ? 'selected' : '' : '';
						let option = `<option value="${data[dataNo]}" ${selected}>${data[dataName]}</option>`;
						if(selected) {
							selectEle.insertAdjacentHTML('afterbegin', option);
						} else {
							selectEle.insertAdjacentHTML('beforeend', option);
						}
					});
				} else {
					const ceateSelect = `<ul data-sort="${dataNo}" data-api="${command}" class="select_option_list sc"></ul>`;
			        selectEle.insertAdjacentHTML('beforeend', ceateSelect);
			        const $selectOptionlist = $('ul.select_option_list');
					
					getListDtos.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let option = `<li data-sort-value="${data[dataNo]}" class="option" onclick="getSelectList(event);">${data[dataName]}</li>`;
						$selectOptionlist[0].insertAdjacentHTML('beforeend', option);
					});
				}
				
			} else {
				selectEle.classList.remove('select');
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
	if(!checkEmpty(input, '검색어를', true)) { // true: alert으로 메세지 띄우기
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
		
		let intPage = page || 1;
		logger.info('searchForm() searchPart:', form.search_part.value);
		logger.info('searchForm() searchString:', input.value.trim());
				
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'GET',
				data: {
					searchPart: form.search_part.value, // 검색 분류 select
					searchString: input.value.trim(), // 검색 입력값
					page: intPage,
				},
			});
			
			logger.info(apiUrl + ' searchForm() response:', response);
			
			const { getListDtos, getListPage, getListCnt } = setParseResponseByCommand(apiUrl, response);
			const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
			const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
			$contentTable.html('');
			$pagination.html('');
			
			if(response && getListDtos) {
				// 쿼리스트링 조건 추가
				setSearchQueryString(getListPage.page, response.searchPart, response.searchString); // page, searchPart, searchString
							
				const paging = setPagination(getListPage, null, null, apiUrl, true); // 페이징벨류값, sort, sortValue, 커맨드, isSearch
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
