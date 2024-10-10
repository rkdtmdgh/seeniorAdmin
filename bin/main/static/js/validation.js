// 유효성 검사
function validateInput(input, regEx, errorMessage, checkProfanity = false) { // 욕설 필터 체크 기본값 false로 설정
	const value = input.value.trim(); // 앞뒤 공백 제거
	const isValid = regEx ? regEx.test(value) : value.trim().length > 0; // 정규 표현식 검사 또는 입력값 확인
	
	// checkProfanity가 false일 경우 욕설 필터링 제외
    const profanityResult = checkProfanity ? profanityFilter(value) : { found : false };
	
	if (value === "" || !isValid || profanityResult.found) {
		setAddErrorMessage(input, profanityResult.found ? "금지된 단어가 포함되어 있습니다." : errorMessage); // 에러 메시지 설정
		
		// 비속어가 있을 경우 위치에 커서 포커싱
		if(profanityResult.found) {
			const position = profanityResult.position;
            const length = profanityResult.length;
            input.focus();
            input.setSelectionRange(position, position + length); // 단어 커서 블럭 처리
		}
		
    } else {
		setClearErrorMessage(input); // 에러 메시지 지우기
    }
	
    return isValid && !profanityResult.found; // 두 조건에 부합해야 true 즉 정규 표현식 검사, 입력값 유무, 욕설 필터 모두 true를 반환 해야 함
}

// 아이디 유효성 검사
async function validateEmail(input, usedCheck, alertMsg) { 
	const regEx = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z.]{2,5}$/; // 로컬파트와 도메인은 영문, 숫자, 정해진 특수문자/ TLD는 영문, "."를 포함할 수 있고 2~5자
	const errorMessage = "이메일을 정확히 입력해 주세요.";
	const isValid = validateInput(input, regEx, errorMessage);
	logger.info('validateEmail isValid:', isValid);
	
	if(alertMsg && !isValid) {
		alert(errorMessage);
		return false;
	}
	
	if(isValid && usedCheck) {
		try {
			const isUsed = await requestEmailDuplicateCheck(input.value); // true=중복, false=정상 반환
			if(!isUsed) { 
				setClearErrorMessage(input);
				return true;
				
			} else {
				if(alertMsg) {
					alert("이미 사용 중인 이메일입니다.");
				}
				
				setAddErrorMessage(input, "이미 사용 중인 이메일입니다.");
				return false;
			}
			
		} catch(error) {
			logger.error('Error during email check:', error);
			
			if(alertMsg) {
				alert("이메일 중복 확인 중 오류가 발생했습니다.");
			}
			
			setAddErrorMessage(input, "이메일 중복 확인 중 오류가 발생했습니다.");
			return false;
		}
	}
	return isValid;
}

// 비밀번호 유효성 검사
function validatePw(input, alertMsg) { 
	const regEx = /^(?=.*[a-zA-Z])(?=.*[@$!%*?&])[a-zA-Z\d@$!%*?&]{8,16}$/; // 8~16자의 영문 대소문자 중 최소 1개, 특수문자 최소 1개, 숫자 선택 입력
    const errorMessage = "비밀번호는 8~16자의 영문대소문자, 특수문자(@, $, !, %, *, ?, &), 숫자를 사용할 수 있습니다. (필수: 영문대소문자, 특수문자)";
	const isValid = validateInput(input, regEx, errorMessage);
	if(alertMsg && !isValid) {
		alert(errorMessage);
		return false;
	}
	
	return isValid;
}

// 연락처 유효성 검사   
function validatePhone(input, alertMsg) {
	const regEx = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/; // 휴대폰 번호 정규 표현식
	const errorMessage = "휴대폰 번호를 정확히 입력해 주세요.";
	const isValid = validateInput(input, regEx, errorMessage);
	if(alertMsg && !isValid) {
		alert(errorMessage);
		return false;
	}
	
	return isValid; 
}

// 데이터 유효값 확인
function validateEmpty(input, txt, alertMsg,  notViewMsg = false) { // 요소, 텍스트, alert 여부, 에러메세지 요소 표기 여부(false = 노출, ture = 노출X)
	const errorMessage = txt + " 입력해 주세요.";
	input.value = input.value.trim(); // 앞뒤 공백 제거 적용
	if(alertMsg) {
		if(!notViewMsg && !validateInput(input, null, errorMessage)) {
			alert(errorMessage);
			return false;
		}
		
		if(input.value.trim().length > 0) {
			return true;
			
		} else {
			alert(errorMessage);
			return false;
		}
		
	} else {
		return validateInput(input, null, errorMessage); // 정규 표현식 대신 값의 길이만 확인
	}
}

// quill 에디터 내용 유효성 및 비속어 검사
function validateQuill(quill) {
	const emptyTagPattern = /<p><br><\/p>/g; // quill 에디터에서 빈값일 경우 기본값
	const trimmedContent = quill.root.innerHTML.replace(emptyTagPattern, "").trim(); // 빈 태그 패턴을 제거한 결과 값
	const isValid = trimmedContent !== "";
	
	if(!isValid) {
		alert('내용을 입력해 주세요.');
		return false;
	}
	
	// 퀼 에디터의 텍스트를 가져와서 비속어 검사
    const plainText = quill.getText();
    const profanityResult = profanityFilter(plainText);
   
	if(profanityResult.found) { // 비속어가 있을 경우
		quill.setSelection(profanityResult.position, profanityResult.length);
		alert('금지된 단어가 포함되어 있습니다.');
		return false;
	}
	
	return true;
}

// 영상 정보 URL 미리 보기 설정
function validateVideo(input, alertMsg) {
	const $input = $(input); // DOM 요소를 jQuery 객체로 변환
	if(!validateEmpty(input, 'URL 주소를', alertMsg)) return false; // 빈값 확인
	
    //동영상 URL을 임베디드 URL로 변환
	const url = $input.val().trim();
    let $previewEle = $input.parent().find('.preview_container'); // 부모 요소 내에 미리보기 요소를 찾음
    if($previewEle.length && $previewEle.find('iframe').attr('src') === url) return false; // 수정되지 않았을 경우 리턴
    
    const platformInfo = extractPlatformInfo(url); // 플랫폼 정보 추출    
    
    if(platformInfo) {	    
		// 미리보기 요소가 없으면 생성
    	if (!$previewEle.length) $previewEle = $(`
    		<div class="table_info preview_container ${
				platformInfo.platform == 'instagram_reels' ? 'vertical' : 'horizontal'
    		}">
    	`); // 요소 생성 및 클래스 추가
    	
    	// iframe 태그에 플랫폼에 맞는 임베디드 URL 삽입하여 preview_container 요소 추가
    	$previewEle.html(`<iframe frameborder="0" allowfullscreen="true" src="${platformInfo.embedUrl}"></iframe>`);
    	$input.val(platformInfo.embedUrl); // 임베드된 주소로 변경
    	$input.parent().append($previewEle);
    	
    	// 플랫폼 input hidden 생성해서 값 추가
    	let $platformInput = $('input[name="v_platform"]');
    	
    	if(!$platformInput.length) {
			$platformInput = $('<input type="hidden" name="v_platform">');
		}
		
		$platformInput.val(platformInfo.platform);
		$input.parent().append($platformInput)
		logger.info('input v_platform:', $platformInput.val());
		
    	setClearErrorMessage(input);
    	
	} else { // 유효한 URl이 아니거나 지원되지 않은 플랫폼일 경우
		if ($previewEle.length) $previewEle.remove(); // 잘못된 URL 입력 시 미리보기 삭제
		if(alertMsg) alert('지원되지 않은 플랫폼이거나 유효하지 않은 URL입니다. \n지원 플랫폼: YouTube, Vimeo, Dailymotion, Instagram Reels');
		setAddErrorMessage(input, '지원되지 않은 플랫폼이거나 유효하지 않은 URL입니다. 지원 플랫폼: YouTube, Vimeo, Dailymotion, Instagram Reels');
		return false;
	}
	
	return true; // 검증을 통과한 경우
}

// URL에서 플랫폼 및 영상 ID를 추출하고 임베드 링크를 반환
function extractPlatformInfo(url) {
	// Youtube, Vimeo, Dailymotion, Facebook Reel, Instagram Reel 플랫폼 확인
    
    const platforms = [ // 플렛폼 추가시 해당 플랫폼에 맞는 정규표현식, 임베디드 URL 추가
		{
			name: 'youtube',
			regEx: /(?:https?:\/\/)?(?:www\.)?(?:youtube\.com\/(?:watch\?v=|embed\/)|youtu\.be\/)([a-zA-Z0-9_-]{11})/,
			embedUrl: (id) => `https://www.youtube.com/embed/${id}`
		},
		{
            name: 'vimeo',
            regEx: /(?:https?:\/\/)?(?:www\.)?vimeo\.com\/(\d+)/,
            embedUrl: (id) => `https://player.vimeo.com/video/${id}`
        },
        {
            name: 'dailymotion',
            regEx: /(?:https?:\/\/)?(?:www\.)?dailymotion\.com\/video\/([a-zA-Z0-9]+)/,
            embedUrl: (id) => `https://www.dailymotion.com/embed/video/${id}`
        },
        {
            name: 'instagram_reels',
            regEx: /(?:https?:\/\/)?(?:www\.)?instagram\.com\/reel\/([a-zA-Z0-9_-]+)/,
            embedUrl: (id) => `https://www.instagram.com/reel/${id}/embed`
        }
	];
	
	for(const platform of platforms) { // URL을 각 플렛폼의 정규표현식과 비교하여 매칭된 플랫폼 반환
		const match = url.match(platform.regEx);
		if(match) {
			return { platform: platform.name, embedUrl: platform.embedUrl(match[1]) };
		}
	}
	
	return null; // 지원되지 않는 플랫폼일 경우 null 반환
}

// 욕설 및 비속어 필터링
function profanityFilter(value) {
	const profanityList = ["ㅅㅂ", "씨발", "씨바", "개세끼", "18년", "18놈", "18새끼", "ㄱㅐㅅㅐㄲl", "ㄱㅐㅈㅏ", "가슴만져", "가슴빨아", "가슴빨어", "가슴조물락", "가슴주물럭", "가슴쪼물딱", "가슴쪼물락", "가슴핧아", "가슴핧어", "강간", "개가튼년", "개가튼뇬", "개같은년", "개걸레", "개고치", "개너미", "개넘", "개년", "개놈", "개늠", "개똥", "개떵", "개떡", "개라슥", "개보지", "개부달", "개부랄", "개불랄", "개붕알", "개새", "개세", "개쓰래기", "개쓰레기", "개씁년", "개씁블", "개씁자지", "개씨발", "개씨블", "개자식", "개자지", "개잡년", "개젓가튼넘", "개좆", "개지랄", "개후라년", "개후라들놈", "개후라새끼", "걔잡년", "거시기", "걸래년", "걸레같은년", "걸레년", "걸레핀년", "게부럴", "게세끼", "게이", "게새끼", "게늠", "게자식", "게지랄놈", "고환", "공지", "공지사항", "귀두", "깨쌔끼", "난자마셔", "난자먹어", "난자핧아", "내꺼빨아", "내꺼핧아", "내버지", "내자지", "내잠지", "내조지", "너거애비", "노옴", "누나강간", "니기미", "니뿡", "니뽕", "니씨브랄", "니아범", "니아비", "니애미", "니애뷔", "니애비", "니할애비", "닝기미", "닌기미", "니미", "닳은년", "덜은새끼", "돈새끼", "돌으년", "돌은넘", "돌은새끼", "동생강간", "동성애자", "딸딸이", "똥구녁", "똥꾸뇽", "똥구뇽", "똥", "띠발뇬", "띠팔", "띠펄", "띠풀", "띠벌", "띠벨", "띠빌", "마스터", "막간년", "막대쑤셔줘", "막대핧아줘", "맛간년", "맛없는년", "맛이간년", "멜리스", "미친구녕", "미친구멍", "미친넘", "미친년", "미친놈", "미친눔", "미친새끼", "미친쇄리", "미친쇠리", "미친쉐이", "미친씨부랄", "미튄", "미티넘", "미틴", "미틴넘", "미틴년", "미틴놈", "미틴것", "백보지", "버따리자지", "버지구녕", "버지구멍", "버지냄새", "버지따먹기", "버지뚫어", "버지뜨더", "버지물마셔", "버지벌려", "버지벌료", "버지빨아", "버지빨어", "버지썰어", "버지쑤셔", "버지털", "버지핧아", "버짓물", "버짓물마셔", "벌창같은년", "벵신", "병닥", "병딱", "병신", "보쥐", "보지", "보지핧어", "보짓물", "보짓물마셔", "봉알", "부랄", "불알", "붕알", "붜지", "뷩딱", "븅쉰", "븅신", "빙띤", "빙신", "빠가십새", "빠가씹새", "빠구리", "빠굴이", "뻑큐", "뽕알", "뽀지", "뼝신", "사까시", "상년", "새꺄", "새뀌", "새끼", "색갸", "색끼", "색스", "색키", "샤발", "써글", "써글년", "성교", "성폭행", "세꺄", "세끼", "섹스", "섹스하자", "섹스해", "섹쓰", "섹히", "수셔", "쑤셔", "쉐끼", "쉑갸", "쉑쓰", "쉬발", "쉬방", "쉬밸년", "쉬벌", "쉬불", "쉬붕", "쉬빨", "쉬이발", "쉬이방", "쉬이벌", "쉬이불", "쉬이붕", "쉬이빨", "쉬이팔", "쉬이펄", "쉬이풀", "쉬팔", "쉬펄", "쉬풀", "쉽쌔", "시댕이", "시발", "시발년", "시발놈", "시발새끼", "시방새", "시밸", "시벌", "시불", "시붕", "시이발", "시이벌", "시이불", "시이붕", "시이팔", "시이펄", "시이풀", "시팍새끼", "시팔", "시팔넘", "시팔년", "시팔놈", "시팔새끼", "시펄", "실프", "십8", "십때끼", "십떼끼", "십버지", "십부랄", "십부럴", "십새", "십세이", "십셰리", "십쉐", "십자석", "십자슥", "십지랄", "십창녀", "십창", "십탱", "십탱구리", "십탱굴이", "십팔새끼", "ㅆㅂ", "ㅆㅂㄹㅁ", "ㅆㅂㄻ", "ㅆㅣ", "쌍넘", "쌍년", "쌍놈", "쌍눔", "쌍보지", "쌔끼", "쌔리", "쌕스", "쌕쓰", "썅년", "썅놈", "썅뇬", "썅늠", "쓉새", "쓰바새끼", "쓰브랄쉽세", "씌발", "씌팔", "씨가랭넘", "씨가랭년", "씨가랭놈", "씨발", "씨발년", "씨발롬", "씨발병신", "씨방새", "씨방세", "씨밸", "씨뱅가리", "씨벌", "씨벌년", "씨벌쉐이", "씨부랄", "씨부럴", "씨불", "씨불알", "씨붕", "씨브럴", "씨블", "씨블년", "씨븡새끼", "씨빨", "씨이발", "씨이벌", "씨이불", "씨이붕", "씨이팔", "씨파넘", "씨팍새끼", "씨팍세끼", "씨팔", "씨펄", "씨퐁넘", "씨퐁뇬", "씨퐁보지", "씨퐁자지", "씹년", "씹물", "씹미랄", "씹버지", "씹보지", "씹부랄", "씹브랄", "씹빵구", "씹뽀지", "씹새", "씹새끼", "씹세", "씹쌔끼", "씹자석", "씹자슥", "씹자지", "씹지랄", "씹창", "씹창녀", "씹탱", "씹탱굴이", "씹탱이", "씹팔", "아가리", "애무", "애미", "애미랄", "애미보지", "애미씨뱅", "애미자지", "애미잡년", "애미좃물", "애비", "애자", "양아치", "어미강간", "어미따먹자", "어미쑤시자", "영자", "엄창", "에미", "에비", "엔플레버", "엠플레버", "염병", "염병할", "염뵹", "엿먹어라", "오랄", "오르가즘", "왕버지", "왕자지", "왕잠지", "왕털버지", "왕털보지", "왕털자지", "왕털잠지", "우미쑤셔", "운디네", "운영자", "유두", "유두빨어", "유두핧어", "유방", "유방만져", "유방빨아", "유방주물럭", "유방쪼물딱", "유방쪼물럭", "유방핧아", "유방핧어", "육갑", "이그니스", "이년", "이프리트", "자기핧아", "자지", "자지구녕", "자지구멍", "자지꽂아", "자지넣자", "자지뜨더", "자지뜯어", "자지박어", "자지빨아", "자지빨아줘", "자지빨어", "자지쑤셔", "자지쓰레기", "자지정개", "자지짤라", "자지털", "자지핧아", "자지핧아줘", "자지핧어", "작은보지", "잠지", "잠지뚫어", "잠지물마셔", "잠지털", "잠짓물마셔", "잡년", "잡놈", "저년", "점물", "젓가튼", "젓가튼쉐이", "젓같내", "젓같은", "젓까", "젓나", "젓냄새", "젓대가리", "젓떠", "젓마무리", "젓만이", "젓물", "젓물냄새", "젓밥", "정액마셔", "정액먹어", "정액발사", "정액짜", "정액핧아", "정자마셔", "정자먹어", "정자핧아", "젖같은", "젖까", "젖밥", "젖탱이", "조개넓은년", "조개따조", "조개마셔줘", "조개벌려조", "조개속물", "조개쑤셔줘", "조개핧아줘", "조까", "조또", "족같내", "족까", "족까내", "존나", "존나게", "존니", "졸라", "좀마니", "좀물", "좀쓰레기", "좁빠라라", "좃가튼뇬", "좃간년", "좃까", "좃까리", "좃깟네", "좃냄새", "좃넘", "좃대가리", "좃도", "좃또", "좃만아", "좃만이", "좃만한것", "좃만한쉐이", "좃물", "좃물냄새", "좃보지", "좃부랄", "좃빠구리", "좃빠네", "좃빠라라", "좃털", "좆같은놈", "좆같은새끼", "좆까", "좆까라", "좆나", "좆년", "좆도", "좆만아", "좆만한년", "좆만한놈", "좆만한새끼", "좆먹어", "좆물", "좆밥", "좆빨아", "좆새끼", "좆털", "좋만한것", "주글년", "주길년", "쥐랄", "지랄", "지랼", "지럴", "지뢀", "쪼까튼", "쪼다", "쪼다새끼", "찌랄", "찌질이", "창남", "창녀", "창녀버지", "창년", "처먹고", "처먹을", "쳐먹고", "쳐쑤셔박어", "촌씨브라리", "촌씨브랑이", "촌씨브랭이", "크리토리스", "큰보지", "클리토리스", "트랜스젠더", "페니스", "항문수셔", "항문쑤셔", "허덥", "허버리년", "허벌년", "허벌보지", "허벌자식", "허벌자지", "허접", "허젚", "허졉", "허좁", "헐렁보지", "혀로보지핧기", "호냥년", "호로", "호로새끼", "호로자슥", "호로자식", "호로짜식", "호루자슥", "호모", "호졉", "호좁", "후라덜넘", "후장", "후장꽂아", "후장뚫어", "흐접", "흐젚", "흐졉", "bitch", "fuck", "fuckyou", "nflavor", "penis", "pennis", "pussy", "sex"]; // 욕설 및 비속어를 배열로 추가	
    const normalizedValue = value.toLowerCase(); // 입력값을 소문자로 표준화
    
    // 비속어가 있는지 여부와 첫 번째로 발견된 위치를 반환
    for(let word of profanityList) {
		let index = normalizedValue.indexOf(word);
		if(index !== -1) {
			return { found: true, position: index, length: word.length }
		}
	}
    return { found: false };
}

// input number "E,e,-,+,."키(Exponential Notation) 입력 제어 / input type="number"는 기본적으로 지수 표기법을 지원하기 때문
function blockEKey(event) {
	if (event.key === 'E' || event.key === 'e' || event.key === '-' || event.key === '+' || event.key === '.') {
        event.preventDefault(); // 기본 동작 차단
    }
}

// 휴대폰 번호 형식으로 변환
function replacePhone(input) {
	const phoneValue = input.value.replace(/[^0-9]/g, "").replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(-{1,2})$/g, ""); // (01x-xxxx-xxxx)
	input.value = phoneValue;
	return phoneValue;
}

// input number에 min, max 기반 입력값 제어하여 변환
function replaceNumber(input) {
	const cleanNumberValue = input.value.replace(/[^0-9.]/g, ""); // 문자값이 입력이 되었을 경우 숫자를 제외한 값 제거	
	// isNaN메소드로 Not-a-Number인지 여부를 확인
	// 예외 적인 오류 사항 대비 후미열에 문자값 입력이 되었을 시 처리가능한 parseFloat메소드 사용
	const min = isNaN(parseFloat(input.min)) ? -Infinity : parseFloat(input.min); // 최소값이 없으면 무한 작은 값
	const max = isNaN(parseFloat(input.max)) ? Infinity : parseFloat(input.max); // 최대값이 없으면 무한 큰 값

	let number = cleanNumberValue;
	if(cleanNumberValue < min) { // min 보다 작으면 min으로 설정
		number = min;
	}
	
	if(cleanNumberValue > max) { // max 보다 크면 max으로 설정
		number = max;
	}
	
	input.value = number;
	
	return number;
}

// input date에 min, max 기반 입력값 제어하여 변환
function replaceDate(input) {
	validateEmpty(input, '생년월일을')
	
	if (input.value.length < 8) {
		return false;
	}
	
    const inputDate = new Date(input.value);
    
    if (input.min) { // min 값이 존재할 때만 처리
        const minDate = new Date(input.min);
        if (inputDate.getFullYear() < minDate.getFullYear()) { // 입력한 연도가 minDate보다 이전일 때
            // 연도를 minDate로 변경하고 월과 일은 유지
            inputDate.setFullYear(minDate.getFullYear());
            input.value = inputDate.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환
            alert(`${minDate.getFullYear()}년도 이후로 입력해 주세요.`);
        }
    }

    if (input.max) { // max 값이 존재할 때만 처리
        const maxDate = new Date(input.max);
        if (inputDate.getFullYear() > maxDate.getFullYear()) { // 입력한 연도가 maxDate보다 이후일 때
            // 연도를 maxDate로 변경하고 월과 일은 유지
            inputDate.setFullYear(maxDate.getFullYear());
            input.value = inputDate.toISOString().split('T')[0]; // YYYY-MM-DD 형식으로 변환
            alert(`${maxDate.getFullYear()}년도 이전으로 입력해 주세요.`);
        }
    }
}
