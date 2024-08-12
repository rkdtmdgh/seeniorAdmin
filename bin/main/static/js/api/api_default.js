document.addEventListener('DOMContentLoaded', () => {
	
	// NAV 선택 표시 및 토글
	const currentPath = window.location.pathname; // 현재 URL
	console.log('URl:', currentPath);
	
	const navMenulistbtns = document.querySelectorAll('.side_menu_list'); // 모든 사이드 메뉴 버튼의 부모 요소
	navMenulistbtns.forEach((navMenulistbtn, index) => {
		const navMenuBtn = navMenulistbtn.querySelector('.side_menu_btn'); // 메뉴 버튼
		console.log(index + '. href:', navMenuBtn.getAttribute('href'));
		
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
			console.log('getBoardList() response:', response);
			const sideBoardSubMenu = document.getElementById('side_board_sub_menu');
			
			if(response && response.boardCategoryDtos) {
				let filterData = response.boardCategoryDtos.filter(data => data._deleted === true)
				.sort((a, b) => a.idx - b.idx); // 데이터를 IDX 오름차순으로 정렬
				
				filterData.forEach((data) => { 
					let innerContent = `
						<a href="/board/board_list?${data.no}" class="side_sub_menu_btn">
							<span class="side_sub_menu">${data.name}</span>
						</a>
					`;
					sideBoardSubMenu.innerHTML += innerContent;
				});
			} else {
				console.log('데이터가 없거나 유효하지 않습니다.');
			}
		})
		.catch((error) => {
			console.error('getBoardList() error:', error);
		});
	}
	
	getBoardList();
	
	
});