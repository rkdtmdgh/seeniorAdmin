// 로그아웃
function signOut() {
	logger.info('signOut()');
	const isConfirm = confirm('로그아웃하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	location.replace('/account/sign_out_confirm');
}

// 계정 삭제
function deleteAccount(a_no, a_id) {
	logger.info('deleteAccount()', a_no, a_id);
	const isConfirm = confirm(a_id + ' 계정을 삭제하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	location.replace('/account/delete_confirm?a_no=' + a_no);
}

// 날짜 포맷팅
function formatDate(dateString) { // yyyy-mm-dd 형식
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2); // 월은 0부터 시작하므로 +1
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
}

document.addEventListener('DOMContentLoaded', () => {
	// 정렬 버튼 세팅(새로 고침 후 버튼 기능 정상화)
	const urlParams = new URLSearchParams(window.location.search);
	const sort = urlParams.get('sort');
	const sortValue = urlParams.get('sortValue');
	
	if(sort) {
		const sortBtn = document.querySelector(`.table_title[data-sort = "${sort}"]`);
		sortBtn.setAttribute('data-sort', sortValue || sort);
	}
	
	// NAV 선택 표시 및 토글
	const currentPath = window.location.pathname; // 현재 URL
	logger.info('URl:', currentPath);
	
	const navMenulistbtns = document.querySelectorAll('.side_menu_list'); // 모든 사이드 메뉴 버튼의 부모 요소
	navMenulistbtns.forEach((navMenulistbtn, index) => {
		const navMenuBtn = navMenulistbtn.querySelector('.side_menu_btn'); // 메뉴 버튼
		logger.info(index + '. href:', navMenuBtn.getAttribute('href'));
		
		// 서브 메뉴가 없는 경우
		if(navMenuBtn && navMenuBtn.getAttribute('href') === currentPath) { // 버튼이 유효하며 href가 현재 경로가 일치할 경우
			navMenulistbtn.classList.add('select'); // 네비 버튼 선택
		}
		
		// 서브 메뉴가 있는 경우
		const navSubBtns = navMenulistbtn.querySelectorAll('.side_sub_menu_btn');
		navSubBtns.forEach(navSubBtn => {
			if(navSubBtn.getAttribute('href') === currentPath) {
				navSubBtn.classList.add('on'); // 네비 서브 메뉴 선택
				navMenulistbtn.classList.add('select'); // 네비 버튼 선택
			}
		});
		
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
	
	// 게시판 리스트 요청
	function getBoardList() {
		$.ajax({
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
	getBoardList();	
});