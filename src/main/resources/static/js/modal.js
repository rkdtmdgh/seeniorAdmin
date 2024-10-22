// 모달 열기
function openModal(formName, contentFunction) {
	const $form = $(`form[name="${formName}"]`);
	const infoForm = modalContent(formName, contentFunction); // info HTML 가져오기
	const $modalContent = $(`
        <div class="fixed_wrap">
        	<div class="dimmed_wrap" onclick="closeModal();"></div>
	        <div class="modal_wrap sc">
	            <div class="modal_container">
	            	<div class="modal_info content_edit_table">
		                ${infoForm}
		            </div>
	            </div>
	        </div>
        </div>
	`);
	 
	$form.append($modalContent); // 모달 추가
	$('.dimmed_wrap').fadeIn(100);
	$('.modal_container').fadeIn(300).css('margin-top', 0);
}

// 모달 닫기
function closeModal() {
    $('.fixed_wrap').fadeOut(100, function() {
        $(this).remove();
    });
}

// 회원 계정 상태 변경 폼
function modalContent(formName, contentFunction) {
	switch(contentFunction) {
		case 'putUserAccountBlockModify':
			return `
				<p class="modal_title bold">회원 계정 정지 사유</p>
				
				<div class="modal_info_list">
					<textarea name="u_blocked_reason" class="border_textarea" placeholder="계정 정지 사유룰 입력하세요"
						oninput="setTextLimit(this, 'short')"
					></textarea>
					
					<div id="text_limit">
        				<span id="current_size">0</span> / <span id="max_size">250 byte</span>
        			</div>
        			
        			<div class="btn_list">
	                    <div onclick="closeModal();" class="btns cancel">닫기</div>
	                    <div onclick="putUserAccountBlockModify('${formName}', false)" class="btns">계정 정지</div>
	                </div>
				</div>
			`;
	}
}