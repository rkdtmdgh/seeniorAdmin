// 모달창 제어
function openModal(formName, contentFunction) {
	const $form = $(`form[name="${formName}"]`);
	const $dimmed = $('<div class="dimmed_wrap" onclick="modalClose();">'); // 딤드 생성
	const infoForm = modalContent(formName, contentFunction); // info HTML 가져오기
	const $modalContent = $(`
        <div class="modal_wrap sc">
            <div class="modal_container">
            	<div class="modal_info content_edit_table">
	                ${infoForm}
	            </div>
            </div>
        </div>
	`);
	 
	$form.append($dimmed, $modalContent); 
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
				</div>
                <div class="btn_list">
                    <div onclick="modalClose();" class="btns cancel">닫기</div>
                    <div onclick="putUserAccountBlockModify('${formName}')" class="btns">계정 정지</div>
                </div>
			`;
	}
}