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
	
});