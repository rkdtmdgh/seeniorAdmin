// 질환 카테고리 등록 유효성 검사

$(document).ready(function() {
    console.log("DOCUMENT READY!!");
    
});


function createCategoryForm() {
	console.log("createCategoryForm()");
	
	let form = document.create_category_form;
	
	if (form.name.value === "") {
		alert("질환 카테고리 명을 입력해주세요");
		
	} else {
		form.submit();
		
	}
	
	
}