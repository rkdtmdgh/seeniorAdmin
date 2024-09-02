// 로그아웃
function signOut() {
	logger.info('signOut()');
	
	const isConfirm = confirm('로그아웃하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	
	location.replace('/account/sign_out_confirm');
}

$(document).ready(function() {
	// NAV 선택 표시 및 토글
	function setNavActiveToggle() {
		const currentPath = window.location.pathname; // 현재 URL
		const currentFirstPath = currentPath.split('/')[1] || ''; // 첫 번째 path
		logger.info('URl:', currentPath);
		logger.info('URl first path:', currentFirstPath);

		const $navMenuBtns = $('.side_menu_list'); // 모든 nav 메뉴 버튼
		$navMenuBtns.each(function(index) { // 화살표 함수에는 this가 포함되지 않아 function으로 대체
			const $navMenu = $(this);
			const $navMenuBtn = $navMenu.find('.side_menu_btn');
			const href = $navMenuBtn.attr('href') || null; // 메뉴 버튼의 href
			logger.info(index + '. href:', href);
			
			if (href && href === currentPath) { // 서브 메뉴가 없고 href가 현재 경로가 일치할 경우
				$navMenu.addClass('select');
			}
			
			const $navSubMenuList = $navMenu.find('.side_sub_menu_list'); // 서브 메뉴 리스트
			if($navSubMenuList.length) { // 서브 메뉴가 있는 경우
				const $navSubMenus = $navSubMenuList.find('.side_sub_menu_btn'); // 서브 메뉴 리스트 포함된 모든 서브 메뉴
				$navSubMenus.each(function() {
					const $navSubMenu = $(this);
					const subHref = $navSubMenu.attr('href') || null;
					const subHrefFirstPath = subHref ? subHref.split('/')[1] || null : null; // 첫 번째 path
					
					if (subHref && subHrefFirstPath === currentFirstPath) { // 첫 번째 path와 URL 첫 번째 path와 같을 경우
						$navSubMenu.addClass('on'); // 네비 서브 메뉴 선택
						$navMenu.addClass('select'); // 네비 버튼 선택
					}
					
					logger.info(index + '. subHref:', subHrefFirstPath);
				});
			}
			
			// 서브 메뉴 토글
			const $subMenuToggle = $navMenu.find('.arrow');
			if ($subMenuToggle.length) {
				$subMenuToggle.click(function() {
					const $siblingsSubMenuList = $(this).siblings('.side_sub_menu_list');
                
	                if (!$(this).parent().hasClass('select')) {
	                    $(this).toggleClass('on');
	                    $siblingsSubMenuList.toggleClass('open');
	                }
				});
			}
		});
	}

	// 게시판 리스트 요청
	async function getBoardList() {
		try {
			const response = await $.ajax({
				url: '/board/get_list',
				method: 'GET',
			});

			logger.info('getBoardList() response:', response);

			const $sideBoardSubMenu = $('#side_board_sub_menu');

			if (response && response.boardCategoryDtos) {
				let filterData = response.boardCategoryDtos.filter(data => data.bc_is_deleted === true);
				filterData.forEach((data) => { 
					let innerContent = `
						<a href="/board/board_list?${data.bc_no}" class="side_sub_menu_btn">
							<span class="side_sub_menu">${data.bc_name}</span>
						</a>
					`;
					
					$sideBoardSubMenu.append(innerContent);
				});
				
			} else {
				logger.info('데이터가 없거나 유효하지 않습니다.');
			}
			
		} catch(error) {
			logger.error('getBoardList() error:', error);
			
		} finally {
			setNavActiveToggle(); // NAV 선택 표시 및 토글 세팅
		}
	}

	getBoardList(); // 게시판 리스트 요청 후 NAV SET
});