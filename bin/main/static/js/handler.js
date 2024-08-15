// input focus
export function setInputFocus(ele) {
	const input = document.querySelector(`input[name="${ele}"]`);
	if (input) {
		console.log('focus input:', input.name);
		input.focus();
	} else {
		console.log(`No input found with name: ${ele}`);
	}
}

// 검색 폼 데이터인지 확인하여 초기화
export function setFormValuesFromUrl(part) {
	const urlParams = new URLSearchParams(window.location.search);
    const sForm = document.forms['search_form'];
	const searchPart = urlParams.get('searchPart') || part;
    const searchString = urlParams.get('searchString') || '';
    const page = urlParams.get('page') || 1;
	
	const hasSearchString = searchString.trim().length > 0; // 검색어가 있는지 확인
	
	// 검색어가 있을 경우 검색 폼 사용으로 새로고침 시 재적용
	if(hasSearchString) {
		sForm.search_part.value = searchPart;
		sForm.search_string.value = searchString;
	}
	
	return { hasSearchString, page };
}