// 회원 가입 폼
async function postSignUpForm(event, formName) {
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
	form.action = "/account/sign_up_confirm"; 
    form.method = "post"; 
    form.submit()
}

// 로그인 폼
function postSignInForm(event, formName) {
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
	form.action = "/account/sign_in_confirm"; 
    form.method = "post"; 
    form.submit();
}

// 본인 확인 폼
async function postIdentityCheckForm(event, formName) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.a_pw;
	if(!validateEmpty(input, '비밀번호를', true, true)) {
		input.focus();
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
		
		logger.info('/account/info/modify_check postIdentityCheckForm() response:', response);
				
		if(response) {
			sessionStorage.setItem('loginedId', response.loginedId);
			sessionStorage.setItem('checkDate', response.checkDate);
			await getAccountInfo(true); // get_account_info 요청, account modify form set
			
		} else {
			alert('비밀번호가 일치하지 않습니다. 확인 후 다시 시도해 주세요.');
			return false;
		}
		
	} catch(error) {
		logger.error('/account/info/modify_check postIdentityCheckForm() error:', error);
		alert('본인 확인 오류로 데이터를 불러오는데 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.');
		location.replace('/account/info/modify_form');
	}
}

// 게시물 등록 폼
async function postPostsCreateForm(formName) {
	const form = document.forms[formName];
	
	input = form.title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
	
	if(!validateQuill(quill)) { // 내용 유효성 및 비속어 검사
		quill.focus();
		return false;
	}
	
	const notice = form.notice.value; // 1=공지, 0=일반
	const prefix = notice == 1 ? 'bn_' : 'bp_';
	const apiUrl = notice == 1 ? '/board/info/create_notice_confirm' : '/board/info/create_confirm';
	const successMessage = `"${title.value.trim()}" ${notice == 1 ? '공지' : '일반'} 게시물이 등록되었습니다.`;
	const errorMessage = `"${title.value.trim()}" ${notice == 1 ? '공지' : '일반'} 게시물 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	const $imgTags = $(quill.root).find('img'); // 모든 이미지 태그 탐색
	let data;
	if($imgTags.length > 0) {
		data = new FormData();
		data.append(`${prefix}title`, input.value.trim()); // 제목
		data.append(`${prefix}category_no`, form.category_no.value); // 게시판 no
		data.append(`${prefix}writer_no`, form.writer_no.value); // 작성자 no
		data.append(`${prefix}body`, quill.root.innerHTML); // quill 에디터 내용
	
		logger.info('이미지 태그 있음');
		
		for(let img of $imgTags) {
			const src= $(img)[0].src; // src 속성에 입력된 base64 Url 가져오기
			const base64Data = src.split(',')[1]; // base64 데이터 부분 추출
			const byteCharacters = atob(base64Data); // base64를 디코딩 반대 메소드 btoa() 
			const byteNumbers = new Array(byteCharacters.length);
			
			for(let i = 0; i < byteCharacters.length; i++) {
				byteNumbers[i] = byteCharacters.charCodeAt(i); // 각 문자를 바이트 배열로 변환
			}
			
			const byteArray = new Uint8Array(byteNumbers); // 8비트 부호 없는 정수(0-255) 저장 배열
			const mimeType = 'image/webp'; // src.match(/data:(.*?);base64/)[1]; // MIME 타입 추출
			const blob = new Blob([byteArray], {type: mimeType}); // 이진 데이터로 blob 객체로 변환	
			
			// 설정된 width 크기로 리사이즈 압축 후 flle 객체로 변환
			const resizedImageFile = await resizeImage(blob, $(img)[0].width, mimeType);
			data.append('files', resizedImageFile); // 리사이즈된 File객체를 formData에 추가
		}
		
	} else {
		data = {
			[`${prefix}title`]: input.value.trim(), // 제목
			[`${prefix}category_no`]: form.category_no.value, // 게시판 no
			[`${prefix}writer_no`]: form.writer_no.value, // 작성자 no
			[`${prefix}body`]: quill.root.innerHTML, // quill 에디터 내용
		}
	}
	
	logger.info('postPostsCreateForm() data:', data);
	
	//const isTrue = false;
	//if(!isTrue) return false;
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: $imgTags.length > 0 ? data : JSON.stringify(data),
			contentType: $imgTags.length > 0 ? false : 'application/json', // 이미지가 있는 경우 contentType false
			processData: $imgTags.length > 0,  // FormData가 아닌 경우
		});
		
		logger.info('postPostsCreateForm() response:', response);
		
		if(response) {
			alert(successMessage);
			location.replace('/board/info/posts_list_form')
			
		} else {
			alert(errorMessage);
			location.reload(true);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} postPostsCreateForm() form submit error:`, error);
		alert(errorMessage);
		location.reload(true);
	}
}

// post ajax 요청
async function postSubmitForm(apiUrl, formData, successMessage, errorMessage, redirectUrl = null) {
	for (const [key, value] of formData.entries()) {
		logger.info('postSubmitForm() formData:', key, value);
	};
	
	try {
		const response = await $.ajax({
			url: apiUrl,
			method: 'POST',
			data: formData,
			processData: false,  // FormData가 자동으로 Content-Type 설정
			contentType: false,  // FormData를 문자열로 변환하지 않음
		});
		
		logger.info(`${apiUrl} postSubmitForm() response:`, response);
		
		if(response) {
			alert(successMessage);
			redirectUrl ? location.replace(redirectUrl) : location.reload(true);
			
		} else {
			alert(errorMessage);
			location.reload(true);
		}
		
	} catch(error) {
		logger.error(`${apiUrl} postSubmitForm() error:`, error);
		alert(errorMessage);
		location.reload(true);
	}
}

// 질환/질병 분류 등록 폼
async function postDiseaseCategoryCreateForm(event, formName, nextPage) {
	if(event) event.preventDefault();
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(!(await requestDuplicateCheck(input, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	const formData = new FormData();
	formData.append('dc_name', input.value.trim());
	
	const successMessage = `"${input.value}" 분류가 등록되었습니다.`;
	const errorMessage = `"${input.value}" 분류 등록 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postSubmitForm(
		'/disease/cate_info/create_category_confirm', 				// apiUrl
		formData, 													// data
		successMessage, 											// 성공 메세지
		errorMessage,												// 실패 메세지 																
		nextPage ? '/disease/cate_info/category_list_form' : null	// 다음 페이지
	);
}

// 질환 / 질병 등록 폼
async function postDiseaseCreateForm(formName) {
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
	
	await postSubmitForm(
		'/disease/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/disease/info/disease_list_form'
	);
}

// 영상 정보 등록 폼
async function postVideoCreateForm(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.v_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
		
	input = form.v_link;
	if(!validateVideo(input, true)) {
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
	
	await postSubmitForm(
		'/video/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/video/info/video_list_form'
	);
}

// 게시판 등록 폼
async function postBoardCategoryCreateForm(formName) {
	const form = document.forms[formName];
	const bc_name = form.bc_name;
	const bc_idx = form.bc_idx;
	
	if(!(await requestDuplicateCheck(bc_name, true, false, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		bc_name.focus();
		return false;
	}
	
	const formData = new FormData();
	formData.append('bc_name', bc_name.value.trim());
	formData.append('bc_idx', replaceNumber(bc_idx)); // 문자열 제외 및 min, max 체크하여 입력값 설정
	
	const successMessage = `"${bc_name.value}" 게시판이 등록되었습니다.`;
	const errorMessage = `"${bc_name.value}" 게시판 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await postSubmitForm(
		'/board/cate_info/create_category_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/board/cate_info/category_list_form'
	);
}

// 공지사항 등록 폼
async function postNoticeCreateForm(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	formData.append('n_body', quill.root.innerHTML);

	const successMessage = '공지사항이 등록되었습니다.';
	const errorMessage = '공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postSubmitForm(
		'/notice/info/create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/notice/info/notice_list_form'
	);
}

// QNA 공지사항 등록 폼
async function postQnaNoticeCreateForm(formName) {
	const form = document.forms[formName];

	const formData = new FormData(form);
	formData.append('bqn_body', quill.root.innerHTML);
	
	const successMessage = '질문과 답변 공지사항이 등록되었습니다.';
	const errorMessage = '질문과 답변 공지사항 등록에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
	await postSubmitForm(
		'/qna/info/qna_notice_create_confirm', 
		formData, 
		successMessage, 
		errorMessage, 
		'/qna/info/qna_list_form'
	);
}

// 식단 정보 업데이트
async function postRecipeUpdate() {
	const isConfirm = confirm('업데이트는 약 10~30초 정도가 소요됩니다. 업데이트하시겠습니까?');
	if(!isConfirm) return false;
	
    setAddLoading(true); // 로딩 시작
	
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