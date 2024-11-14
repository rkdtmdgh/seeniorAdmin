// 함수 디바운싱 적용 // 함수, key명
const getList = debounceAsync(getListProcess, 'getListProcess'); // 콘텐츠 리스트 요청
const getSearchList = debounceAsync(getSearchListProcess, 'getSearchListProcess'); // 검색 리스트 요청

// 메인 콘텐츠 리스트 요청 함수 실행
function getMainContentList() {
	const apiList = [ // 요청할 api, 로딩 요소 객체 배열
		{apiUrl: '/report/main/get_report_list' , loddingParentEle: 'main_report_table'}, // 메인 신고 현황 콘텐츠 요청 객체 설정
		{apiUrl: '/advertisement/main/get_advertisement_list' , loddingParentEle: 'main_advertisement_table'}, // 메인 광고 현황 콘텐츠 요청 객체 설정
		{apiUrl: '/qna/main/get_qna_list' , loddingParentEle: 'main_qna_table'}, // 메인 QnA 현황 콘텐츠 요청 객체 설정
		{apiUrl: '/notice/main/get_notice_list' , loddingParentEle: 'main_notice_table'}, // 메인 전체 공지 사항 콘텐츠 요청 객체 설정
		{apiUrl: '/qna/main/get_notice_list' , loddingParentEle: 'main_qna_notice_table'}, // 메인 QnA 공지 사항 콘텐츠 요청 객체 설정
		{apiUrl: '/board/main/get_notice_list' , loddingParentEle: 'main_board_notice_table'}, // 메인 게시판 공지 사항 콘텐츠 요청 객체 설정
	];
	
	// 각 요청을 독립적으로 실행하여 개별적으로 응답 처리(응답이 빠른 순서대로 처리)
	apiList.forEach(apiObj => getMainList(apiObj.apiUrl, apiObj.loddingParentEle));
}

// 메인 콘텐츠 리스트 요청
async function getMainList(apiUrl, loddingParentEle) {
	const page_limit = 5; // 데이터 리스트 개수
	setLoading(true, loddingParentEle); // 로딩 추가
	try {
		const response = await $.ajax({
			url: `${apiUrl}?page_limit=${page_limit}`,
			method: 'GET',
		});
		
		logger.info(`${apiUrl} getMainList() response:`, response);
		
		// 요청 성공 시 처리 로직 실행
		mainContentApiResponse(apiUrl, response, loddingParentEle);
		
	} catch(error) {
		logger.error(apiUrl + ' getMainList() error:', error);
		
	} finally {
		setLoading(false, loddingParentEle); // 로딩 제거
	}
}

// 메인 콘텐츠 요청 성공 시 처리 로직
function mainContentApiResponse(apiUrl, response, loddingParentEle) {
	const { getListDtos } = mapApiResponseObject(apiUrl, response); // 요청 Api Response 객체 설정
	const $contentTable = $(`.${loddingParentEle} table tbody`); // 데이터가 나열될 테이블 요소
	$contentTable.html(''); // 콘텐츠 초기화
	
	if(response && getListDtos.length) {				
		getListDtos.forEach((data) => { 			   
			$contentTable[0].insertAdjacentHTML(
				'beforeend', 
				generateTableList(apiUrl, data) // apiUrl, dtoData, listCnt, 현재 페이지 첫번째 index값, page
			);
		});
		
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
}

// 콘텐츠 리스트 요청
async function getListProcess(apiUrl, sortValue, order, page, resetParams = false) {
	setAllcheck(); // all_check 체크박스 초기화
	
	if(resetParams) setDelQueryString(); // 쿼리 파라미터 제거
	
    // 검색 인풋 벨류 삭제
	const $searchStringInput = $('form[name="search_form"]').find('input[name="searchString"]');
	if($searchStringInput.length && $searchStringInput.val().trim()) $searchStringInput.val(''); // 검색 이력이 남았을 경우에만 삭제
	
	const urlParams = new URLSearchParams(window.location.search);
	const infoNo = urlParams.get('infoNo') || undefined;
	
	const params = new URLSearchParams(); // URL 쿼리 파라미터 생성
	if(infoNo) params.append('infoNo', infoNo);
	if(sortValue) { // sort값이 있을 경우 추가
		params.append('sortValue', sortValue);
		params.append('order', order);
	}
	params.append('page', page || 1); // 페이지 추가
	
	logger.info(`apiUrl: ${apiUrl}?${params.toString()}`);
	
	setLoading(true, 'content_inner'); // 로딩 추가
	try {
		const response = await $.ajax({
			url: `${apiUrl}?${params.toString()}`,
			method: 'GET',
		});
		
		logger.info(`${apiUrl} getList() response:`, response);
		
		// 요청 성공 시 처리 로직 실행
		contentApiResponse(apiUrl, sortValue, order, response);
		
	} catch(error) {
		logger.error(apiUrl + ' error:', error);
		
	} finally {
		setLoading(false, 'content_inner'); // 로딩 제거
	}

}

// 검색 리스트 요청
async function getSearchListProcess(event, apiUrl, sortValue, order, page) {
	if(event) event.preventDefault();
	const form = document.forms['search_form'];
	let input;
	
	input = form.search_string;
	if(!validateEmpty(input, '검색어', true, true)) { // 요소, text, alert 여부, 에러메세지 미노출 여부
		input.focus();
		return false;
	}
	
	if(input.value.trim().length < 2) {
		alert('검색어는 2자 이상 입력해 주세요.');
		input.focus();
		return false;
	}
		
	setAllcheck(); // all_check 체크박스 초기화
	
	const formData = new FormData(form); // form에 모든 정보 가져오기
	const params = new URLSearchParams(); // URL 쿼리 파라미터 생성
	
	formData.forEach((value, key) => { // formData의 모든 값을 쿼리 파라미터에 추가
		params.append(key, value);
	});
	
	const urlParams = new URLSearchParams(window.location.search);
	const infoNo = urlParams.get('infoNo') || undefined;
	if(infoNo) params.append('infoNo', infoNo); // infoNo값이 있을 경우 추가 (분류 no 값)
	if(sortValue) { // sort값이 있을 경우 추가
		params.append('sortValue', sortValue);
		params.append('order', order);				
	}
	params.append('page', page || 1); // 페이지 추가
	
	logger.info('search params:', params.toString());
			
	setLoading(true, 'content_inner'); // 로딩 추가
	try {
		const response = await $.ajax({
			url: `${apiUrl}?${params.toString()}`,
			method: 'GET',
		});
		
		logger.info(`${apiUrl} searchForm() response:`, response);
		
		// 요청 성공 시 처리 로직 실행
		contentApiResponse(apiUrl, sortValue, order, response);
		
	} catch(error) {
		logger.error(apiUrl + ' searchForm() error:', error);
		
	} finally {
		setLoading(false, 'content_inner'); // 로딩 제거
	}
}

// 콘텐츠 리스트 요청 성공 시 처리 로직
function contentApiResponse(apiUrl, sortValue, order, response, contentTable = '.content_table tbody') {
	const { getListDtos, getListPage, getListCnt, otherData } = mapApiResponseObject(apiUrl, response); // 요청 Api Response 객체 설정
	const searchPart = response.searchPart || null; // 리턴된 searchPart 값
	const searchString = response.searchString || null; // 리턴된 searchString 값
	const isSearch = searchString !== null; // searchString 값이 있을 경우 검색 요청
	const $contentTable = $(contentTable); // 데이터가 나열될 테이블 요소
	const $pagination = $('.pagination_wrap'); // 페이지 네이션 요소
	$contentTable.html(''); // 콘텐츠 초기화
	$pagination.html(''); // 페이지네이션 초기화
	
	setQueryString(sortValue, order, getListPage.page, searchPart, searchString); // 쿼리스트링 조건 추가
	setContentSubInfo(otherData); // 타이틀 옆 서브내용 표시(예: 업데이트 날짜 등)
	
	if(response && getListDtos.length) {		
		let pageLimit = getListPage.pageLimit; // 한 페이지에 노출될 리스트 수
		let listIndex = getListCnt - (pageLimit * (getListPage.page - 1)); // 현재 페이지의 첫번째 리스트 index 값
		
		getListDtos.forEach((data) => { 			   
			$contentTable[0].insertAdjacentHTML(
				'beforeend', 
				generateTableList(apiUrl, data, getListCnt, listIndex, getListPage.page) // apiUrl, dtoData, listCnt, 현재 페이지 첫번째 index값, page
			);
			listIndex --;
		});
		
		// 페이지네이션 생성	
		const paging = generatePagination(apiUrl, sortValue, order, getListPage, isSearch); // apiUrl, sortValue, order, 페이징벨류값, isSearch
		$pagination.html(paging);
		
	} else {
		logger.info('데이터가 없거나 유효하지 않습니다.');
		const maxCols = setTableColumnsNum();
		$contentTable.html(`
			<tr>
                <td colspan="${maxCols}">
                    <p class="table_info">${isSearch ? '검색된 내용이 없습니다.' : '목록이 없습니다.'}</p>
                </td>
            </tr>
		`);
	}
};

// 요청 Api Response 객체 설정 
function mapApiResponseObject(apiUrl, response) { 
	let getListDtos = null;
	let getListPage = null;
	let getListCnt = null;
	let otherData = null;
	
	switch(apiUrl) {
		case '/report/main/get_report_list': // 메인 신고 현황
			getListDtos = response.reportDtos;
			break;
			
		case '/advertisement/main/get_advertisement_list': // 메인 광고 현황
			getListDtos = response.advertisementDtos;
			break;
			
		case '/qna/main/get_qna_list': // 메인 질문과 답변
			getListDtos = response.qnaDtos;
			break;
			
		case '/notice/main/get_notice_list': // 메인 전체 공지 사항
			getListDtos = response.noticeDtos;
			break;
			
		case '/qna/main/get_notice_list': // 메인 QnA 공지 사항
			getListDtos = response.qnaNoticeDtos;
			break;
			
		case '/board/main/get_notice_list': // 메인 게시판 공지 사항
			getListDtos = response.boardNoticeDtos;
			break;
			
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
			
		case '/user_account/info/get_user_account_list': // 회원 관리
			getListDtos = response.userAccountDtos;
			getListPage = response.userAccountListPage;
			getListCnt = response.userAccountListPage.userAccountListCnt;
			break;
			
		case '/user_account/info/search_user_account_list': // 회원 관리 검색
			getListDtos = response.userAccountDtos;
			getListPage = response.searchUserAccountListPageNum;
			getListCnt = response.searchUserAccountListPageNum.searchUserListCnt;
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
			
		case '/disease/info/get_disease_list_by_category': // 질환 / 질병 정보 관리 질병군별 데이터
			getListDtos = response.diseaseDtos;
			getListPage = response.diseaseListByCategoryPageNum;
			getListCnt = response.diseaseListByCategoryPageNum.diseaseListCnt;
			break;	
			
		case '/disease/cate_info/get_category_list': // 질환 / 질병 정보 분류 관리
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.diseaseCategoryListPageNum;
			getListCnt = response.diseaseCategoryListPageNum.diseaseCategoryListCnt;
			break;
			
		case '/disease/cate_info/search_category_list': // 질환 / 질병 정보 분류 검색
			getListDtos = response.diseaseCategoryDtos;
			getListPage = response.searchDiseaseCategoryListPageNum;
			getListCnt = response.searchDiseaseCategoryListPageNum.searchDiseaseCategoryListCnt;
			break;
			
		case '/recipe/info/get_recipe_list': // 식단 정보 관리
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListPageNum;
			getListCnt = response.recipeListPageNum.recipeListCnt;
			otherData = `마지막 업데이트 ${response.reg_date}`;
			break;
			
		case '/recipe/info/search_recipe_list': // 식단 정보 관리 검색
			getListDtos = response.recipeDtos;
			getListPage = response.searchRecipeListPageNum;
			getListCnt = response.searchRecipeListPageNum.searchRecipeListCnt;
			otherData = `마지막 업데이트 ${response.reg_date}`;
			break;	
			
		case '/recipe/info/get_recipe_list_by_type': // 식단 정보 관리 음식 종류별 데이터
			getListDtos = response.recipeDtos;
			getListPage = response.recipeListByTypePageNum;
			getListCnt = response.recipeListByTypePageNum.recipeListByTypeCnt;
			otherData = `마지막 업데이트 ${response.reg_date}`;
			break;
			
		case '/video/info/get_video_list': // 영상 정보 관리
			getListDtos = response.videoDtos;
			getListPage = response.videoListPage;
			getListCnt = response.videoListPage.videoListCnt;
			break;
			
		case '/video/info/search_video_list': // 영상 정보 관리 검색
			getListDtos = response.videoDtos;
			getListPage = response.searchVideoListPage;
			getListCnt = response.searchVideoListPage.searchVideoListCnt;
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
			
		case '/qna/noti_info/get_notice_list': // 질문과 답변 공지 사항
			getListDtos = response.qnaNoticeDtos;
			getListPage = response.qnaNoticeListPageNum;
			getListCnt = response.qnaNoticeListPageNum.qnaNoticeListCnt;
			break;
			
		case '/qna/noti_info/search_notice_list': // 질문과 답변 공지 사항 검색
			getListDtos = response.qnaNoticeDtos;
			getListPage = response.searchQnaNoticeListPageNum;
			getListCnt = response.searchQnaNoticeListPageNum.searchQnaNoticeListCnt;
			break;
			
		case '/board/noti_info/get_notice_list': // 공지 게시물
			getListDtos = response.boardNoticePostsDtos;
			getListPage = response.boardNoticePostsListPageNum;
			getListCnt = response.boardNoticePostsListPageNum.boardNoticePostsListCnt;
			break;
			
		case '/board/info/search_notice_list': // 공지 게시물 검색
			getListDtos = response.boardNoticePostsDtos;
			getListPage = response.searchBoardNoticePostsListPageNum;
			getListCnt = response.searchBoardNoticePostsListPageNum.searchNoticePostsListCnt;
			break;
		
		case '/qna/cate_info/get_category_list': // 질문 유형 분류 관리
			getListDtos = response.qnaCategoryDtos;
			getListPage = response.qnaCategoryListPageNum;
			getListCnt = response.qnaCategoryListPageNum.qnaCategoryListCnt;
			break;
			
		case '/qna/cate_info/search_category_list': // 질문 유형 분류 검색
			getListDtos = response.qnaCategoryDtos;
			getListPage = response.searchQnaCategoryListPageNum;
			getListCnt = response.searchQnaCategoryListPageNum.searchQnaCategoryListCnt;
			break;
			
		case '/qna/info/get_qna_list': // 질문과 답변
			getListDtos = response.qnaDtos;
			getListPage = response.qnaListPageNum;
			getListCnt = response.qnaListPageNum.qnaListCnt;
			otherData = response.unansweredQnaCnt > 0 ? `답변 대기 ${response.unansweredQnaCnt}` : null;
			break;
			
		case '/qna/info/search_qna_list': // 질문과 답변 검색
			getListDtos = response.qnaDtos;
			getListPage = response.searchQnaListPageNum;
			getListCnt = response.searchQnaListPageNum.searchQnaListCnt;
			otherData = response.unansweredSearchQnaCnt > 0 ? `답변 대기 ${response.unansweredSearchQnaCnt}` : null;
			break;	
			
		case '/qna/info/get_qna_list_by_category': // 질문 유형별 데이터
			getListDtos = response.qnaDtos;
			getListPage = response.qnaListByCategoryPageNum;
			getListCnt = response.qnaListByCategoryPageNum.qnaListCnt;
			otherData = response.unansweredCategoryQnaCnt > 0 ? `답변 대기 ${response.unansweredCategoryQnaCnt}` : null;
			break;
			
		case '/board/cate_info/get_category_list': // 게시판 관리
			getListDtos = response.boardCategoryDtos;
			getListPage = response.boardCategoryListPageNum;
			getListCnt = response.boardCategoryListPageNum.boardCategoryListCnt;
			break;
			
		case '/board/cate_info/search_category_list': // 게시판 관리 검색
			getListDtos = response.boardCategoryDtos;
			getListPage = response.searchBoardCategoryListPageNum;
			getListCnt = response.searchBoardCategoryListPageNum.searchBoardCategoryListCnt;
			break;
			
		case '/board/info/get_posts_list': // 일반 게시물
			getListDtos = response.boardPostsDtos;
			getListPage = response.boardPostsListPageNum;
			getListCnt = response.boardPostsListPageNum.boardPostsListCnt;
			break;
			
		case '/board/info/search_posts_list': // 일반 게시물 검색
			getListDtos = response.boardPostsDtos;
			getListPage = response.searchBoardPostsListPageNum;
			getListCnt = response.searchBoardPostsListPageNum.searchBoardPostsListCnt;
			break;
			
		case '/report/cate_info/get_category_list': // 신고 분류 관리
			getListDtos = response.reportCategoryDtos;
			getListPage = response.reportCategoryListPageNum;
			getListCnt = response.reportCategoryListPageNum.reportCategoryListCnt;
			break;
			
		case '/report/cate_info/search_category_list': // 신고 분류 검색
			getListDtos = response.reportCategoryDtos;
			getListPage = response.searchReportCategoryListPageNum;
			getListCnt = response.searchReportCategoryListPageNum.searchReportCategoryListCnt;
			break;
			
		case '/report/info/get_report_list': // 신고 관리
			getListDtos = response.reportDtos;
			getListPage = response.reportListPageNum;
			getListCnt = response.reportListPageNum.reportListCnt;
			otherData = response.unresultedReportCnt > 0 ? `처리 대기 ${response.unresultedReportCnt}` : null;
			break;
			
		case '/report/info/search_report_list': // 신고 관리 검색
			getListDtos = response.reportDtos;
			getListPage = response.searchReportListPageNum;
			getListCnt = response.searchReportListPageNum.searchReportListCnt;
			otherData = response.unresultedSearchReportCnt > 0 ? `처리 대기 ${response.unresultedSearchReportCnt}` : null;
			break;	
			
		case '/report/info/get_report_list_by_category': // 신고 유형별 데이터
			getListDtos = response.reportDtos;
			getListPage = response.reportListByCategoryPageNum;
			getListCnt = response.reportListByCategoryPageNum.reportListCnt;
			otherData = response.unresultedReportCntByCategory > 0 ? `처리 대기 ${response.unresultedReportCntByCategory}` : null;
			break;	
			
		case '/advertisement/info/get_advertisement_list': // 광고 관리
			getListDtos = response.advertisementDtos;
			getListPage = response.advertisementListPageNum;
			getListCnt = response.advertisementListPageNum.advertisementListCnt;
			break;	
			
		case '/advertisement/info/search_advertisement_list': // 광고 관리 검색
 			getListDtos = response.advertisementDtos;
			getListPage = response.searchAdvertisementListPageNum;
			getListCnt = response.searchAdvertisementListPageNum.searchAdvertisementListCnt;
			break;	
			
		case '/advertisement/info/get_advertisement_list_by_category': // 광고 관리 위치별 데이터
		case '/advertisement/cate_info/get_advertisement_list_by_category': // 광고 분류 상세페이지 내 위치별 데이터
			getListDtos = response.advertisementDtos;
			getListPage = response.advertisementByCategoryPageNum;
			getListCnt = response.advertisementByCategoryPageNum.advertisementListByCategoryCnt;
			break;
			
		case '/advertisement/cate_info/get_category_list': // 광고 분류 관리
			getListDtos = response.advertisementCategoryDtos;
			getListPage = response.advertisementCategoryListPageNum;
			getListCnt = response.advertisementCategoryListPageNum.advertisementCategoryListCnt;
			break;
			
		case '/advertisement/cate_info/search_category_list': // 광고 분류 검색
			getListDtos = response.advertisementCategoryDtos;
			getListPage = response.searchAdvertisementCategoryListPageNum;
			getListCnt = response.searchAdvertisementCategoryListPageNum.searchAdvertisementCategoryListCnt;
			break;
			
	}
		
	return { getListDtos, getListPage, getListCnt, otherData }
}

// 콘텐츠 테이블 리스트 생성
function generateTableList(apiUrl, data, getListCnt, listIndex, page) { 
	let tableTrContent = '';
	let regDate = null; // 날짜 비교 값 저장 변수
	const nowDate = new Date();
	const newIconsHours = 1000 * 60 * 60 * 24; // 24시간
	
	switch(apiUrl) {
		case '/report/main/get_report_list': // 메인 신고 현황 테이블
			regDate = new Date(new Date(data.br_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/report/info/result_form?br_no=${data.br_no}" class="table_info">${data.reportCategoryDto.brr_name}</a>
		            </td>
		            <td>
		                <a href="/report/info/result_form?br_no=${data.br_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.br_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.userAccountDto.u_no}" class="table_info">
		                	${data.userAccountDto.u_name}
		                </a>
		            </td>
		            <td>
		                <a href="/report/info/result_form?br_no=${data.br_no}" class="table_info">${setFormatDate(data.br_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/advertisement/main/get_advertisement_list': // 메인 광고 현황 테이블
			regDate = new Date(new Date(data.ad_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${data.advertisementCategoryDto.ac_name}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.ad_client}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_start_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_end_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/main/get_qna_list': // 메인 질문과 답변 현황 테이블
			regDate = new Date(new Date(data.bq_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${data.qnaCategoryDto.bqc_name}</a>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bq_title}</p>
		                	${data.bq_state === true ? '<img src="/image/icons/lock.png" alt="비공개글" class="table_info_icons">' : ''}
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.userAccountDto.u_no}" class="table_info">
		                	${data.userAccountDto.u_name}
		                </a>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${setFormatDate(data.bq_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/notice/main/get_notice_list': // 메인 전체 공지 사항 테이블
			regDate = new Date(new Date(data.n_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.n_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">${data.adminAccountDto.a_name}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${setFormatDate(data.n_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${data.n_view_cnt}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/main/get_notice_list': // 메인 QnA 공지 사항 테이블
			regDate = new Date(new Date(data.bqn_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bqn_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">${data.adminAccountDto.a_name}</a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${setFormatDate(data.bqn_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${data.bqn_view_cnt}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/main/get_notice_list': // 메인 게시판 공지 사항 테이블
			regDate = new Date(new Date(data.bn_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
					<td>
		                <a href="/board/noti_info/modify_notice_form?infoNo=${data.bn_category_no}bn_no=${data.bn_no}" class="table_info">
		                	${data.boardCategoryDto.bc_name}
		                </a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_notice_form?infoNo=${data.bn_category_no}bn_no=${data.bn_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bn_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">
		               		${data.adminAccountDto.a_name}
		                </a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_notice_form?infoNo=${data.bn_category_no}bn_no=${data.bn_no}" class="table_info">
		                	${setFormatDate(data.bn_reg_date)}
		                </a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_notice_form?infoNo=${data.bn_category_no}bn_no=${data.bn_no}" class="table_info">
		                	${data.bn_view_cnt}
		                </a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/account/list/get_admin_list':  // 관리자 계정 리스트 테이블
		case '/account/list/search_admin_list': // 관리자 계정 검색 리스트 테이블
			regDate = new Date(new Date(data.a_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <p class="table_info">${listIndex}</p>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.a_id}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_name}</a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info">${data.a_phone}</a>
		            </td>
		            <td class="va_m">
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="flex_area">
		                	<span class="state icon ${data.a_authority_role === 'SUB_ADMIN' ? '' : 'off'}">
		                		${data.a_authority_role === 'SUB_ADMIN' ? '승인' : '대기'}
		                	</span>
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.a_no}" class="table_info">${setFormatDate(data.a_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/user_account/info/get_user_account_list':  // 회원 관리 리스트 테이블
		case '/user_account/info/search_user_account_list': // 회원 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.u_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <p class="table_info">${listIndex}</p>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.u_id}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.u_name}</p>
		                	<span class="divider"></span>
		                	<p class="info_text">${data.u_nickname}</p>
		                </a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="table_info">${data.u_phone}</a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="table_info">${data.u_company || '-'} </a>
		            </td>
		            <td class="va_m">
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="flex_area">
		                	<span class="state ${data.u_is_blocked === true ? '' : 'off'}">
		                		${data.u_is_blocked === true ? '정상' : '정지'}
		                	</span>
		                </a>
		            </td>
		            <td>
		                <a href="/user_account/info/modify_form?u_no=${data.u_no}" class="table_info">${setFormatDate(data.u_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/disease/cate_info/get_category_list': // 질환/질병 분류 관리 리스트 테이블
		case '/disease/cate_info/search_category_list': // 질환/질병 분류 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.dc_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.dc_name}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/disease/info/disease_list_form?sortType=2&infoNo=${data.dc_no}&sortValue=d_no&order=desc" class="table_info">${data.dc_item_cnt}</a>
		            </td>
		            <td>
		                <a href="/disease/cate_info/modify_category_form?dc_no=${data.dc_no}" class="table_info">${setFormatDate(data.dc_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/disease/info/get_disease_list': // 질환/질병 정보 관리 리스트 테이블
		case '/disease/info/search_disease_list': // 질환/질병 정보 관리 검색 리스트 테이블
		case '/disease/info/get_disease_list_by_category': // 질환/질병 정보 관리 질병군별 분류 리스트 테이블
			regDate = new Date(new Date(data.d_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td class="va_m">
		                <div class="flex_area"><input type="checkbox" name="d_no" value="${data.d_no}"></div>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${data.diseaseCategoryDto.dc_name}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.d_name}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${setFormatDate(data.d_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/disease/info/modify_form?d_no=${data.d_no}" class="table_info">${setFormatDate(data.d_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/recipe/info/get_recipe_list': // 식단 정보 관리 리스트 테이블
		case '/recipe/info/search_recipe_list': // 식단 정보 관리 검색 리스트 테이블
		case '/recipe/info/get_recipe_list_by_type': // 식단 정보 관리 음식 종류별 분류 리스트 테이블
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_pat2}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_nm}</a>
		            </td>
		            <td>
		                <a href="/recipe/info/detail_form?rcp_seq=${data.rcp_seq}" class="table_info">${data.rcp_way2}</a>
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
			regDate = new Date(new Date(data.v_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td class="va_m">
		                <div class="flex_area"><input type="checkbox" name="v_no" value="${data.v_no}"></div>
		            </td>
		            <td>
		                <a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		            	<a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.v_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td class="ta_l">
		                <a href="${data.v_link}" onclick="setWindowOpenPosition(this.href, 640, 360); return false;" class="table_info">${data.v_link}</a>
		            </td>
		            <td>
		                <a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info">${setFormatDate(data.v_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/video/info/modify_form?v_no=${data.v_no}" class="table_info">${setFormatDate(data.v_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/notice/info/get_notice_list': // 전체 공지 사항 리스트 테이블
		case '/notice/info/search_notice_list': // 전체 공지 사항 검색 리스트 테이블
			regDate = new Date(new Date(data.n_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.n_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">${data.adminAccountDto.a_id}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${data.n_view_cnt}</a>
		            </td>
		            <td class="va_m">
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="flex_area">
		                	<span class="state ${data.n_state === true ? '' : 'off'}">
		                		${data.n_state === true ? '공개' : '숨김'}
		                	</span>
		                </a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${setFormatDate(data.n_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/notice/info/modify_form?n_no=${data.n_no}" class="table_info">${setFormatDate(data.n_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/noti_info/get_notice_list': // 질문과 답변 공지 사항 리스트 테이블
		case '/qna/noti_info/search_notice_list': // 질문과 답변 공지 사항 검색 리스트 테이블
			regDate = new Date(new Date(data.bqn_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bqn_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/account/list/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">${data.adminAccountDto.a_name}</a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${data.bqn_view_cnt}</a>
		            </td>
		            <td class="va_m">
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="flex_area">
		                	<span class="state ${data.bqn_state === true ? '' : 'off'}">
		                		${data.bqn_state === true ? '공개' : '숨김'}
		                	</span>
		                </a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${setFormatDate(data.bqn_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/qna/noti_info/modify_notice_form?bqn_no=${data.bqn_no}" class="table_info">${setFormatDate(data.bqn_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
		
		case '/board/noti_info/get_notice_list': // 공지 게시물 리스트 테이블
		case '/board/info/search_notice_list': // 공지 게시물 검색 리스트 테이블
			regDate = new Date(new Date(data.bn_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">게시판명</a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bn_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
					<td>
		                <a href="/account/noti_info/admin_modify_form?a_no=${data.adminAccountDto.a_no}" class="table_info">${data.adminAccountDto.a_id}</a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">
		                	${data.bn_view_cnt}
		                </a>
		            </td>
					<td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">
							<span onclick="" class="btns small ${data.bn_state === 1 ? '' : 'white'}">${data.bn_state === 1 ? '사용' : '숨김'}</span>
						</a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">
		                	${setFormatDate(data.bn_reg_date)}
		                </a>
		            </td>
		            <td>
		                <a href="/board/noti_info/modify_board_notice_form?infoNo=${data.bn_category_no}&bn_no=${data.bn_no}" class="table_info">
		                	${setFormatDate(data.bn_mod_date)}
		                </a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/cate_info/get_category_list': // 게시판 관리 리스트 테이블
		case '/board/cate_info/search_category_list': // 게시판 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.bc_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr data-no-name="bc_no" data-no="${data.bc_no}" data-idx="${data.bc_idx}">
					<td class="va_m">
						<div class="flex_area">
							${getListCnt > 1 ? `
								${data.bc_idx !== 1 ? `<span onclick="putOrderModify(event, ${data.bc_idx - 1}, ${page})" class="func_arrow up"></span>` : ''}
								${data.bc_idx !== getListCnt ? `<span onclick="putOrderModify(event, ${data.bc_idx + 1}, ${page})" class="func_arrow down"></span>` : ''}
							` : ''}
						</div>
					</td>
		            <td>
		                <a href="/board/cate_info/modify_category_form?bc_no=${data.bc_no}" class="table_info">${data.bc_idx}</a>
		            </td>
		            <td>
		                <a href="/board/cate_info/modify_category_form?bc_no=${data.bc_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.bc_name}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/board/info/posts_list_form?infoNo=${data.bc_no}" class="table_info">${data.bc_item_cnt}</a>
		            </td>
		            <td>
		                <a href="/board/info/posts_list_form?infoNo=${data.bc_no}" class="table_info">${setFormatDate(data.bc_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/board/info/get_posts_list': // 일반 게시물 리스트 테이블
		case '/board/info/search_posts_list': // 일반 게시물 검색 리스트 테이블
			regDate = new Date(new Date(data.bp_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
					<td class="va_m">
		                <div class="flex_area"><input type="checkbox" name="bp_no" value="${data.bp_no}"></div>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bp_title}</p>
		                	${data.bp_reply_cnt > 0 ? `<span class="info_num">(${data.bp_reply_cnt})</span>` : ''}
							${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
	                	</a>
		            </td>
					<td>
						${data.bp_account === 'admin' ?
							`<a href="/account/list/admin_modify_form?a_no=${data.bp_writer_no}" class="table_info table_flex_info f_jc_center">
								<p class="info_text">${data.adminAccountDto.a_name}</p>
								<img src="/image/icons/manager.png" alt="관리자" class="table_info_icons">
							</a>`
						:
							`<a href="/user_account/info/modify_form?u_no=${data.bp_writer_no}" class="table_info">${data.userAccountDto.u_name}</a>`
						}
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${data.bp_view_cnt}</a>
		            </td>
		            <td class="va_m">
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="flex_area">
		                	<span class="state ${data.bp_report_state === 2 ? 'on' : data.bp_report_state === 0 ? 'off' :  ''}">
		                		${data.bp_report_state === 2 ? '처리중' : data.bp_report_state === 0 ? '처리완료' : '정상'}
		                	</span>
		                </a>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${setFormatDate(data.bp_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/board/info/modify_form?infoNo=${data.bp_category_no}&bp_no=${data.bp_no}" class="table_info">${setFormatDate(data.bp_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/cate_info/get_category_list': // 질문 유형 분류 관리 리스트 테이블
		case '/qna/cate_info/search_category_list': // 질문 유형 분류 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.bqc_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/qna/cate_info/modify_category_form?bqc_no=${data.bqc_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/qna/cate_info/modify_category_form?bqc_no=${data.bqc_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.bqc_name}</p>
							${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/qna/info/qna_list_form?sortType=2&infoNo=${data.bqc_no}&sortValue=bq_answer_no&order=asc" class="table_info">
		                	${data.bqc_item_cnt} / ${data.bqc_unanswered_cnt}
		                </a>
		            </td>
		            <td>
		                <a href="/qna/cate_info/modify_category_form?bqc_no=${data.bqc_no}" class="table_info">${setFormatDate(data.bqc_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/qna/info/get_qna_list': // 질문과 답변 리스트 테이블
		case '/qna/info/search_qna_list': // 질문과 답변 검색 리스트 테이블
		case '/qna/info/get_qna_list_by_category': // 질문 유형별 분류 리스트 테이블
			regDate = new Date(new Date(data.bq_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${data.qnaCategoryDto.bqc_name}</a>
		            </td>
		            <td class="va_m">
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="flex_area">
		                	<span class="state icon ${data.qnaAnswerDto === null ? 'off' : ''}">
		                		${data.qnaAnswerDto === null ? '대기' : '답변'}
		                	</span>
		                </a>
		            </td>
					<td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.bq_title}</p>
		                	${data.bq_state === true ? '<img src="/image/icons/lock.png" alt="비공개글" class="table_info_icons">' : ''}
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
					<td>
		                <a href="/user_account/info/modify_form?u_no=${data.userAccountDto.u_no}" class="table_info">${data.userAccountDto.u_name}</a>
		            </td>
					<td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${setFormatDate(data.bq_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/qna/info/answer_form?bq_no=${data.bq_no}" class="table_info">${setFormatDate(data.bq_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/report/cate_info/get_category_list': // 신고 유형 분류 관리 리스트 테이블
		case '/report/cate_info/search_category_list': // 신고 유형 분류 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.brc_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/report/cate_info/modify_category_form?brc_no=${data.brc_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/report/cate_info/modify_category_form?brc_no=${data.brc_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.brc_name}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/report/info/report_list_form?sortType=2&infoNo=${data.brc_no}&sortValue=br_state&order=asc" class="table_info">
		                	${data.brc_item_cnt} / ${data.brc_unresulted_cnt}
		                </a>
		            </td>
		            <td>
		                <a href="/report/cate_info/modify_category_form?brc_no=${data.brc_no}" class="table_info">${setFormatDate(data.brc_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/report/info/get_report_list': // 신고 관리 리스트 테이블
		case '/report/info/search_report_list': // 신고 관리 검색 리스트 테이블
		case '/report/info/get_report_list_by_category': // 신고 유형별 분류 리스트 테이블
			regDate = new Date(new Date(data.br_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="table_info">${data.reportCategoryDto.brc_name}</a>
		            </td>
		            <td class="va_m">
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="flex_area">
		                	<span class="state icon ${data.br_state === 1 ? 'off' : ''}">
		                		${data.br_state === 1 ? '대기' : '처리완료'}
		                	</span>
		                </a>
		            </td>
					<td>
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="table_info table_flex_info">
		                	<p class="info_text">${data.br_title}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
					<td>
		                <a href="/user_account/info/modify_form?u_no=${data.userAccountDto.u_no}" class="table_info">${data.userAccountDto.u_name}</a>
		            </td>
					<td>
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="table_info">${setFormatDate(data.br_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/report/info/detail_form?br_no=${data.br_no}" class="table_info">${setFormatDate(data.br_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/advertisement/cate_info/get_category_list': // 광고 분류 관리 리스트 테이블
		case '/advertisement/cate_info/search_category_list': // 광고 분류 관리 검색 리스트 테이블
			regDate = new Date(new Date(data.ac_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/advertisement/cate_info/modify_category_form?infoNo=${data.ac_no}&sortType=2" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/advertisement/cate_info/modify_category_form?infoNo=${data.ac_no}&sortType=2" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.ac_name}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td>
		                <a href="/advertisement/info/advertisement_list_form?sortType=2&sortValue=ad_idx&order=asc&infoNo=${data.ac_no}" class="table_info">${data.ac_item_cnt}</a>
		            </td>
		            <td>
		                <a href="/advertisement/cate_info/modify_category_form?infoNo=${data.ac_no}&sortType=2" class="table_info">
		                	${data.ac_note ? data.ac_note : '-'}
		                </a>
		            </td>
		            <td>
		                <a href="/advertisement/cate_info/modify_category_form?infoNo=${data.ac_no}&sortType=2" class="table_info">${setFormatDate(data.ac_reg_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/advertisement/info/get_advertisement_list': // 광고 관리 리스트 테이블
		case '/advertisement/info/search_advertisement_list': // 광고 관리 검색 리스트 테이블
		case '/advertisement/info/get_advertisement_list_by_category': // 광고 관리 위치별 분류 리스트 테이블
			regDate = new Date(new Date(data.ad_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${listIndex}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${data.advertisementCategoryDto.ac_name}</a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.ad_client}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
		            <td class="va_m">
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="flex_area">
		                	<span class="state ${data.ad_state === 1 ? '' : 'off'}">
		                		${data.ad_state === 1 ? '사용' : '만료'}
		                	</span>
		                </a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_start_date)}</a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_end_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_mod_date)}</a>
		            </td>
		        </tr>
			`;
			break;
			
		case '/advertisement/cate_info/get_advertisement_list_by_category': // 광고 분류 상세페이지 내 위치별 분류 리스트 테이블	
			regDate = new Date(new Date(data.ad_reg_date).getTime() + newIconsHours);
			tableTrContent = `
				<tr data-no-name="ad_no" data-no="${data.ad_no}" data-idx="${data.ad_idx}">
					<td class="va_m">
						<div class="flex_area">
							${getListCnt > 1 ? `
								${data.ad_idx !== 1 ? `<span onclick="putOrderModify(event, ${data.ad_idx - 1}, ${page})" class="func_arrow up"></span>` : ''}
								${data.ad_idx !== getListCnt ? `<span onclick="putOrderModify(event, ${data.ad_idx + 1}, ${page})" class="func_arrow down"></span>` : ''}
							` : ''}
						</div>
					</td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${data.ad_idx}</a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info table_flex_info f_jc_center">
		                	<p class="info_text">${data.ad_client}</p>
		                	${nowDate <= regDate ? '<img src="/image/icons/new.png" alt="새글" class="table_info_icons">' : ''}
		                </a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_start_date)}</a>
		            </td>
					<td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_end_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_reg_date)}</a>
		            </td>
		            <td>
		                <a href="/advertisement/info/modify_form?ad_no=${data.ad_no}" class="table_info">${setFormatDate(data.ad_mod_date)}</a>
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
function generatePagination(apiUrl, sortValue, order, pagingValues, isSearch) { // apiUrl, sortValue, order, 페이징벨류값, isSearch
	const blockLimit = pagingValues.blockLimit; // 한 블럭에 포함되는 페이지 수
	const startPage = pagingValues.startPage; // 현재 블럭의 시작 페이지
	const endPage = pagingValues.endPage; // 현재 블럭의 마지막 페이지
	const currentPage = pagingValues.page; // 현재 페이지
	const maxPage = pagingValues.maxPage; // 마지막 페이지
	const totalBlocks = Math.ceil(maxPage / blockLimit); // 전체 블록 수
	const currentBlock = Math.ceil(currentPage / blockLimit); // 현재 블록
	
	// 함수명, 인자 생성
	const handlerFunction = isSearch ? 'getSearchList' : 'getList';
	const args = `${isSearch ? 'null, ' : ''}'${apiUrl}', '${sortValue || ''}', '${order || ''}'`;
	
	let paging = '';
	
	if(totalBlocks > 1 && currentBlock > 1) { // 블럭이 1개 이상일 경우 2번째 블럭 부터 노출
		paging += `
			<div onclick="${handlerFunction}(${args}, 1)" class="first func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="10,5 10,0 0,10 10,20 10,15 5,10 	"/>
	             <polygon points="15,5 10,10 15,15 20,20 20,0 	"/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${args}, ${startPage - 1})" class="prev func_icon">
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
			paging += `<div class="num" onclick="${handlerFunction}(${args}, ${i})">${i}</div>`;
		}
	}
	
	if(totalBlocks > 1 && currentBlock < totalBlocks) { // 마지막 전 블럭까지 노출
		paging += `
			<div onclick="${handlerFunction}(${args}, ${endPage + 1})" class="next func_icon">
	            <svg aria-label="first" class="fill" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" enable-background="new 0 0 20 20">
	                <polygon points="15,0 5,10 15,20 "/>
	            </svg>
	        </div>
	        
	        <div onclick="${handlerFunction}(${args}, ${maxPage})" class="last func_icon">
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
    const sortBtn = event.currentTarget.closest('.sort'); // 클릭된 요소가 가장 가까운 부모 요소 중 클래스가 sort인 요소를 찾음
	if(!sortBtn) return; // 만약 sort 요소가 없다면 아무 작업도 하지 않음
	
    const currentSortValue = sortBtn.getAttribute('data-current-sort-value'); // 현재 정렬 값 가져오기 default all
    const order = currentSortValue === 'all' ? 'desc' : currentSortValue === 'desc' ? 'asc' : 'desc'; // 정렬 값 토글
    sortBtn.setAttribute('data-current-sort-value', order); // 버튼의 data-sort-value 속성 값 업데이트
	
	const urlParams = new URLSearchParams(window.location.search);
	const sortType = urlParams.get('sortType') || 0; // 0 = 기본값, 1 = 검색, 2 = 카테고리선택
    const apiUrl = mapSortListApiObject(dbTable, sortType); // 커맨드 가져오기
    
	if(sortType === '1') { // 검색 리스트인 경우
		logger.info(`getSearchList() aipUrl: ${apiUrl} sortType: ${sortType}`);
		getSearchList(null, apiUrl, sortValue, order, 1); // 변경된 정렬 값으로 getSearchList 호출	
		
	} else {
		logger.info(`getList() aipUrl: ${apiUrl} sortType: ${sortType}`);
	    getList(apiUrl, sortValue, order, 1); // 변경된 정렬 값으로 getList 호출		
	}
}

// sortType에 따른 sort getList() 요청 커맨드 설정
function mapSortListApiObject(dbTable, sortType) {
	// dbTable과 sortType 조합에 따른 커맨드 매핑
	const apiUrlMap = { // 0 = 기본값, 1 = 검색, 2 = 카테고리선택
		'admin_account': { // 관리자 계정 관리 페이지
			0: '/account/list/get_admin_list',
			1: '/account/list/search_admin_list',
		},
		'user_account': { // 회원 관리 페이지
			0: '/user_account/info/get_user_account_list',
			1: '/user_account/info/search_user_account_list',
		},
		'disease_category': { // 질환/질병 분류 관리 페이지
			0: '/disease/cate_info/get_category_list',
			1: '/disease/cate_info/search_category_list',
		},
		'disease': { // 질환/질병 정보 관리 페이지
			0: '/disease/info/get_disease_list',
			1: '/disease/info/search_disease_list',
			2: '/disease/info/get_disease_list_by_category',
		},
		'recipe': { // 식단 정보 관리 페이지
			0: '/recipe/info/get_recipe_list',
			1: '/recipe/info/search_recipe_list',
			2: '/recipe/info/get_recipe_list_by_type',
		},
		'video': { // 영상 정보 관리 페이지
			0: '/video/info/get_video_list',
			1: '/video/info/search_video_list',
		},
		'board_qna_notice': { // 질문과 답변 공지 사항 페이지
			0: '/qna/noti_info/get_notice_list',
			1: '/qna/noti_info/search_notice_list',
		},
		'board_qna_category': { // 질문 유형 분류 관리 페이지
			0: '/qna/cate_info/get_category_list',
			1: '/qna/cate_info/search_category_list',
		},
		'board_qna': { // 질문과 답변 페이지
			0: '/qna/info/get_qna_list',
			1: '/qna/info/search_qna_list',
			2: '/qna/info/get_qna_list_by_category',
		},
		'board_notice': { // 게시판 공지 사항 페이지
			0: '/board/noti_info/get_notice_list',
			1: '/board/noti_info/search_notice_list',
			2: '/board/noti_info/get_notice_list_by_category',
		},
		'board_category': { // 게시판 관리 페이지
			0: '/board/cate_info/get_category_list',
			1: '/board/cate_info/search_category_list',
		},
		'board_posts': { // 특정 게시판 페이지
			0: '/board/info/get_posts_list',
			1: '/board/info/search_posts_list',
		},
		'board_report_category': { // 신고 유형 분류 관리 페이지
			0: '/report/cate_info/get_category_list',
			1: '/report/cate_info/search_category_list',
		},
		'board_report': { // 신고 관리 페이지
			0: '/report/info/get_report_list',
			1: '/report/info/search_report_list',
			2: '/report/info/get_report_list_by_category',
		},
		'advertisement_category': { // 광고 위치 분류 관리 페이지
			0: '/advertisement/cate_info/get_category_list',
			1: '/advertisement/cate_info/search_category_list',
		},
		'advertisement': { // 광고 관리 페이지
			0: '/advertisement/info/get_advertisement_list',
			1: '/advertisement/info/search_advertisement_list',
			2: '/advertisement/info/get_advertisement_list_by_category',
		},
	};
	
	if(!apiUrlMap[dbTable] || !apiUrlMap[dbTable][sortType]) { // 해당 조합이 없을 경우 에러
		throw new Error(`Invalid dbTable = "${dbTable}" or sortType = "${sortType}"`);
	}
	
	return apiUrlMap[dbTable][sortType];
}

// 선택된 카테고리의 리스트 요청
function getSelectList(event) {
	const selectOption = event.target; // 클릭된 버튼 요소
	const sortValue = selectOption.parentElement.getAttribute('data-sort-value'); // 정렬 기준 값 가져오기
	const infoNo = selectOption.getAttribute('data-info-no'); // 분류 값 가져오기
	const apiUrl = mapSelectListApiObject(sortValue); // 커맨드 가져오기
	
	const urlParams = new URLSearchParams(window.location.search);
	urlParams.set('sortType', 2); // 1 = 검색, 2 = 카테고리선택
	urlParams.set('infoNo', infoNo); // 분류 값
	const newUrl = `${window.location.pathname}?${urlParams.toString()}`;
	window.history.replaceState({}, '', newUrl);
	
	getList(apiUrl, sortValue, 'desc', 1); // 셀렉트 기본 정렬 값은 desc
}

// select getList() 요청에 필요한 객체 설정
function mapSelectListApiObject(sortValue) {
	let apiUrl = null;
	
	switch(sortValue) {				
		case 'd_no': // 질환/질병 정보 리스트 페이지 질병군별 분류 리스트 요청
			apiUrl = '/disease/info/get_disease_list_by_category';
			break;
			
		case 'rcp_pat2': // 식단 정보 리스트 페이지 음식 종류별 분류 리스트 요청
			apiUrl = '/recipe/info/get_recipe_list_by_type';
			break;
			
		case 'bq_no': // QnA 질문 유형별 분류 리스트 요청
			apiUrl = '/qna/info/get_qna_list_by_category';
			break;
			
		case 'br_no': // 신고 유형별 분류 리스트 요청
			apiUrl = '/report/info/get_report_list_by_category';
			break;
		
		case 'ad_no': // 광고 관리 리스트 페이지 위치별 분류 리스트 요청
			apiUrl = '/advertisement/info/get_advertisement_list_by_category';
			break;
			
		default:
			logger.error('mapSelectListApiObject() sortValue:', value);
			return false;
	}
	
	return apiUrl;
}

// 셀렉트 옵션 리스트 요청 및 옵션 생성
async function getCategoryList(ele, formName, selectedValue, ) {
	const $selectEle = $(`#${ele}`); // 셀렉트 요소가 생성될 table th or td
	const bgc = $selectEle.parent()[0].tagName === 'TH' ? '#F7F7F7' : '#FFFFFF';
	const categoryConfig = mapCategorylistObject(ele);
	
	if($selectEle.length) {
		setLoading(true, `${ele}_select`, bgc); // 로딩 추가
		try {
			const response = await $.ajax({
				url: categoryConfig.getCateSelectApiUrl,
				method: 'GET',
			});
			
			const categoryDto = response[categoryConfig.getListDtos];
			logger.info(`${categoryConfig.getCateSelectApiUrl} categoryDto:`, response);
			
			if(categoryDto && categoryDto.length) {
				if(formName) {
					categoryDto.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let selected = selectedValue ? data[categoryConfig.infoNo] === selectedValue ? 'selected' : '' : '';
						let option = `
							<option 
								${selected}
								value="${data[categoryConfig.infoNo]}" 
								${data[categoryConfig.note] ?
									`data-info="${data[categoryConfig.note]}"`
								:
									''
								}
							>
								${data[categoryConfig.infoName]}
							</option>
						`;
						
						if(selected) {
							$selectEle[0].insertAdjacentHTML('afterbegin', option);
							
							// 선택된 참고 사항 노출(처음만 적용)
							if(data[categoryConfig.note]){
								logger.info('guideline:', data[categoryConfig.note]);
								const $guideline = $('#guideline');
								$guideline.text(data[categoryConfig.note]);									
							}
							
						} else {
							$selectEle[0].insertAdjacentHTML('beforeend', option);
						}
						
						// note가 있을 경우에 onchange 이벤트 추가
						if(categoryDto.some(data => data[categoryConfig.note])) {
							$selectEle.attr("onchange", `setSelectGuidelineInfo(this, '${formName}')`);
						}
					});
					
				} else {
					const ceateSelect = `
						<ul data-sort-value="${categoryConfig.soltValue}" class="select_option_list sc"></ul>
					`;
					
			        $selectEle[0].insertAdjacentHTML('beforeend', ceateSelect);
			        
			        const $selectOptionlist = $('ul.select_option_list');
			        
					categoryDto.forEach((data) => { // 커스텀 셀렉트 옵션 항목 추가
						let option = `
							<li data-info-no="${data[categoryConfig.infoNo]}" class="option" onclick="getSelectList(event);">
								${data[categoryConfig.infoName]}
							</li>
						`;
						$selectOptionlist[0].insertAdjacentHTML('beforeend', option);
					});
				}
				
			} else {
				$selectEle.removeClass('select');
			}
			
		} catch(error) {
			logger.error(`${categoryConfig.getCateSelectApiUrl} error:`, error);
			
		} finally {
			setLoading(false, `${ele}_select`) // 로딩 제거
		}
	}
}

// 동적으로 순번 max값 적용
async function getMaxIdxAndSetAttribute(name, value, formName) {
	logger.info('getMaxIdxAndSetAttribute():', name, value, formName);
	const { getSelectMaxIdxApiUrl } = mapCategorylistObject(name); // name값에 해당하는 maxIdx값 요청 api
	if(!getSelectMaxIdxApiUrl) return; // name값에 해당하는 maxIdx값 요청 api가 없을 경우 리턴
	
	setLoading(true, 'order_number'); // 로딩 추가
	try {
		const response = await $.ajax({
			url: getSelectMaxIdxApiUrl,
			method: 'GET',
			data: {
				[name]: value,
			},
		});
		
		logger.info(`${getSelectMaxIdxApiUrl} getMaxIdxAndSetAttribute():`, response);
		
		// modify인 경우 최대값 그대로 사용 create일 경우 최대값+1, 기본값은 1
		let max = response < 1 ? 1 : response + (formName === 'create' ? 1 : 0);
		
		if(formName === 'create') $('#idx_number').val(max); // create form일 경우 최대값으로 설정
		$('#idx_number').attr('max', max); // 해당 인풋 요소에 max속성, value값 추가/변경
		
		// modify인 경우 값이 유지가 되어야하나 max값이 입력값보다 작을 경우에 대비하여 강제로 blur 이벤트 트리거
		$('#idx_number').focus().trigger('blur'); 
		
	} catch(error) {
		logger.error(`${getSelectMaxIdxApiUrl} getMaxIdxAndSetAttribute() error:`, error);
		
	} finally {
		setLoading(false, `order_number`) // 로딩 제거
	}
}

// 분류별 리스트 요청에 필요한 객체 설정
function mapCategorylistObject(ele) {
	let getCateSelectApiUrl = null; // 분류별 리스트 요청 api
	let getSelectMaxIdxApiUrl = null; // 분류 또는 분류에 속한 데이터에 순번 입력이 필요한 경우 max값 요청 api
	let getListDtos = null; // 객체명
	let infoNo = null; // 분류 no 값
	let infoName = null; // 분류 no 값을 가져오기 위한 객체명
	let soltValue = null; // 정렬 기준값
	let note = null; // 기타사항이 있을 경우 해당 객체명
	
	switch(ele) {
		case 'd_category_no': // 질병군별 분류 리스트(분류별 관리o)
			getCateSelectApiUrl = '/disease/cate_info/get_category_list_select';
			getListDtos = 'diseaseCategoryDtos';			
			infoNo = 'dc_no';
			infoName = 'dc_name';
			soltValue = 'd_no';
			break;
			
		case 'rcp_pat2': // 음식 종류별 분류 리스트(분류별 관리x)
			getCateSelectApiUrl = '/recipe/info/get_type_list_select';
			getListDtos = 'recipeTypeDtos';
			infoNo = 'rcp_pat2';
			infoName = 'rcp_pat2';
			soltValue = 'rcp_pat2';
			break;
			
		case 'bq_category_no': // 질문 유형별 분류 리스트(분류별 관리o)
			getCateSelectApiUrl = '/qna/cate_info/get_category_list_select';
			getListDtos = 'qnaCategoryDtos';			
			infoNo = 'bqc_no';
			infoName = 'bqc_name';
			soltValue = 'bq_no';
			break;
			
		case 'br_category_no': // 신고 유형별 분류 리스트(분류별 관리o)
			getCateSelectApiUrl = '/report/cate_info/get_category_list_select';
			getListDtos = 'reportCategoryDtos';			
			infoNo = 'brc_no';
			infoName = 'brc_name';
			soltValue = 'br_no';
			break;
			
		case 'ad_category_no': // 위치별 분류 리스트(분류별 관리o)
			getCateSelectApiUrl = '/advertisement/cate_info/get_category_list_select';
			getSelectMaxIdxApiUrl = '/advertisement/info/create_category_select';
			getListDtos = 'advertisementCategoryDtos';
			infoNo = 'ac_no';
			infoName = 'ac_name';
			soltValue = 'ad_no';
			note = 'ac_note';
			break;
		
		default:
			logger.error('mapCategorylistObject() value:', ele);
			return false;
	}
	
	return { getCateSelectApiUrl, getSelectMaxIdxApiUrl, getListDtos, infoNo, infoName, soltValue, note };
}

// 로그인 유저 데이터 요청
async function getAccountInfo(modify = false) {
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