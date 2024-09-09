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
});