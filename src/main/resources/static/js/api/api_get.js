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

// 콘텐츠 리스트 요청
async function getList(apiUrl, sortValue, order, page) {
    setAddLoading(true); // 로딩 시작
	setAllcheck(); // all_check 체크박스 초기화
	
	// 검색 인풋 벨류 삭제
	const searchStringInput = document.forms['search_form'].search_string;
	if(searchStringInput.value.trim().length) searchStringInput.value = ''; // 검색 이력이 남았을 경우에만 삭제
	
	const urlParams = new URLSearchParams(window.location.search);
	const infoNo = urlParams.get('infoNo') || undefined;
	
	const intPage = page || 1
	let params = `?page=${intPage}`;
	if(sortValue) params = `${params}&sortValue=${encodeURIComponent(sortValue)}&order=${encodeURIComponent(order)}`;
	if(infoNo) params = `${params}&infoNo=${infoNo}`;
	
	logger.info('apiUrl:', apiUrl + params);
	
	try {
		const response = await $.ajax({
			url: apiUrl + params,
			method: 'GET',
		});
		
		logger.info(`${apiUrl} getList() response:`, response);
		
		const { getListDtos, getListPage, getListCnt } = mapApiResponseObject(apiUrl, response);
		const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
		const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
		$contentTable.html('');
		$pagination.html('');
		
		if(response && getListDtos.length) {
			// 쿼리스트링 조건 추가
			setListQueryString(sortValue, order, getListPage.page); // page, sortValue, order
			
			if(response.reg_date) setContentSubInfo(response.reg_date); // 타이틀 옆 서브내용 표시(예: 업데이트 날짜 등)
							
			let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
			let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
			
			getListDtos.forEach((data) => { 
				$contentTable[0].insertAdjacentHTML('beforeend', generateTableList(apiUrl, data, listIndex));
				listIndex --;
			});
			
			// 페이지네이션 생성	
			const paging = generatePagination(getListPage, sortValue, order, apiUrl, false); // 페이징벨류값, sortValue, order, 커맨드, isSearch
			$pagination.html(paging);
			
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
		
	} finally {
		setAddLoading(false); // 로딩 제거
	}
}

// 검색 리스트 요청
async function getSearchList(event, apiUrl, page) {
	if(event) event.preventDefault();
	const form = document.forms['search_form'];
	let input;
	
	input = form.search_string;
	if(!validateEmpty(input, '검색어를', true, true)) { // 요소, text, alert 여부, 에러메세지 미노출 여부
		input.focus();
		return false;
	}
	
	if(input.value.trim().length < 2) {
		alert('검색어는 2자 이상 입력해 주세요.');
		input.focus();
		return false;
	}
	
	if(apiUrl) {
        setAddLoading(true); // 로딩 시작
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
			
			const { getListDtos, getListPage, getListCnt } = mapApiResponseObject(apiUrl, response);
			const $contentTable = $('.content_table tbody'); // 데이터가 나열될 테이블 요소
			const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
			$contentTable.html('');
			$pagination.html('');
			
			if(response && getListDtos.length) {
				// 쿼리스트링 조건 추가
				setSearchQueryString(getListPage.page, response.searchPart, response.searchString); // page, searchPart, searchString
				
				let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
				let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
						
				getListDtos.forEach((data) => { 
					$contentTable[0].insertAdjacentHTML('beforeend', generateTableList(apiUrl, data, listIndex)); // jQuery 선택자에서 DOM 요소가져오기[0]
					listIndex --;
				});
				
				// 페이지네이션 생성			
				const paging = generatePagination(getListPage, null, null, apiUrl, true); // 페이징벨류값, sortValue, order, 커맨드, isSearch
				$pagination.html(paging);
				
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
			
		} finally {
			setAddLoading(false); // 로딩 제거
		}
	}
}

// 요청 Api Response 객체 설정 
function mapApiResponseObject(apiUrl, response) { 
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
			
		case '/disease/info/get_disease_list': // 질환 / 질병 정보 관리
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListPageNum;
			getListCnt = response.diseaseListPageNum.diseaseListCnt;
			break;
			
		case '/disease/info/search_disease_list': // 질환 / 질병 정보 관리 검색
			getListDtos = response.diseaseDtos;
			getListPage = response.searchDiseaseListPageNum;
			getListCnt = response.searchDiseaseListPageNum.searchDiseaseListCnt;
			break;	
			
		case '/disease/info/get_disease_list_by_category': // 질환 / 질병 정보 관리 분류별 데이터
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListByCategoryPageNum;
			getListCnt = response.diseaseListByCategoryPageNum.diseaseListCnt;
			break;	
			
		case '/disease/cate_info/get_category_list': // 질환 / 질병 정보 분류 관리
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.diseaseCategoryListPageNum;
			getListCnt = response.diseaseCategoryListPageNum.diseaseCategoryListCnt;
			break;
			
		case '/disease/cate_info/search_disease_category_list': // 질환 / 질병 정보 분류 검색
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.searchDiseaseCategoryListPageNum;
			getListCnt = response.searchDiseaseCategoryListPageNum.searchDiseaseCategoryListCnt;
			break;
			
		case '/recipe/info/get_recipe_list': // 식단 정보 관리
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListPageNum;
			getListCnt = response.recipeListPageNum.recipeListCnt;
			break;
			
		case '/recipe/info/search_recipe_list': // 식단 정보 관리 검색
			getListDtos = response.recipeDtos;
			getListPage = response.searchRecipeListPageNum;
			getListCnt = response.searchRecipeListPageNum.searchRecipeListCnt;
			break;	
			
		case '/recipe/info/get_recipe_list_by_category': // 식단 정보 관리 분류별 데이터
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListByCategoryPageNum;
			getListCnt = response.recipeListByCategoryPageNum.recipeListByCategoryCnt;
			break;
			
		case '/video/info/get_video_list': // 영상 정보 관리
			getListDtos = response.videoDtos;
			getListPage = response.videoListPage;
			getListCnt = response.videoListPage.videoListCnt;
			break;
			
		case '/video/info/search_video_list': // 영상 정보 관리 검색
			getListDtos = response.videoDtos;
			getListPage = response.searchVideoListPage;
			getListCnt = response.searchVideoListPageNum.searchVideoListCnt;
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
			
		case '/board/info/get_posts_list': // 게시물
			getListDtos = response.postsDtos;
			getListPage = response.postsListPageNum;
			getListCnt = response.postsListPageNum.postsListCnt;
			break;
			
		case '/board/info/search_posts_list': // 게시물 검색
			getListDtos = response.postsDtos;
			getListPage = response.searchPostsListPageNum;
			getListCnt = response.searchPostsListPageNum.searchPostsListCnt;
			break;	
			
		case '/notice/info/get_notice_list': // 공지 사항
			getListDtos = response.noticeDtos;
			getListPage = response.noticeListPageNum;
			getListCnt = response.noticeListPageNum.noticeListCnt;
			break;
			
		case '/notice/info/search_notice_list': // 공지 사항 검색
			getListDtos = response.noticeDtos;
			getListPage = response.searchNoticeListPageNum;
			getListCnt = response.searchNoticeListPageNum.searchNoticeListCnt;
			break;	
			
		case '/qna/info/get_qna_list': // 질문과 답변
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

// 콘텐츠 테이블 리스트 생성
function generateTableList(apiUrl, data, listIndex) { 
	let tableTrContent = '';
	
	switch(apiUrl) {
		case '/account/list/get_admin_list':  // 관리자 계정 리스트 테이블
		case '/account/list/search_admin_list': // 관리자 계정 검색 리스트 테이블
			tableTrContent = `
				<tr>
		            <td>
		                <p class="table_info">${listIndex}</p>
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
			
		case '/disease/info/get_disease_list': // 질환/질병 정보 관리 리스트 테이블
		case '/disease/info/search_disease_list':            // 질환/질병 정보 관리 검색 리스트 테이블
		case '/disease/info/get_disease_list_by_category': // 질환/질병 정보 관리 분류선택 리스트 테이블
			tableTrContent = `
				<tr>
		            <td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="d_no" class="d_no" value="${data.d_no}"></div>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${data.diseaseCategoryDto.dc_name}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${data.d_name || 'N/A'}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.d_mod_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/disease/cate_info/get_category_list': // 질환/질병 분류 관리 리스트 테이블
		case '/disease/cate_info/search_disease_category_list': // 질환/질병 분류 관리 검색 리스트 테이블
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info">${listIndex}</a>
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
			
		case '/recipe/info/get_recipe_list': // 식단 정보 관리 리스트 테이블
		case '/recipe/info/search_recipe_list':            // 식단 정보 관리 검색 리스트 테이블
		case '/recipe/info/get_recipe_list_by_category': // 식단 정보 관리 분류선택 리스트 테이블
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${listIndex}</a>
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
			
		case '/video/info/get_video_list': // 영상 정보 관리 리스트 테이블
		case '/video/info/search_video_list': // 영상 정보 관리 검색 리스트 테이블
			tableTrContent = `
				<tr>
		            <td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="v_no" class="d_no" value="${data.v_no}"></div>
		            </td>
		            <td>
		                <a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td class="ta_l">
		                <a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info">${data.v_title}</a>
		            </td>
		            <td class="ta_l">
		                <a href="${data.v_link}" onclick="setWindowOpenPosition(this.href, '640', '360'); return false;" class="table_info">${data.v_link}</a>
		            </td>
		            <td>
		                <p class="table_info">${setFormatDate(data.v_mod_date) || 'N/A'}</p>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/cate_info/get_list': // 게시판 관리 리스트 테이블
		case '/board/cate_info/search_board_category_list': // 게시판 관리 검색 리스트 테이블
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/board/cate_info/modify_category_form?bc_no=${data.bc_no}" class="table_info">${listIndex}</a>
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
			
		case '/board/info/get_posts_list': // 게시물 리스트 테이블
		case '/board/info/search_posts_list': // 게시물 검색 리스트 테이블
			tableTrContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="bp_no" class="d_no" value="${data.bp_no}"></div>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${listIndex}</a>
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
			
		case '/notice/info/get_notice_list': // 공지 사항 리스트 테이블
		case '/notice/info/search_notice_category_list': // 공지 사항 검색 리스트 테이블
			tableTrContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="d_no" class="d_no" value="${data.n_no}"></div>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${listIndex}</a>
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
			
		case '/qna/info/get_qna_list': // 질문과 답변 리스트 테이블
		case '/qna/info/search_qna_list': // 질문과 답변 검색 리스트 테이블
			tableTrContent = `
				<tr>
					<td class="vam">
		                <div class="table_info func_area"><input type="checkbox" name="bq_no" class="bq_no" value="${data.bq_no}"></div>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${listIndex}</a>
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
			tableTrContent = '';
	}
	
	return tableTrContent;
}

// 페이지네이션 생성
function generatePagination(pagingValues, sortValue, order, apiUrl, isSearch) { // 페이징벨류값, sortValue, order, 커맨드, isSearch
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

// 버튼으로 정렬된 리스트 요청
function getSortList(event, dbTable, sortValue) {
    const sortBtn = event.currentTarget.closest('.sort'); // 클릭된 요소가 가장 가까운 부모 요소 중 클래스가 "sort"인 요소를 찾음
	if(!sortBtn) return; // 만약 sort 요소가 없다면 아무 작업도 하지 않음
	
    const apiUrl = mapSortListApiObject(dbTable); // 커맨드 가져오기
    const currentSortValue = sortBtn.getAttribute('data-current-sort-value'); // 현재 정렬 값 가져오기 default all
    const order = currentSortValue === 'asc' ? 'desc' : 'asc'; // 정렬 값 토글
    sortBtn.setAttribute('data-current-sort-value', order); // 버튼의 data-sort-value 속성 값 업데이트
	
	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 0); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
    getList(apiUrl, sortValue, order, 1); // 변경된 정렬 값으로 getList 호출
}

// sort getList() 요청에 필요한 객체 설정
function mapSortListApiObject(dbTable) {
	let apiUrl;
	
	switch(dbTable) {
		case 'disease': // 질환/질병 정보 관리 페이지
			apiUrl = '/disease/info/get_disease_list';
			break;
		
		case 'disease_category': // 질환/질병 분류 관리 페이지
			apiUrl = '/disease/cate_info/get_category_list';
			break;
			
		case 'admin_account': // 관리자 계정 관리 페이지
			apiUrl = '/account/list/get_admin_list';
			break;
			
		case 'recipe': // 식단 정보 관리 페이지
			apiUrl = '/recipe/info/get_recipe_list';
			break;
			
		case 'video': // 영상 정보 관리 페이지
			apiUrl = '/video/info/get_video_list';
			break;
			
		case 'board_category': // 게시판 관리 페이지
			apiUrl = '/board/cate_info/get_list';
			break;
			
		default:
			logger.error('mapSortListApiObject() dbTable:', value);
			return false;
	}
	
	return apiUrl;
}

// 선택된 카테고리의 리스트 요청
function getSelectList(event) {
	const sortBtn = event.target; // 클릭된 버튼 요소
	const sortValue = sortBtn.parentElement.getAttribute('data-sort-value'); // 정렬 종류 가져오기
	const order = sortBtn.getAttribute('data-order'); // 정렬할 값
	const apiUrl = mapSelectListApiObject(sortValue); // 커맨드 가져오기
	
	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 1); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
	getList(apiUrl, sortValue, order, 1);
}

// getList() 요청에 필요한 객체 설정
function mapSelectListApiObject(sortValue) {
	let apiUrl;
	
	switch(sortValue) {				
		case 'dc_no': // 질환/질병 정보 리스트 페이지 분류명별 필터
			apiUrl = '/disease/info/get_disease_list_by_category';
			break;
			
		case 'rcp_pat2': // 식단 정보 리스트 페이지 분류명별 필터
			apiUrl = '/recipe/info/get_recipe_list_by_category';
			break;
			
		default:
			logger.error('mapSelectListApiObject() sortValue:', value);
			return false;
	}
	
	return apiUrl;
}

// 카테고리 리스트 요청 및 옵션 생성
async function getCategoryList(ele, isForm, selectedValue) {
	const $selectEle = $(`#${ele}`); // 셀렉트 요소가 생성될 table th
	const { apiUrl, getListDtos, dataNo, dataName } = mapCategorylistObject(ele);
	
	if($selectEle.length) {
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'GET',
			});
			
			const categoryDto = response[getListDtos];
			logger.info(`${apiUrl} categoryDto:`, response);
			
			if(categoryDto && categoryDto.length) {
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

// 카테고리 리스트 요청에 필요한 객체 설정
function mapCategorylistObject(ele) {
	let apiUrl;
	let getListDtos;
	let dataNo;
	let dataName;
	
	switch(ele) {
		case 'dc_name': // 질환/질병 관련 페이지
		case 'd_category_no':
			apiUrl = '/disease/cate_info/get_category_list_select';
			getListDtos = 'diseaseCategoryDto';
			dataNo = 'dc_no';
			dataName = 'dc_name';
			break;
			
		case 'rcp_pat2': // 식단 관련 페이지
			apiUrl = '/recipe/cate_info/get_category_list_select';
			getListDtos = 'recipeCategoryDto';
			dataNo = 'rcp_pat2';
			dataName = 'rcp_pat2';
			break;
		
		default:
			logger.error('mapCategorylistObject() value:', value);
			return false;
	}
	
	return { apiUrl, getListDtos, dataNo, dataName };
}