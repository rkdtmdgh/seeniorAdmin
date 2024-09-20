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
	
	// 첨부 파일 최대 허용 용량 설정
	const maxFileSize = 1024 * 1024 * 5; // 5MB
	
	// input을 한 번만 생성하고 재사용
	const $fileInput = $('<input type="file" accept="image/*">');
	
	// 이미지 버튼 이벤트 처리
	quill.getModule('toolbar').addHandler('image', function () { // 해당 모듈에 접근하여 image 버튼에 커스텀 이벤트 연결
        $fileInput.trigger('click'); // 파일 선택창 열기
    });
    
    // input file 선택 이벤트
    $fileInput.on('change', function() {
		handleFileUpload(this.files[0]);
	});
	
	// file drop 이벤트 
	$(quill.root).on('drop', function(event) {
		event.preventDefault();
		const file = event.originalEvent.dataTransfer.files[0]; // jQuery 객체로 감싸졌을 경우 직접 dataTransfer가 접근되지 않을 수 있음(originalEvent 사용)
		handleFileUpload(file);
	});
	
	// 이미지 파일 첨부 핸들러
	function handleFileUpload(file) {
		if(!file) return false;
		
		if(!file.type.startsWith('image/')) { // 이미지 파일인지 검사		
			alert('이미지 파일만 업로드할 수 있습니다. 이미지 파일을 선택해 주세요.');	
			return false;
		}
		
		if(file.size > maxFileSize) { // 파일 크기 검사
			alert('업로드 가능한 최대 용량을 초과했습니다. 5MB 이하의 이미지 파일을 선택해 주세요.');
			return false;
		}
		
		uploadImage(file); // 모든 유효성 검사 통과 시 업로드 로직 실행
	}

	// 파일 업로드
	async function uploadImage(file) {
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
				const range = quill.getSelection();
				quill.clipboard.insertEmbed(range ? range.index : 0, `<img src="${response.imageUrl}" alt="image">`);
				
			} else {
				alert(response.errorMessage || '이미지 업로드에 실패했습니다.');
			}
			
		} catch(error) {
			alert('파일 업로드 중 오류가 발생했습니다.');
			logger.error('uploadImage() error:', error);
		}
	}
});