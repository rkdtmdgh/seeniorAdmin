// 관리자 계정 리스트 요청
export function getAdminList(page, sort) {
	// 검색 인풋 벨류 삭제
	const seartStringInput = document.forms['search_form'].search_string;
	seartStringInput.value = '';
	
	let intPage = page || 1;
	
	$.ajax({
		url: `/account/get_admin_list${sort ? '?sort=' + sort : ''}`,
		method: 'GET',
		data: {
			page: intPage,
		},
	})
	.then(response => {
		console.log('getAdminList() response:', response);
		
		// url 검색 조건 추가
		const url = new URL(window.location);
		url.searchParams.delete('searchPart');
		url.searchParams.delete('searchString');
		url.searchParams.set('page', response.adminListPage.page); // 페이지
		window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
					
		const contentTable = document.querySelector('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const pagination = document.querySelector('.pagination_wrap'); // 페이지 네이션 요소
		contentTable.innerHTML = '';
		pagination.innerHTML = '';
		
		if(response && response.adminAccountDtos) {
			const pagingValues = response.adminListPage; // 페이지네이션을 위한 값들
			let currentPage = pagingValues.page; // 현재 페이지
			let maxPage = pagingValues.maxPage; // 마지막 페이지
			let startPage = pagingValues.startPage; // 현재 블럭의 시작 페이지
			let endPage = pagingValues.endPage; // 현재 블럭의 마지막 페이지
			let blockLimit = pagingValues.blockLimit; // 한 블럭에 포함되는 페이지 수
			let currentBlock = Math.ceil(currentPage / blockLimit); // 현재 블록
			let totalBlocks = Math.ceil(maxPage / blockLimit); // 전체 블록 수
			let paging = '';
			
			if(totalBlocks > 1) { // 블럭이 1개 이상일 경우에만 
				if(currentBlock > 1) { // 2번째 블럭 부터 노출
					paging += `
						<div onclick="getList()" class="first func_icon">
				            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
				                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
				             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
				            </svg>
				        </div>
				        
				        <div onclick="getList(${startPage - 1})" class="prev func_icon">
				            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
				                <polygon points="15,0 5,10 15,20 "/>
				            </svg>
				        </div>
					`;
				}
			}
			
			for(let i = startPage; i <= endPage; i++) {
				if(i === currentPage) {
					paging += `<div class="current">${i}</div>`;
				} else {
					paging += `<div class="num" onclick="getList(${i})">${i}</div>`;
				}
			}
			
			if(totalBlocks > 1) {
				if(currentBlock < totalBlocks) { // 마지막 전 블럭까지 노출
					paging += `
						<div onclick="getList(${endPage + 1})" class="next func_icon">
				            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
				                <polygon points="15,0 5,10 15,20 "/>
				            </svg>
				        </div>
				        
				        <div onclick="getList(${maxPage})" class="last func_icon">
				            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
				                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
				             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
				            </svg>
				        </div>
					`;
				}
			}
			
			pagination.innerHTML = paging;
			
			let pageLimit = pagingValues.pageLimit; // 한 페이지에 노출될 리스트 수
			let totalCnt = pagingValues.accountListCnt; // 총 리스트 합계
			let adminListCnt = totalCnt - (pageLimit * (currentPage - 1)); 

			response.adminAccountDtos.forEach((data) => { 
				let innerContent = `
					<tr>
                        <td>
                            <p class="table_info">${adminListCnt}</p>
                        </td>
                        <td>
                            <a href="" class="table_info">${data.a_id}</a>
                        </td>
                        <td>
                            <a href="" class="table_info">${data.a_authority_role}</a>
                        </td>
                        <td>
                            <p class="table_info">${data.a_phone}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.a_name}</p>
                        </td>
                        <td>
                            <p class="table_info">${formatDate(data.a_reg_date)}</p>
                        </td>
                    </tr>
				`;
				contentTable.innerHTML += innerContent;
				adminListCnt --;
			});
		} else {
			console.log('데이터가 없거나 유효하지 않습니다.');
		}
	})
	.catch((error) => {
		console.error('getAdminList() error:', error);
	});
}