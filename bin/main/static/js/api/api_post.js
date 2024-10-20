// 함수 디바운싱 적용 // 함수, key명
const postSignUp = debounceAsync(postSignUpProcess, 'postSignUpProcess'); // 회원 가입
const postSignIn = debounceAsync(postSignInProcess, 'postSignInProcess'); // 로그인
const postIdentityCheck = debounceAsync(postIdentityCheckProcess, 'postIdentityCheckProcess'); // 본인 확인
const postIntegSubmit = debounceAsync(postIntegSubmitProcess, 'postIntegSubmitProcess'); // post 통합 ajax 요청
const postRecipeUpdate = debounceAsync(postRecipeUpdateProcess, 'postRecipeUpdateProcess'); // 식단 정보 업데이트

// 회원 가입
async function postSignUpProcess(event, formName) {    
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	// 유효성 검사 재실행
	input = form.a_id;
	if(!(await validateEmail(input, true, true))) { // 제출 전 한번 더 중복 확인
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!validatePw(input, true)) { 
		input.focus();
		return false;
	}
	
	input = form.a_name;
	if(!validateEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_birth;
	if(!validateEmpty(input, '생년월일을', true)) {
		input.focus();
		return false;
	}
	
	input = form.a_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	if(setLoading(true, 'input_list_container')) { // 로딩 추가 함수 실행이 성공하면 요청 진행
		form.action = "/account/sign_up_confirm"; 
	    form.method = "post"; 
	    form.submit()
	}
}

// 로그인
function postSignInProcess(event, formName) {        
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_id;
	if(!validateEmpty(input, '이메일을', true, true)) { // 요소, 텍스트, alert 여부, 메세지 요소 미노출 여부
		input.focus();
		return false;
	}
	
	input = form.a_pw;
	if(!validateEmpty(input, '비밀번호를', true, true)) {
		input.focus();
		return false;
	}
	
	// 모든 유효성 검사가 통과되었을 때 폼 제출
	if(setLoading(true, 'input_list_container')) { // 로딩 추가 함수 실행이 성공하면 요청 진행
		form.action = "/account/sign_in_confirm"; 
	    form.method = "post"; 
	    form.submit();
    }
}

// 본인 확인
async function postIdentityCheckProcess(event, formName) {   
	if(setLoading(true, 'content_inner')) { // 로딩 추가 함수 실행이 성공하면 요청 진행
		if(event) event.preventDefault();
		const form = document.forms[formName];
		let input;
		
		input = form.a_pw;
		if(!validateEmpty(input, '비밀번호를', true, true)) {
			input.focus();
			setLoading(false, 'content_inner')
			return false;
		}
	
		try {
			const response = await $.ajax({
				url: '/account/info/modify_check',
				method: 'POST',
				data: {
					a_pw: input.value.trim(),
				},
			});
			
			logger.info('/account/info/modify_check postIdentityCheck() response:', response);
					
			if(response) {
				sessionStorage.setItem('loginedId', response.loginedId);
				sessionStorage.setItem('checkDate', response.checkDate);
				await getAccountInfo(true); // get_account_info 요청, account modify form set
				
			} else {
				alert('비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요.');
			}
			
		} catch(error) {
			logger.error('/account/info/modify_check postIdentityCheck() error:', error);
			alert('본인 확인 오류로 데이터를 불러오는데 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
			location.reload(true);
			
		} finally {
			setLoading(false, 'content_inner');
		}
	}
}

// post 통합 ajax 요청
async function postIntegSubmitProcess(apiUrl, formData, successMessage, errorMessage, redirectUrl = null, loddingParentEle) {
	if(setLoading(true, loddingParentEle)) { // 로딩 추가 함수 실행이 성공하면 요청 진행 
		setFormDataCheckConsoleLog(formData); // FormData 키벨류, byte 확인
		
		try {
			const response = await $.ajax({
				url: apiUrl,
				method: 'POST',
				data: formData,
				processData: false,  // FormData가 자동으로 Content-Type 설정
				contentType: false,  // FormData를 문자열로 변환하지 않음
			});
			
			logger.info(`${apiUrl} postIntegSubmit() response:`, response);
			
			if(response) {
				alert(successMessage);
				redirectUrl ? location.replace(redirectUrl) : location.reload(true);
				
			} else {
				alert(errorMessage);
				location.reload(true);
			}
			
		} catch(error) {
			logger.error(`${apiUrl} postIntegSubmit() error:`, error);
			alert(errorMessage);
			location.reload(true);
		}
	}
}

// 질환/질병 분류 등록
async function postDiseaseCategoryCreate( formName, nextPage) {
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(!(await requestDuplicateCheck(input, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	
	const successMessage = `"${input.value}" 분류가 등록되었습니다.`;
	const errorMessage = `"${input.value}" 분류 등록 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/disease/cate_info/create_category_confirm', 				// apiUrl
		formData, 													// data
		successMessage, 											// 성공 메세지
		errorMessage,												// 실패 메세지 																
		nextPage ? '/disease/cate_info/category_list_form' : null,	// 다음 페이지
		'content_inner'                                             // 로딩 표시할 부모 요소
	);
}

// 질환 / 질병 등록
async function postDiseaseCreate(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	input = form.d_good_food;
	if(!validateEmpty(input, '추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
		
	input = form.d_bad_food;
	if(!validateEmpty(input, '비추천 식단 재료를', true)) {
		input.focus();
		return false;
	}
	
	input = form.d_info;
	if(!validateEmpty(input, '질환 / 질병 정보를', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${form.d_name.value}" 질환/질병 정보가 등록되었습니다.`;
	const errorMessage = `"${form.d_name.value}" 질환 / 질병 정보 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/disease/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/disease/info/disease_list_form',
		'content_inner'
	);
}

// 식단 정보 업데이트
async function postRecipeUpdateProcess() {
	const isConfirm = confirm('업데이트는 약 10~30초 정도가 소요됩니다. 업데이트하시겠습니까?');
	if(!isConfirm) return false;
	
	if(setLoading(true, 'content_inner')) { // 로딩 추가 함수 실행이 성공하면 요청 진행 
		try {
			const response = await $.ajax({
				url: '/recipe/info/refresh_api_recipe_data',
				method: 'GET',
			});
			
			logger.info('postRecipeUpdate() response:', response);	
			
			if(response) {
				alert('최신 정보로 업데이트하였습니다.');
				
			} else {
				alert('최신 정보 업데이트에 실패하였습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
			}
			
		} catch(error) {
			logger.error('postRecipeUpdate() error:', error);
			
		} finally {
			location.reload(true);
		}
	}
}

// 영상 정보 등록
async function postVideoCreate(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.v_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
		
	input = form.v_link;
	if(!validateEmpty(input, 'URL 주소를', true)) {
		input.focus();
		return false;
	}
	
	input = form.v_text;
	if(!validateEmpty(input, '내용을', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${form.v_title.value}"\n영상 정보가 등록되었습니다.`;
	const errorMessage = `"${form.v_title.value}"\n영상 정보 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/video/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/video/info/video_list_form',
		'content_inner'
	);
}

// 공지사항 등록
async function postNoticeCreate(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	formData.set('n_body', quill.root.innerHTML);

	const successMessage = '공지사항이 등록되었습니다.';
	const errorMessage = '공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postIntegSubmit(
		'/notice/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/notice/info/notice_list_form',
		'content_inner'
	);
}

// QNA 공지사항 등록
async function postQnaNoticeCreate(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	formData.set('bqn_body', quill.root.innerHTML);
	
	const successMessage = '질문과 답변 공지사항이 등록되었습니다.';
	const errorMessage = '질문과 답변 공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postIntegSubmit(
		'/qna/info/qna_notice_create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/qna/info/qna_list_form',
		'content_inner'
	);
}

// 게시판 등록
async function postBoardCategoryCreate(formName) {
	const form = document.forms[formName];
	const bc_name = form.bc_name;
	const bc_idx = form.bc_idx;
	
	if(!(await requestDuplicateCheck(bc_name, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		bc_name.focus();
		return false;
	}
	
	const formData = new FormData(form);
	formData.set('bc_idx', replaceNumber(bc_idx)); // 문자열 제외 및 min, max 체크하여 입력값 설정
	
	const successMessage = `"${bc_name.value}" 게시판이 등록되었습니다.`;
	const errorMessage = `"${bc_name.value}" 게시판 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/board/cate_info/create_category_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/board/cate_info/category_list_form',
		'content_inner'
	);
}

// 게시판 공지 사항 등록
async function postNoticePostsCreate(formName) {
	const form = document.forms[formName];
	
	input = form.bn_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
	
	if(!validateQuill(quill)) { // 내용 유효성 및 비속어 검사
		quill.focus();
		return false;
	}
	
	const successMessage = `"${bn_title.value.trim()}" 게시판 공지 사항이 등록되었습니다.`;
	const errorMessage = `"${bn_title.value.trim()}" 게시판 공지 사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	const formData = new FormData();
	formData.set('bn_body', quill.root.innerHTML); // quill 에디터 내용
	
	// 이미지 파일 리사이즈 및 압축하여 formData에 담기
	const $imgTags = $(quill.root).find('img'); // 모든 이미지 태그 탐색
	if($imgTags.length) {
		logger.info('이미지 태그 있음');
		
		for(let img of $imgTags) {
			const blobURL= $(img)[0].src; // src 속성에 입력된 blob URL 가져오기
			const targetWidth = $(img)[0].width; // 리사이즈할 대상 이미지의 너비 가져오기
			
			// 설정된 width 크기로 리사이즈 압축 후 flle 개게로 변환하여 formData 추가
			const resizedImageFile = await resizeImage(blobURL, targetWidth);
			formData.set('files', resizedImageFile); // 리사이즈된 File객체를 formData에 추가
			URL.revokeObjectURL(blobURL); // blob URL을 브라우저 메모리에서 해제
		}
		
	} else {
		logger.info('이미지 태그 없음');
		const emptyBlob = new Blob([], { type: 'application/octet-stream' }); // 빈 Blob 생성
		formData.append('files', emptyBlob); 
	}
	
	await postIntegSubmit(
		'/board/info/create_board_notice_confirm',
		formData,
		successMessage,
		errorMessage,
		`/board/info/board_notice_list_form?infoNo=${form.bn_category_no.value}`,
		'content_inner'
	);
}

// 일반 게시물 등록
async function postPostsCreate(formName) {
	const form = document.forms[formName];
	
	input = form.bp_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
	
	if(!validateQuill(quill)) { // 내용 유효성 및 비속어 검사
		quill.focus();
		return false;
	}
	
	const successMessage = `"${bp_title.value}" 게시물이 등록되었습니다.`;
	const errorMessage = `"${bp_title.value}" 게시물 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	const formData = new FormData(form);
	formData.set('bp_body', quill.root.innerHTML); // quill 에디터 내용
	
	// 이미지 파일 리사이즈 및 압축하여 formData에 담기
	const $imgTags = $(quill.root).find('img'); // 모든 이미지 태그 탐색
	if($imgTags.length) {
		logger.info('이미지 태그 있음');
		
		for(let img of $imgTags) {
			const blobURL= $(img)[0].src; // src 속성에 입력된 blob URL 가져오기
			const targetWidth = $(img)[0].width; // 리사이즈할 대상 이미지의 너비 가져오기

			// 설정된 width 크기로 리사이즈 압축 후 flle 개게로 변환하여 formData 추가
			const resizedImageFile = await resizeImage(blobURL, targetWidth);
			formData.append('files', resizedImageFile); // 리사이즈된 File객체를 formData에 추가
			URL.revokeObjectURL(blobURL); // blob URL을 브라우저 메모리에서 해제
		}
		
	} else {
		logger.info('이미지 태그 없음');
		const emptyBlob = new Blob([], { type: 'application/octet-stream' }); // 빈 Blob 생성
		formData.set('files', emptyBlob); 
	}

	await postIntegSubmit(
		'/board/info/create_confirm',
		formData,
		successMessage,
		errorMessage,
		`/board/info/posts_list_form?infoNo=${form.bp_category_no.value}`,
		'content_inner'
	);
}

// 광고 분류 등록
async function postAdvertisementCategoryCreate(formName, nextPage) {
	const form = document.forms[formName];
	let input;
	
	input = form.ac_name;
	if(!(await requestDuplicateCheck(input, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${input.value}" 분류가 등록되었습니다.`;
	const errorMessage = `"${input.value}" 분류 등록 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/advertisement/cate_info/create_category_confirm',
		formData,
		successMessage,
		errorMessage, 																
		nextPage ? '/advertisement/cate_info/category_list_form' : null,
		'content_inner'
	);
}

// 광고 등록
async function postAdvertisementCreate(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.ad_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.ad_client;
	if(!validateEmpty(input, '클라이언트를', true)) {
		input.focus();
		return false;
	}
	
	input = form.ad_start_date;
	if(!validateEmpty(input, '시작일을', true)) {
		input.focus();
		return false;
	}
	
	input = form.ad_end_date;
	if(!validateEmpty(input, '종료일을', true)) {
		input.focus();
		return false;
	}
	
	if(new Date(form.ad_end_date.value) < new Date(form.ad_start_date.value)) {
		alert('종료일이 시작일보다 작을 수 없습니다.');
		input.focus();
		return false;
	}
	
	input = form.ad_url;
	if(!validateEmpty(input, 'URL 주소를', true)) {
		input.focus();
		return false;
	}
	
	input = form.files;
	if(!input.files.length) {
		alert('이미지 파일을 선택해 주세요.');
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);	
	const successMessage = `"${form.ad_client.value}" 님의 광고가 등록되었습니다.`;
	const errorMessage = `"${form.ad_client.value}" 님의 광고 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postIntegSubmit(
		'/advertisement/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/advertisement/info/advertisement_list_form',
		'content_inner'
	);
}
