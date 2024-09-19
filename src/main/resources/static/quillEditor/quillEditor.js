$(document).ready(function() {
	const quill = new Quill('#editor', {
		modules: {
			toolbar: [
				[
					{ 'header': [1, 2, 3, 4, 5, 6, false] }, 
					'bold', 'italic', 'underline', 'strike', 
					{ 'align': [] }, 
					{ 'color': [] }, 
					{ 'background': [] }, 
					'link', 'image', 'video',
				],
			],
			imageResize: {},
			imageDrop: false,
		},
		placeholder: '내용을 입력하세요',
		theme: 'snow',
	});
	
	// 이미지 버튼 이벤트 처리
	quill.getModule('toolbar').addHandler('image', function () { // 해당 모듈에 접근하여 image 버튼에 커스텀 이벤트 연결
        selectLocalImage();
    });

	function selectLocalImage() { // input type=file 생성하여 실행
		const $fileInput = $('<input type="file" accept="image/*">');
		
		$fileInput.trigger('click'); // 파일 선택창 열기
		
		$fileInput.on('change', function() { // 파일이 선택이 되었을 때
			const file = this.files[0]; 
			if(quill && file) {
				uploadImage(file);
			}
		});
	}

	// 파일 드롭 이벤트 핸들러 등록
	$(quill.root).on('drop', function(event) {
		event.preventDefault();
		const file = event.originalEvent.dataTransfer.files[0]; // jQuery 객체로 감싸졌을 경우 직접 dataTransfer가 접근되지 않을 수 있음(originalEvent 사용)
		if(quill && file) {
			uploadImage(file);
		}
	});

	// 파일 업로드 함수
	async function uploadImage(file) {
		if (!quill) {
			return;
		}
		
		// 이미지 파일인지 체크
		if (!file.type.startsWith('image/')) {
			alert('이미지 파일만 업로드할 수 있습니다.');
			return;
		}
		
		const formData = new FormData();
		formData.append('file', file);

		try {
			const response = await $.ajax({
				url: '/upload/upload_file',
				method: 'POST',
				enctype: 'multipart/form-data',
				data: formData,
				processData: false,  // FormData가 자동으로 Content-Type 설정
				contentType: false,  // FormData를 문자열로 변환하지 않음
			});
			
			logger.info('uploadImage() response:', response);

			if(response && response.imageUrl) {
				// 이미지 삽입을 제어
				const range = quill.getSelection();
				quill.clipboard.dangerouslyPasteHTML(range ? range.index : 0, `<img src="${response.imageUrl}" alt="image">`);
				
			} else {
				alert(response.errorMessage || '이미지 업로드에 실패했습니다.');
			}
			
		} catch(error) {
			logger.error(error);
		}
	}
});