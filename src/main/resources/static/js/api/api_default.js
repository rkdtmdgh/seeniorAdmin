// 로그아웃
function signOut() {
	logger.info('signOut()');
	const isConfirm = confirm('로그아웃하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	location.replace('/account/sign_out_confirm');
}

// all_check 체크박스 초기화
function  resetAllcheck() {
	const allCheckBox = document.querySelector('input[type="checkbox"][name="all_check"]');
	if(allCheckBox && allCheckBox.checked === true) allCheckBox.checked = false;
}

document.addEventListener('DOMContentLoaded', () => {
	// 게시판 리스트 요청
	function getBoardList() {
		return $.ajax({
			url: '/board/get_list',
			method: 'GET',
		})
		.then(response => {
			logger.info('getBoardList() response:', response);
			const sideBoardSubMenu = document.getElementById('side_board_sub_menu');
			
			if(response && response.boardCategoryDtos) {
				let filterData = response.boardCategoryDtos.filter(data => data.bc_is_deleted === true);
				
				filterData.forEach((data) => { 
					let innerContent = `
						<a href="/board/board_list?${data.bc_no}" class="side_sub_menu_btn">
							<span class="side_sub_menu">${data.bc_name}</span>
						</a>
					`;
					sideBoardSubMenu.innerHTML += innerContent;
				});
			} else {
				logger.info('데이터가 없거나 유효하지 않습니다.');
			}
		})
		.catch((error) => {
			logger.error('getBoardList() error:', error);
		});
	}
	
	getBoardList().then(() => {
		// NAV 선택 표시 및 토글
		const currentPath = window.location.pathname; // 현재 URL
		const currentFirstPath = currentPath.split('/')[1] || ''; // 첫 번째 path
		logger.info('URl:', currentPath);
		logger.info('URl first path:', currentFirstPath);
		
		const navMenulistbtns = document.querySelectorAll('.side_menu_list'); // 모든 사이드 메뉴 버튼의 부모 요소
		navMenulistbtns.forEach((navMenulistbtn, index) => {
			const navMenuBtn = navMenulistbtn.querySelector('.side_menu_btn'); // 메뉴 버튼
			const href = navMenuBtn.getAttribute('href') != '' ? navMenuBtn.getAttribute('href') : null; // 메뉴 버튼의 href
			logger.info(index + '. href:', href);
			
			if(href && href === currentPath) { // 서브 메뉴가 없고 href가 현재 경로가 일치할 경우
				navMenulistbtn.classList.add('select');
			}
			
			if(!href) { // 서브 메뉴가 있는 경우
				const navSubBtns = navMenulistbtn.querySelectorAll('.side_sub_menu_btn');
				navSubBtns.forEach(navSubBtn => {
					const subHref = navSubBtn.getAttribute('href') != '' ? navSubBtn.getAttribute('href') : null;
					const subHrefFirstPath = subHref ? subHref.split('/')[1] : ''; // 첫 번째 path
					if(subHref && subHrefFirstPath === currentFirstPath) { // 첫 번째 path와 URL 첫번쨰 path와 같을 경우
						navSubBtn.classList.add('on'); // 네비 서브 메뉴 선택
						navMenulistbtn.classList.add('select'); // 네비 버튼 선택
					}
					logger.info(index + '. subHref:', subHrefFirstPath);
				});
			}
			
			// 서브 메뉴 토글
			const subMenuToggle = navMenulistbtn.querySelector('.arrow');
			if(subMenuToggle) {
				subMenuToggle.addEventListener('click', () => {
					const subMenuList = navMenulistbtn.querySelector('.side_sub_menu_list');
									
					// 클릭된 요소에만 토글
					if(!navMenulistbtn.classList.contains('select')) {
						navMenuBtn.classList.toggle('on');
						subMenuList.classList.toggle('open');
					}
				});
			}
		});
	});	
});