// 전역 변수로 선언
let quill; // quill 에디터

// Quill의 기본 Image 포맷 가져오기
const Image = Quill.import('formats/image');

// blob URL을 허용하도록 sanitize 메서드 재정의
Image.sanitize = function(url) {
    if (url.startsWith('blob:')) { // Blob URL을 허용하는 조건 추가
        return url;
    }
    
    return Quill.import('formats/image').sanitize(url); // 기본적으로 허용되는 URL (http, https 등)
};

// 새로운 Image 포맷 등록
Quill.register(Image, true);

// 이미지 리사이즈 및 객체 반환
async function resizeImage(blobURL, targetWidth, quality = 0.8) {
    return new Promise((resolve, reject) => {
        const $img = $('<img>')[0]; // img 태그 생성
        $img.src = blobURL;
        
        //const response = await fetch(blobURL); // blob URL에서 blob 객체 가져오기
        //const blob = await response.blob(); // blob 객체 추출
        
        // jpeg보다 높은 압축률, 손실, 무손실, 투명, 애니메이션 지원으로 webp로 변환
        const mimeType = 'image/webp'; // 원본 타입 유지 시 위 주석 처리 해제하여 blob.type; MIME 타입 추출 (Blob 자체에서 제공)

        // 이미지가 로드되면 리사이즈 시작
        $img.onload = function() {
            const $canvas = $('<canvas>')[0]; // 새로운 캔버스 요소 생성
            const ctx = $canvas.getContext('2d'); // 2D 컨텍스트 가져오기

            // 비율에 맞게 새로운 크기 설정
            const scaleFactor = targetWidth / $img.width; // 크기 비율 계산
            const targetHeight = $img.height * scaleFactor; // 비율에 맞는 높이 값
            $canvas.width = targetWidth; // 타겟 너비 설정
        	$canvas.height = targetHeight; // 타겟 높이 설정

            // 이미지를 캔버스에 그려서 리사이즈된 이미지로 복사본 생성
            ctx.drawImage($img, 0, 0, targetWidth, targetHeight); // 이미지를 캔버스에 그림(이미지 소스, x축, y축, 이미지 너비, 이미지 높이)
            
            // blob으로 변환 후 File객체로 변환
            $canvas.toBlob((resizedBlob) => {
                if (resizedBlob) {
					const fileName = `image_${Date.now()}.${mimeType.split('/')[1]}`;
					const file = new File([resizedBlob], fileName, {type: mimeType}); // 파일 객체로 변환
                    resolve(file); // 리사이즈된 파일 반환
                    
                } else {
                    reject(new Error("blob 생성에 실패했습니다.")); // blob 생성 실패 시 에러 반환
                }
                
           		URL.revokeObjectURL(blobURL); // blob URL을 브라우저 메모리에서 해제
            
            }, mimeType, quality);
        };

        $img.onerror = reject; // 이미지 읽는 과정에서 에러 발생 시 reject
    });
}

$(document).ready(function() {
	quill = new Quill('#editor', {
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
			imageDrop: false, // 드롭 이벤트 커스텀을 위해 false처리
		},
		placeholder: '내용을 입력하세요',
		theme: 'snow',
	});
	
	// 제한 설정
	const maxFileSize = 1024 * 1024 * 5; // 파일 사이즈 byte
	const maxTextSize = 1000000; // 최대 텍스트 입력 설정 byte
	const maxImageCount = 10; // 최대 이미지 삽입 개수
	const maxVideoCount = 5; // 최대 영상 삽입 개수
	
	// 이미지 삽입 버튼 클릭 이벤트 처리
	quill.getModule('toolbar').addHandler('image', function () { // 해당 모듈에 접근하여 image 버튼에 커스텀 이벤트 연결
        if(insertImageConfirm()) { // 이미지 삽입 제한 체크
			const $fileInput = $('<input type="file" accept="image/*">'); // 같은 파일 삽입시 중복 적용을 위해 재사용하지 않음
	        $fileInput.trigger('click'); // 파일 선택창 열기
		    $fileInput.on('change', function() { // 파일이 선택되었을 때
				validationImage(this.files[0]);
			});
		}
    });
	
	// 파일이 드롭될 때
	$(quill.root).on('drop', function(event) {
		event.preventDefault();
		if(insertImageConfirm()) { // 이미지 삽입 제한 체크
			const file = event.originalEvent.dataTransfer.files[0]; // jQuery 객체로 감싸졌을 경우 직접 dataTransfer가 접근되지 않을 수 있음(originalEvent 사용)
			validationImage(file);
		}
	});
	
	// 이미지 파일 첨부 핸들러
	function validationImage(file) {
		if(!file) return false;
		
		if(!file.type.startsWith('image/')) { // 이미지 파일인지 검사	
			alert('이미지 파일만 업로드할 수 있습니다. 이미지 파일을 선택해 주세요.');	
			return false;
		}
		
		if(file.size > maxFileSize) { // 파일 크기 검사
			alert('업로드 가능한 최대 용량을 초과했습니다. 5MB 이하의 이미지 파일을 선택해 주세요.');
			return false;
		}
		
		previewImage(file); // 유효성 검사 완료 시 image URL 생성하여 미리보기 처리
	}
	
	// image URL 생성하여 미리보기 처리 
	function previewImage(file) {
		const range = quill.getSelection(); // 현재 커서 위치
		
		// base64 URL 사용
	    //const reader = new FileReader(); // FileReader 객체 생성
	    //reader.readAsDataURL(file); // 파일을 base64 데이터 URL로 읽기
	    //reader.onload = function(event) {
	    //    const imageURL = event.target.result; // base64 문자열을 가져옴
	    //};
	    
	    //reader.onerror = function(error) {
	    //    logger.error('Error reading file:', error);
	    //};
	    
	    // blob URL 사용
	    const imageURL = URL.createObjectURL(file); // blob URL 생성
	    
	    // 이미지 URL 입력하여 이미지 태그 삽입
	    quill.clipboard.dangerouslyPasteHTML(range ? range.index : 0, `<img src="${imageURL}" alt="image">`);
	}
	
	// 이미지 개수 제한 체크	
	function insertImageConfirm() {
		const imageCount = $(quill.root).find('img').length; // 현재 삽입된 이미지 개수
				
		if(imageCount >= maxImageCount) {
			alert(`이미지는 최대 ${maxImageCount}개까지만 삽입할 수 있습니다.`);
			return false;
		}
		
		return true;
	}
	
	// 텍스트 리미트 표시 초기화
	const initialText = quill.getText(); // 에디터 초기 순수 텍스트 가져오기
    const initialTextSize = new Blob([initialText]).size; // 텍스트 크기 계산 (byte)
	$('#current_size').text(initialTextSize.toLocaleString()); // 기본값 설정
	$('#max_size').text(`${maxTextSize.toLocaleString()} byte`);
	
	// quill 텍스트, 영상 입력 제한
	quill.on('text-change', function(delta, oldDelta, source) { // 변경된 내용, 변경 전 내용, 변경의 출처
		const text = quill.getText(); // 에디터의 순수 텍스트 가져오기
		const textSize = new Blob([text]).size; // 텍스트 크기 계산 (byte)
		
		$('#current_size').text(textSize.toLocaleString()); // 입력된 값 변경
		
		if(textSize > maxTextSize) {
			alert(`입력 가능한 최대 텍스트 용량은 ${maxTextSize.toLocaleString()} byte 입니다.`);
			quill.history.undo(); // 최근 입력된 내용을 되돌려 제한 초과 방지
		}
		
		const videoCount = $(quill.root).find('iframe').length; // 현재 삽입된 영상 개수
		if(videoCount > maxVideoCount) {
			alert(`영상은 최대 ${maxVideoCount}개까지만 삽입할 수 있습니다.`);
			quill.history.undo(); // 최근 입력된 내용을 되돌려 제한 초과 방지
		}
		
		if(!insertImageConfirm()) quill.history.undo(); // 이미지 삽입 개수 제한
	});
});