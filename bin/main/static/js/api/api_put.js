// 함수 디바운싱 적용 // 함수, key명
const putIntegSubmit = debounceAsync(putIntegSubmitProcess, 'putIntegSubmitProcess'); // put 통합 ajax 요청

// put 통합 ajax 요청
async function putIntegSubmitProcess(apiUrl, formData, successMessage, errorMessage, loddingParentEle) {   
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
			
			logger.info(`${apiUrl} putIntegSubmit() response:`, response);
			
			if(response) {
				if(successMessage) alert(successMessage);
				
			} else {
				if(errorMessage) alert(errorMessage);
			}
			
		} catch(error) {
			logger.error(`${apiUrl} putIntegSubmit() error:`, error);
			if(errorMessage) alert(errorMessage);
			
		} finally {
			location.reload(true);
		}
	}
}

// 본인 계정 정보 수정
async function putMyAccountSubmit(formName) {
	const form = document.forms[formName];
	let input;
	
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
	
	input = form.a_pw;
	if(input.value.trim().length) {
		const isConfirm = confirm('비밀번호를 변경하시겠습니까?\n변경하지 않을 경우 입력한 값을 삭제 후 다시 저장해 주세요.');
		if(!isConfirm) return false;	
		
		if(!validatePw(input, true)) { 
			input.focus();
			return false;
		}
	}
	
	input = form.a_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(); // 비동기로 추가된 html로 폼 요소들이 DOM에 제대로 반영되지 않을 수 있어 append로 삽입
	formData.append('a_no', form.a_no.value);
	formData.append('a_id', form.a_id.value);
	formData.append('a_pw', form.a_pw.value);
	formData.append('a_name', form.a_name.value);
	formData.append('a_birth', form.a_birth.value);
	formData.append('a_phone', form.a_phone.value);
	
	const successMessage = '정보가 수정되었습니다';
	const errorMessage = '정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	logger.info('putMyAccountSubmit formData:', formData);
	
	await putIntegSubmit(
		'/account/info/modify_confirm', // apiUrl
		formData, 						// data
		successMessage, 				// 성공 메세지
		errorMessage,					// 실패 메세지
		'content_inner'                 // 로딩 표시할 부모 요소
	);
}

// 관리자 계정 정보 수정(SUPER_ADMIN)
async function putAdminModify(formName) {
	const form = document.forms[formName];
	let input;
	
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
	
	const formData = new FormData(form);
	const successMessage = `"${form.a_id.value}" 정보가 수정되었습니다`;
	const errorMessage = `"${form.a_id.value}" 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/account/list/admin_modify_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 관리자 계정 비밀번호 초기화
async function putResetPassword(a_no, a_id) {
	const isConfirm = confirm(`${a_id} 계정 비밀번호를 초기화하시겠습니까?`);
	if(!isConfirm) return false;	
		
	const formData = new FormData(); // 비동기로 추가된 html로 폼 요소들이 DOM에 제대로 반영되지 않을 수 있어 append로 삽입
	formData.append('a_no', a_no);
	
	const successMessage = `"${a_id}" 비밀번호가 초기화되었습니다.`;
	const errorMessage = `"${a_id}" 비밀번호 초기화에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await putIntegSubmit(
		'/account/list/reset_password', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 회원 정보 수정
async function putUserAccountModify(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.u_name;
	if(!validateEmpty(input, '이름을', true)) {
		input.focus();
		return false;
	}
	
	input = form.u_nickname;
	if(!validateEmpty(input, '닉네임을', true)) {
		input.focus();
		return false;
	}
	
	input = form.u_birth;
	if(!validateEmpty(input, '생년월일을', true)) {
		input.focus();
		return false;
	}
	
	input = form.u_phone;
	if(!validatePhone(input, true)) {
		input.focus();
		return false;
	}
	
	input = form.u_company;
	if(!form.u_is_personal.value && !validateEmpty(input, '소속기관을', true)) {
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);	
	const successMessage = `"${form.u_id.value}" 계정 정보가 수정되었습니다`;
	const errorMessage = `"${form.u_id.value}" 계정 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/user_account/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 회원 계정 상태 수정
async function putUserAccountBlockModify(formName) {
	const form = document.forms[formName];
	let input;
	
}

// 질환 / 질병 분류 수정
async function putDiseaseCategoryModify(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.dc_name;
	if(input.value !== form.current_dc_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			input.focus();
			return false;
		}
		
	} else {
		alert('수정된 내용이 없습니다');
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${input.value}" 질환/질병 분류명이 수정되었습니다`;
	const errorMessage = `"${input.value}" 질환/질병 분류명 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/disease/cate_info/modify_category_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 질환 / 질병 수정
async function putDiseaseModify(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.d_category_no;
	if(input.value === "") {
		alert('분류를 선택해 주세요.');
		return false;
	}
	
	input = form.d_name;
	if(input.value !== form.current_d_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			input.focus();
			return false;
		}
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
	const successMessage = `"${form.d_name.value}" 질환/질병 정보가 수정되었습니다`;
	const errorMessage = `"${form.d_name.value}" 질환/질병 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/disease/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 영상 정보 수정
async function putVideoModify(formName) {
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
	const successMessage = `"${form.v_title.value}" 영상 정보가 수정되었습니다`;
	const errorMessage = `"${form.v_title.value}" 영상 정보 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/video/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 공지사항 수정
async function putNoticeModify(formName) {
	const form = document.forms[formName];
	let input;
		
	input = form.n_title;
	if(!validateEmpty(input, '제목을', true)) {
		input.focus();
		return false;
	}
		
	input = form.n_body;
	if(!validateEmpty(input, '내용을', true)) {
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = '수정되었습니다';
	const errorMessage = '수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';

	await putIntegSubmit(
		'/notice/info/modify_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 게시판 수정
async function putBoardCategoryModify(formName) {
	const form = document.forms[formName];
	const bc_name = form.bc_name;
	const bc_idx = form.bc_idx;
	const current_bc_name = form.current_bc_name;
	const current_bc_idx = form.current_bc_idx;
	
	if(bc_name.value === current_bc_name.value && bc_idx === current_bc_idx.value) {
		alert('수정된 내용이 없습니다');
		return false;		
	}
	
	if(bc_name.value !== current_bc_name.value) { // 수정이 되었을 경우
		if(!(await requestDuplicateCheck(bc_name, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
			bc_name.focus();
			return false;
		}
	} 
	
	const formData = new FormData(form);
	const successMessage = `"${bc_name.value}" 이(가) 수정되었습니다`;
	const errorMessage = `"${bc_name.value}" 이(가) 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/board/cate_info/modify_category_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 게시판 순번 수정
async function putBoardCategoryModifyButton(event, bc_idx, page) {    
	// 실시간 비동기 작업으로 리로드 되지 않도록 putIntegSubmit함수 사용하지 않음
	if(setLoading(true, 'content_inner')) { // 로딩 추가 함수 실행이 성공하면 요청 진행 
		const infoEle = event.target.closest('tr'); // 클릭된 요소의 가장 가까운 tr 요소 찾기
	    const bc_no = infoEle.getAttribute('data-bc-no'); 
	    const current_bc_idx = infoEle.getAttribute('data-bc-idx'); 
		
		const formData = new FormData();
		formData.append('bc_no', bc_no);
		formData.append('current_bc_idx', current_bc_idx);
		formData.append('bc_idx', bc_idx);
		
		// setFormDataCheckConsoleLog(formData); // FormData 키벨류, byte 확인
		
		const errorMessage = '순번 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.';
	
		try {
			const response = await $.ajax({
				url: '/board/cate_info/modify_category_idx',
				method: 'POST',
				data: formData,
				processData: false,  // FormData가 자동으로 Content-Type 설정
				contentType: false,  // FormData를 문자열로 변환하지 않음
			});
			
			logger.info('/board/cate_info/modify_category_idx putBoardCategoryModifyButton() response:', response);
			
			if(!response) {
				if(errorMessage) alert(errorMessage);
			}
			
		} catch(error) {
			logger.error('/board/cate_info/modify_category_idx putBoardCategoryModifyButton() error:', error);
			if(errorMessage) alert(errorMessage);
			
		} finally {
			setLoading(false, 'content_inner'); // 로딩 종료
			getList('/board/cate_info/get_category_list', null, null, page);
		}
	}
}

// 게시판 공지 사항 수정
async function putNoticePostsModify(formName) {
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
	
	const successMessage = `"${bn_title.value}" 게시판 공지 사항이 수정되었습니다.`;
	const errorMessage = `"${bn_title.value}" 게시판 공지 사항 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	const formData = new FormData();
	formData.append('bn_body', quill.root.innerHTML); // quill 에디터 내용
	
	const $imgTags = $(quill.root).find('img'); // 모든 이미지 태그 탐색
	if($imgTags.length) {
		logger.info('이미지 태그 있음');
		
		for(let img of $imgTags) {
			const blobURL= $(img)[0].src; // src 속성에 입력된 blob URL 가져오기
			const targetWidth = $(img)[0].width; // 리사이즈할 대상 이미지의 너비 가져오기
			
			// 설정된 width 크기로 리사이즈 압축 후 flle 객체로 변환하여 formData 추가
			const resizedImageFile = await resizeImage(blobURL, targetWidth);
			formData.append('files', resizedImageFile); // 리사이즈된 File객체를 formData에 추가
			URL.revokeObjectURL(blobURL); // blob URL을 브라우저 메모리에서 해제
		}
		
	} else {
		logger.info('이미지 태그 없음');
		const emptyBlob = new Blob([], { type: 'application/octet-stream' }); // 빈 Blob 생성
		formData.append('files', emptyBlob); 
	}
	
	await putIntegSubmit(
		'/board/info/modify_confirm',
		formData,
		successMessage,
		errorMessage,
		'content_inner'
	);
}

// 게시물 수정
async function putPostsModify(formName) {
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
	
	const successMessage = `"${bp_title.value}" 게시물이 수정되었습니다.`;
	const errorMessage = `"${bp_title.value}" 게시물 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	const formData = new FormData(form);
	formData.set('bp_body', quill.root.innerHTML); // quill 에디터 내용
	
	const $imgTags = $(quill.root).find('img'); // 모든 이미지 태그 탐색
	if($imgTags.length) {
		logger.info('이미지 태그 있음');
		
		for(let img of $imgTags) {
			const blobURL= $(img)[0].src; // src 속성에 입력된 blob URL 가져오기
			const targetWidth = $(img)[0].width; // 리사이즈할 대상 이미지의 너비 가져오기
			
			// 설정된 width 크기로 리사이즈 압축 후 flle 객체로 변환하여 formData 추가
			const resizedImageFile = await resizeImage(blobURL, targetWidth);
			formData.append('files', resizedImageFile); // 리사이즈된 File객체를 formData에 추가
			URL.revokeObjectURL(blobURL); // blob URL을 브라우저 메모리에서 해제
		}
		
	} else {
		logger.info('이미지 태그 없음');
		const emptyBlob = new Blob([], { type: 'application/octet-stream' }); // 빈 Blob 생성
		formData.set('files', emptyBlob); 
	}
	
	await putIntegSubmit(
		'/board/info/modify_confirm',
		formData,
		successMessage,
		errorMessage,
		'content_inner'
	);
}

// 광고 분류 수정
async function putAdvertisementCategoryModify(formName) {
	const form = document.forms[formName];
	let input;
	
	input = form.ac_name;
	if(!(await requestDuplicateCheck(input, true, null, true))) { // 요소, 빈값 체크 여부, 기본값 비교 여부, 경고창 표시 여부
		input.focus();
		return false;
	}
	
	const formData = new FormData(form);
	const successMessage = `"${input.value}" 광고 분류명이 수정되었습니다`;
	const errorMessage = `"${input.value}" 광고 분류명 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;

	await putIntegSubmit(
		'/advertisement/cate_info/modify_category_confirm', 
		formData, 
		successMessage, 
		errorMessage,
		'content_inner'
	);
}

// 광고 수정
async function putAdvertisementModify(formName) {
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
	const $previewContainer = $('.image_file_preview'); // 미리보기 요소
	if(!input.files.lenth && !$previewContainer.length) { // 기존이미지 제거한 후 이미지 파일이 선택되지 않음
		alert('이미지 파일을 선택해 주세요.');
		input.focus();
		return false;
	}
	
	const formData = new FormData(form); 
	const isChangedImg = $previewContainer.find('img').attr('src').startsWith('blob:'); // src가 blob으로 시작할 경우 수정됨
	if(!isChangedImg) { // 이미지 파일이 수점되지 않았을 경우 formData에 files객체 수정
		const emptyBlob = new Blob([], { type: 'application/octet-stream' }); // 빈 Blob 생성
		formData.set('files', emptyBlob); 
	}
	
	const successMessage = `"${form.ad_client.value}" 님의 광고가 수정되었습니다.`;
	const errorMessage = `"${form.ad_client.value}" 님의 광고 수정에 실패했습니다. 다시 시도해 주세요.\n문제가 지속될 경우 관리자에게 문의해 주세요.`;
	
	await putIntegSubmit(
		'/advertisement/info/modify_confirm',
		formData,
		successMessage,
		errorMessage,
		'content_inner'
	);
}
