// 콘텐츠 리스트 요청
export function getList(command, sort, sortValue, page) {
	// 검색 인풋 벨류 삭제
	const searchStringInput = document.forms['search_form'].search_string;
	if(searchStringInput.value.trim().length > 0) searchStringInput.value = ''; // 검색 이력이 남았을 경우에만 삭제
	
	const intPage = page || 1;
	let queryParams = `?page=${intPage}`;
	if(sort) queryParams = queryParams + `&sort=${sortValue}`;
	const apiUrl = command + queryParams;
	
	$.ajax({
		url: apiUrl,
		method: 'GET',
	})
	.then(response => {
		logger.info(command + ' getList() response:', response);
					
		const contentTable = document.querySelector('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const pagination = document.querySelector('.pagination_wrap'); // 페이지 네이션 요소
		contentTable.innerHTML = '';
		pagination.innerHTML = '';
		
		const { getListDtos, getListPage } = setParseResponseByCommand(command, response);
		if(response && getListDtos) {
			// 쿼리스트링 조건 추가
			setListQueryString(getListPage.page, sort, sortValue); // page, sort, sortValue
					
			const paging = setPagination(getListPage, sort, sortValue, command); // 페이징벨류값, sort, sortValue, 커맨드, isSearch
			pagination.innerHTML = paging;
			
			let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
			let totalCnt = getListPage.accountListCnt; // 총 리스트 합계
			let adminListCnt = totalCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
			
			if(adminListCnt > 0) {
				getListDtos.forEach((data) => { 
					contentTable.insertAdjacentHTML('beforeend', setDataList(command, data, adminListCnt));
					adminListCnt --;
				});
			} else {
				const maxCols = setTableColumnsNum();
				contentTable.innerHTML = `
					<tr>
	                    <td colspan="${maxCols}">
	                        <p class="table_info">목록이 없습니다.</p>
	                    </td>
	                </tr>
				`;
			}
			
		} else {
			logger.info('데이터가 없거나 유효하지 않습니다.');
			const maxCols = setTableColumnsNum();
			contentTable.innerHTML = `
				<tr>
                    <td colspan="${maxCols}">
                        <p class="table_info">목록이 없습니다.</p>
                    </td>
                </tr>
			`;
		}
	})
	.catch((error) => {
		logger.error('getAdminList() error:', error);
	});
}
