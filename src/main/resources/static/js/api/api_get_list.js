// 관리자 계정 리스트 요청
export function getAdminList(sort, sortValue, page) {
	// 검색 인풋 벨류 삭제
	const searchStringInput = document.forms['search_form'].search_string;
	if(searchStringInput.value.trim().length > 0) searchStringInput.value = ''; // 검색 이력이 남았을 경우에만 삭제
	
	const intPage = page || 1;
	const queryParams =  sort ? `?sort=${sortValue}&page=${intPage}` : `?page=${intPage}`;
	
	$.ajax({
		url: `/account/get_admin_list${queryParams}`,
		method: 'GET',
	})
	.then(response => {
		logger.info('getAdminList() response:', response);
		
		// 쿼리스트링 조건 추가
		// page, sort or part, sortValue or string, boolSearch // boolSearch값에 따라 사용값 다름
		setQueryString(response.adminListPage.page, sort, sortValue);
					
		const contentTable = document.querySelector('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const pagination = document.querySelector('.pagination_wrap'); // 페이지 네이션 요소
		contentTable.innerHTML = '';
		pagination.innerHTML = '';
		
		if(response && response.adminAccountDtos) {
			const pagingValues = response.adminListPage; // 페이지네이션을 위한 값들
			const paging = setPagination(pagingValues, 'getList', sort, sortValue); // 페이징벨류값, 핸들러,  sort, 검색 커맨드 or sortValue, boolSearch
			pagination.innerHTML = paging;
			
			let pageLimit = pagingValues.pageLimit; // 한 페이지에 노출될 리스트 수
			let totalCnt = pagingValues.accountListCnt; // 총 리스트 합계
			let adminListCnt = totalCnt - (pageLimit * (pagingValues.page - 1)); // 현재 페이지의 첫번째 리스트 index 값

			if(adminListCnt > 0) {
				response.adminAccountDtos.forEach((data) => { 
					contentTable.insertAdjacentHTML('beforeend', setDataList('getAdminList', data, adminListCnt));
					adminListCnt --;
				});
			} else {
				contentTable.innerHTML = `
					<tr>
	                    <td colspan="6">
	                        <p class="table_info">목록이 없습니다.</p>
	                    </td>
	                </tr>
				`;
			}
			
		} else {
			logger.info('데이터가 없거나 유효하지 않습니다.');
		}
	})
	.catch((error) => {
		logger.error('getAdminList() error:', error);
	});
}