// Quill 관련 스타일시트 및 스크립트 로드
document.write('<link rel="stylesheet" href="/quillEditor/quill.snow.css">');
document.write('<script src="/quillEditor/1.3.6.quill.min.js"></' + 'script>'); // HTML 파서가 닫는 태그를 읽을 경우 잘못 인식 방지
document.write('<script src="/quillEditor/image-resize.min.js"></' + 'script>');
document.write('<script src="/quillEditor/image-drop.min.js"></' + 'script>');

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
					{ 'list': 'ordered' }, 
					{ 'list': 'bullet' }, 
					'link', 'image', 'video'
				],
			],
			imageResize: {},
			imageDrop: false,
		},
		placeholder: '내용을 입력하세요.',
		theme: 'snow',
	});
	
	// 이미지 버튼 이벤트 처리
	quill.getModule('toolbar').addHandler('image', function () {
        selectLocalImage();
    });

	function selectLocalImage() {
		const fileInput = document.createElement('input');
		fileInput.setAttribute('type', 'file');
		fileInput.accept = "image/*";

		fileInput.click();

		fileInput.addEventListener("change", function () {
			if (quill) {
				uploadImage(fileInput.files[0]);
			}
		});
	}

	// 이미지 드롭 이벤트 핸들러 등록
	quill.root.addEventListener('drop', function (event) {
		event.preventDefault();
		if (quill) {
			const file = event.dataTransfer.files[0];
			uploadImage(file);
		}
	});

	// 이미지 업로드 함수
	function uploadImage(file) {
		if (!quill) {
			return;
		}
		
		const formData = new FormData();
		formData.append('uploadFile', file);

		$.ajax({
			type: 'post',
			enctype: 'multipart/form-data',
			url: '/quillEditor/upimg_ok.asp',
			data: formData,
			processData: false,
			contentType: false,
			dataType: 'json',
			success: function (data) {
				if (data.errorNum === 0) {
					// 이미지 삽입을 제어
					const range = quill.getSelection();
					quill.clipboard.dangerouslyPasteHTML(range ? range.index : 0, `<img src="${data.imageUrl}" alt="Image" />`);
				} else {
					alert(data.errorMessage);
				}
			},
			error: function (err) {
				console.log('ERROR!! ::');
				console.log(err);
			}
		});
	}
});