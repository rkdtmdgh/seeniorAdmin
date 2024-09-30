// input focus
function setInputFocus(ele) {
	const $input = $(`input[name="${ele}"]`);
	if ($input.length) {
		$input.focus();
		
	} else {
		logger.info(`No input found with name: ${ele}`);
	}
}

// loading set
let loadingTimeout; // 타이머 변수를 전역으로 선언
function setAddLoading(loading) {
	const $loadingElement = $('<span class="loading_wrap"></span>');
	if(loading) {
		loadingTimeout = setTimeout(() => {
			$('.content_info_wrap').append($loadingElement); // 해당 컨텐츠에 로딩 요소 추가		
		}, 100);
		
	} else {
		clearTimeout(loadingTimeout); // 딜레이 시간 안에 통신 완료 시 로딩 타이머 취소
		$('.loading_wrap').remove(); // 로딩 요소 제거
	}
}

// 로그인 유저 정보 해당 인풋에 정보 입력
async function setLoginUserInfoInputValue(name, key) {
	const $input = $(`input[name="${name}"]`);
	
	if($input.length) {
		const loginUserInfo = await getAccountInfo();
		$input.val(loginUserInfo[key]);
		
	} else {
		logger.error(`No input found with name: ${name}`);
	}
}

// submit 이벤트 막기(form 에서 enter키 입력 되지 않고 버튼으로 submit만 가능하도록)
// 예외 사항으로 form 내부에 disabled 되어 있는 인풋이 있는 경우에는 사용하지 않아도 됨
function setFormSendFalse(event) {
	event.preventDefault(); // 폼의 기본 제출 동작 방지
    return false; // 폼 제출 방지
}

// 콘텐츠 타이틀 설정
async function setContentTitle(infoNo) {
	if(infoNo) {
		const info = await getBoardInfo(infoNo);
		
		if(info) {
			const $title = $('.categoty_title');
			$title.text(info.boardCategoryDto.bc_name);
		}
		
	} else {
		logger.error('Not found infoNo');
	}
}

// 콘텐츠 서브 내용 설정
async function setContentSubInfo(txt) {
	const $title = $('.categoty_title');
	let $subInfo = $('.title_other_info_text');
	
	if(!$subInfo.length) {
		$subInfo = $('<span class="title_other_info_text">');
	}
	
	$subInfo.text(`마지막 업데이트 ${txt}`);		
	$title.append($subInfo);	
}

// 레시피 내용 레이아웃 설정
function setRecipeContentInfo(recipeDto) {
	logger.info('setRecipeContentInfo():', recipeDto);
	
 	if(recipeDto) {
		for (let i = 1; i <= 20; i++) {
	        let imgField = recipeDto['manual_img' + (i < 10 ? '0' + i : i)];
	        let manualField = recipeDto['manual' + (i < 10 ? '0' + i : i)];

		    if (imgField || manualField) { // 둘 중 하나라도 존재할 경우
		        const $manualInfoDiv = $('<div class="manual_info">');
	            const $imgTag = $(`<img src="${imgField}" alt="조리방법" class="sub_thumbnail">`);
	            const $pTag = $(`<p class="manual_info_text">${manualField}</p>`);
	            $manualInfoDiv.append($imgTag);
	            $manualInfoDiv.append($pTag);
		
		        $('.recipe_content').append($manualInfoDiv); // 필요한 위치에 삽입
		    }
		}
	}   
}

// 최대 텍스트 입력 설정 byte
const maxSize = {
	info: 5000,
}; 

// 텍스트 바이트 계산
function extractionByte(str) {
	const encoder = new TextEncoder();
    const encoded = encoder.encode(str); // 문자열을 UTF-8 바이트로 인코딩
    return encoded.length; // 인코딩된 바이트 배열의 길이 반환
}

// 이전 텍스트 상태를 저장
let previousText = '';

// textarea 텍스트 입력 제한 표시 초기화
function setTextareatLimitInit() {
	const $textareaEle = $('[data-current-size="textarea"]').val(); // 초기값
	const currentValue = extractionByte($textareaEle); // textarea의 현재 텍스트 값 바이트 크기
	previousText = $textareaEle; // 기초상태 업데이트
	
	const $textLimitEle =  $('#text_limit'); // text_limit 요소 찾기
	if($textLimitEle.length) {
		$('#current_size').text(currentValue.toLocaleString()); // 현재 입력된 텍스트의 바이트 크기 표시
		$('#max_size').text(`${maxSize.info.toLocaleString()} byte`); // 최대 크기 표시
	}
}

// 텍스트 입력 제한
function setTextLimit(ele, maxSizeKey) {
	const value = $(ele).val(); // 입력된 값 가져오기
	const textSize = extractionByte(value); // 현재 텍스트의 바이트 크기 계산
	const maxSizeValue = maxSize[maxSizeKey];
	
	$('#current_size').text(textSize.toLocaleString()); // 실시간으로 크기 업데이트
	
	if(textSize > maxSizeValue) { // 최대 크기를 초과한 경우
		alert(`입력 가능한 최대 텍스트 용량은 ${maxSizeValue.toLocaleString()} byte 입니다.`);
		$(ele).val(previousText); // 초과된 부분 제거 후 초과되기 전 값 입력
		$('#current_size').text(extractionByte(previousText).toLocaleString()); // 다시 계산 하여 표시
		
	} else {
		previousText = value; // 이전 텍스트 업데이트	
	}
}

// 새창 열기 중앙 설정
function setWindowOpenPosition(url, width, height) {
	const left = Math.ceil((window.screen.width - width)/2);
	const top = Math.ceil((window.screen.height - height)/2);
	window.open(url,'','width='+width+',height='+height+',left='+left+',top='+top+',scrollbars=yes,resizable=no,toolbar=no,titlebar=no,menubar=no,location=no')
}

// 본인 확인 페이지 세션스토리지 저장 값 확인하여 요청 처리
function setSessionIdentityCheck(loginUser) {
	const sessionLogId = sessionStorage.getItem('loginedId') || '';
	const sessionCheckDate = sessionStorage.getItem('checkDate') || '';
	
	if(!sessionLogId || !sessionCheckDate) {
		return false;
	}
	
	if(loginUser != sessionLogId) {
		return false;
	}
	
	// 현재 시간과 비교
	const currentTime = new Date();
	const checkTime = new Date(sessionCheckDate);
	const sessionTime = 10 * 60 * 1000; // 10분
	
	if(currentTime - checkTime > sessionTime) { // sessionTime 경과
		sessionStorage.removeItem('loginedId');
		sessionStorage.removeItem('checkDate');
		alert('세션이 만료되었습니다. 다시 확인해 주세요.');
		return false;
		
	}
	
	return true;
}

// all_check 체크박스 초기화
function setAllcheck() {
	const $allCheckBox = $('input[type="checkbox"][name="all_check"]');
	if($allCheckBox && $allCheckBox.prop('checked')) $allCheckBox.prop('checked', false);
}

// 체크박스 일괄 체크 및 해제
function setAllCheckBox(ele) {	
	const isChecked = ele.checked;
	const $checkboxList = $(`input[name="${ele.value}"]`);
	$checkboxList.prop('checked', isChecked);
}

// 비밀번호 노출 설정
function setPwViewToggle(ele) {
	const $icon = $(ele).find('.icon');
	const $parentEle = $(ele).parent();
	const $passwordInput = $parentEle.find('input');
	
	if($passwordInput.length) {
		if($passwordInput.attr('type') === "password") {
			$passwordInput.attr('type', 'text');
			$icon.attr('src', '/image/icons/eye_open.svg');
			
		} else {
			$passwordInput.attr('type', 'password');
			$icon.attr('src', '/image/icons/eye_off.svg');
		}
	}
}

// 에러 메세지 요소 생성
function setCreateErrorElement($ele) { // jQeury 객체를 받음을 명시
    const $parentEle = $ele.parent();
    let $errorEle = $parentEle.find('.input_error'); // 부모 요소 내에 에러 요소를 찾음
    
    // 에러 메세지 요소가 없으면 생성
    if (!$errorEle.length) {
        $errorEle = $('<span>').addClass('input_error'); // 요소 생성 및 클래스 추가
        $parentEle.append($errorEle); // 부모 요소에 추가
    }

    return $errorEle;
}

// 에러 메세지 노출
function setAddErrorMessage(input, message) {
	const $input = $(input); // DOM 요소를 jQuery 객체로 변환
	const $errorEle = setCreateErrorElement($input);
	$errorEle.text(message);
	$input.parent().addClass('error');
}

// 에러 메세시 제거
function setClearErrorMessage(input) {
	const $input = $(input); // DOM 요소를 jQuery 객체로 변환
	const $errorEle = setCreateErrorElement($input);
	$errorEle.remove();
	$input.parent().removeClass('error');
}

// 날짜 포맷팅
function setFormatDate(dateString) { // yyyy-mm-dd 형식
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
}

// 검색 폼 데이터인지, 정렬된 데이터인지 확인하여 초기화
function setFormValuesFromUrl() {
	const urlParams = new URLSearchParams(window.location.search);
    const $sForm = $('form[name="search_form"]');
	const searchPart = urlParams.get('searchPart') || undefined;
    const searchString = urlParams.get('searchString') || '';
	const sortType = urlParams.get('sortType') || undefined;
	const sortValue = urlParams.get('sortValue') || undefined;
	const order = urlParams.get('order') || undefined;
    const page = urlParams.get('page') || 1;
	
	// 검색어가 있을 경우 검색 폼 사용으로 새로고침 시 재적용
	if(sortType === '2') { // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
		$sForm.find('select[name="search_part"]').val(searchPart)
		$sForm.find('input[name="search_string"]').val(searchString);
	}
	
	return { sortType, sortValue, order, page };
}

// 페이지 유지를 위한 쿼리 스트링 제어(검색 이력 제거)
function setListQueryString(sortValue, order, page) {
	const url = new URL(window.location); // 현재 url
	const infoNo = url.searchParams.get('infoNo') || undefined; // cateNor가 있을 경우 값 가지고 있기
	const sortType = url.searchParams.get('sortType') || undefined; // sortType이 있을 경우 값 가지고 있기
    url.search = ''; // 파라미터 비우기
	
	// 파라미터 추가
	if(infoNo) url.searchParams.set('infoNo', infoNo); 
    if (sortValue) {
		if(sortType) url.searchParams.set('sortType', sortType); 
		url.searchParams.set('sortValue', sortValue); 
		url.searchParams.set('order', order); 
		
    } else {
		// sort 버튼 기본값으로 초기화
		$('.sort').attr('data-current-sort-value', 'all');
	}
	
	url.searchParams.set('page', page);
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 검색 후 페이지 유지를 위한 쿼리 스트링 제어(검색 파트, 스트링 재입력)
function setSearchQueryString(page, searchPart, searchString) {
	const url = new URL(window.location);
	const infoNo = url.searchParams.get('infoNo') || undefined; // cateNor가 있을 경우 값 가지고 있기
    url.search = '';
    if(infoNo) url.searchParams.set('infoNo', infoNo); 
	url.searchParams.set('sortType', 2); // 0 = 올림/내림차순, 1 = 카테고리선택, 2 = 검색
    url.searchParams.set('searchPart', searchPart);
    url.searchParams.set('searchString', searchString);
	url.searchParams.set('page', page); 
	window.history.replaceState({}, '', url); // 현재 url 변경 및 리로드 제어
}

// 테이블의 전체 열 수 계산하기
function setTableColumnsNum() {
	const maxCols = $('table thead tr').find('th').length;
	return maxCols;
}

// 테이블 셀렉트 옵션 노출 토글 버튼
function setSelectOptionTopggle(event) {
	const $selectEle = $(event.currentTarget).find('.select_option_list'); // 클릭한 요소 내의 커스텀 셀렉트 요소 찾기
	const $allSelectEle = $('.select_option_list'); // 모든 커스텀 셀렉트 요소
	$allSelectEle.not($selectEle).removeClass('active');
	$selectEle.toggleClass('active');
}

// textarea 입력 시 자동으로 높이값 조정
function setTextareaAutoHeight(ele) {
	const $textarea = $(ele);
	$textarea.height('auto'); // 요소의 높이를 초기화 하고 시작

	const scrollHeight = $textarea[0].scrollHeight; // 자바스크립트로 이벤트가 일어난 DOM요소의 scrollHeight 원시 속성 값 가져오기
	const clientHeight = $textarea[0].clientHeight; // 현재 textarea의 높이 (padding 포함, 스크롤 바 제외)
	
    if (scrollHeight <= clientHeight) { // 스크롤 높이가 클라이언트 높이와 작서나 같다면 내용이 한 줄임
        return false; // 한 줄만 입력된 경우에는 높이를 auto로 유지하고 종료
    }
	
	const maxHeight = parseInt($textarea.css('max-height')); // css로 설정한 max-height 속성 값 가져오기
	const newHeight = Math.min(scrollHeight, maxHeight); // 스크롤 높이와 max-height 중 작은 값ㅇ르 높이로 설정	
	$textarea.height(newHeight + 'px');
}

// ","구분이 필요한 textarea에서 enter입력 시 자동으로 "," 추가
function setTextareaAddCommaBeforeEnter(ele, event) {
	if(event.key === 'Enter') { // enter 키를 누를 때 동작
		let currentValue = ele.value;
		
		if(currentValue.trim() !== "" && !currentValue.trim().endsWith(',')) { // 빈값이 아니면서 마지막 글자가 ','가 아닐 경우 ','추가
			ele.value = currentValue.trim() + ',\n';
		}	
		
		event.preventDefault();	// 기본 enter 동작 방지
	}
}

// 본인 확인 전 account/modify_form SET 
function setIdentityCheckForm() {
	const $contentInfoWrap = $('.content_info_wrap');
	
	let dataFormContent;
	dataFormContent = `
		<div class="content_top">
	        <div class="content_top_info">
	            <h2 class="title">본인확인</h2>
	        </div>
            <p class="sub_title">본인확인을 위해 비밀번호를 입력해 주세요</p>
	    </div>
	    
	    <div class="sign_form" id="password_check_form">
	    	<form name="modify_check_form" onsubmit="postIdentityCheckForm(event, 'modify_check_form')">
            	<div class="input_list_container">
	                <div class="input_list" id="input_list_info">					
	                    <label for="pw" class="border_input">
	                        <span class="input_title">비밀번호</span>
	                        <input type="password" name="a_pw" id="pw" placeholder="비밀번호" class="input_txt" autocomplete="off">
	                        <span class="input_icon" onclick="setPwViewToggle(this)">
	                        	<img src="/image/icons/eye_off.svg" alt="toggle password visibility" class="icon">
	                        </span>
	                    </label>
	                </div>
	                
                    <div class="btn_list col">
                        <button type="submit" class="btns">확인</button>
                    </div>
                </div>
            </form>
	    </div>
	`;
	
	$contentInfoWrap.html(dataFormContent); // account/modifyForm SET
}

// 본인 확인 후 account/modify_form SET
function setAccountModifyForm(data) {
	let dataFormContent;
	dataFormContent = `
		<div class="content_top">
	        <div class="content_top_info">
	            <h2 class="title">내 정보 관리</h2>
	        </div>
            <p class="sub_title">내 정보 수정</p>
	    </div>
							
		<form name="modify_form">					    	
		    <div class="table_wrap">
                <table class="content_edit_table">
                    <colgroup>
                        <col class="col_row_title">
                        <col>
                        <col class="col_row_title">
                        <col>
                    </colgroup>

                    <tbody>
                        <tr>
                        	<th><p class="table_title">아이디</p></th>
                        	<td class="disabled">
                        		<input type="hidden" name="a_no" value="${data.a_no}">
                        		<input type="text" name="a_id" id="id" class="table_info disabled"
                                	value="${data.a_id}" disabled>
                            </td>

                            <th><p class="table_title">비밀번호</p></th>
                        	<td>
                        		<label class="cont_info">
                        			<input type="password" name="a_pw" id="pw" class="table_info" placeholder="변경 시 입력" autocomplete="off">
                        			<span class="input_icon" onclick="setPwViewToggle(this)">
			                        	<img src="/image/icons/eye_off.svg" alt="toggle password visibility" class="icon">
			                        </span>
                        		</label>
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">이름</p></th>
                        	<td>
                                <input type="text" name="a_name" id="name" class="table_info" placeholder="이름"
                                	oninput="validateEmpty(this, '이름을')" onblur="validateEmpty(this, '이름을')"
                                	value="${data.a_name}">
                            </td>

                        	<th><p class="table_title">생년월일</p></th>
                        	<td>
                                <input type="date" name="a_birth" id="birth" min="1900-01-01" max="9999-12-31" class="table_info"
                                	onblur="replaceDate(this)"
                                	value="${data.a_birth}">
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">연락처</p></th>
                        	<td>
                                <input type="text" name="a_phone" id="phone" maxlength="13" class="table_info" placeholder="연락처"
                                	onkeydown="replacePhone(this)" onkeyup="validatePhone(this)" onblur="validatePhone(this)"
                                	value="${data.a_phone}">
                            </td>
                        	<th><p class="table_title">부서</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_department" id="department" class="table_info" placeholder="부서"
                                	value="${data.a_department || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        </tr>

                        <tr>
                        	<th><p class="table_title">직위</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_level" id="level" class="table_info" placeholder="직위"
                                	value="${data.a_level || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        	<th><p class="table_title">직책</p></th>
                        	<td class="disabled">
                                <input type="text" name="a_position" id="position" class="table_info" placeholder="직책"
                                	value="${data.a_position || ''}"
                                	${data.a_authority_role != 'SUPER_ADMIN' ? 'disabled' : ''}>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="btn_list right">
                    <div class="btn_list">
                        <div onclick="putModifyForm('modify_form')" class="btns">수정</div>
                    </div>
                </div>
            </div>
        </form>
	`;
	
	return dataFormContent;
}

// 문서 클릭 이벤트
$(document).on('click', function(event) {
	// 커스텀 셀렉트 open, close 기능
	const $openSelectEle = $('.select_option_list.active'); // 열려 있는 셀렉트 옵션 요소
	const isTriggerClick = event.target.closest('.table_title.select'); // 클릭한 요소가 커스텀 셀렉트 버튼인지 확인
	if(!isTriggerClick) { // 클릭한 요소가 커스텀 셀렉트 버튼이 아닐 경우
		$openSelectEle.removeClass('active'); // 열려 있는 셀렉트 옵션 닫기
	}
});

// 문서가 준비된 후 실행
$(function() {
	// textarea 입력된 값으로 높이값 조절
	$('.table_textarea.small').each(function() {
		setTextareaAutoHeight(this);
	});	
	
	// textarea 텍스트 입력 제한 표시 초기화
	setTextareatLimitInit();
});