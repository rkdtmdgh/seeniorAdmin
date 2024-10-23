$(function() {
	// 게시판 리스트 요청 후 NAV SET
	getBoardList(); 
});

// 게시판 리스트 요청
async function getBoardList() {
	try {
		const response = await $.ajax({
			url: '/board/cate_info/get_list',
			method: 'GET',
		});

		logger.info('getBoardList() response:', response);

		const $sideBoardSubMenu = $('#side_board_sub_menu');

		if (response && response.boardCategoryDtos) {
			response.boardCategoryDtos.forEach((data) => { 				
				let boardSubMenu = `
					<a href="/board/info/posts_list_form?infoNo=${data.bc_no}" class="side_sub_menu_btn">
						<span class="side_sub_menu">${data.bc_name}</span>
					</a>
				`;
				
				$sideBoardSubMenu.append(boardSubMenu);
			});
			
		} else {
			logger.info('데이터가 없거나 유효하지 않습니다.');
		}
		
	} catch(error) {
		logger.error('getBoardList() error:', error);
		
	} finally {
		navActiveToggle(); // NAV 선택 표시 및 토글
	}
}

// 로그아웃
function signOut() {
	logger.info('signOut()');
	
	const isConfirm = confirm('로그아웃하시겠습니까?');
	if(!isConfirm) {
		return false;	
	}
	
	sessionStorage.clear();
	location.replace('/account/sign_out_confirm');
}

// NAV 토글 및 페이지 관련 카테고리 메뉴 선택 표시
function navActiveToggle() {
	const currentPath = window.location.pathname.split('/').slice(0,3).join('/'); // 현재 URL에서 1~2번째까지 path 
	//logger.info('URL currentPath:', currentPath);

	const $navMenuBtns = $('.side_menu_list'); // 모든 nav 메뉴 버튼
	$navMenuBtns.each(function() { // 화살표 함수에는 this가 포함되지 않아 function으로 대체
		const $navMenu = $(this);
		const $navMenuBtn = $navMenu.find('.side_menu_btn');
		const href = $navMenuBtn.attr('href') || null; // 메뉴 버튼의 href
		const hrefPath = href ? new URL(href, window.location.origin).pathname.split('/').slice(0, 3).join('/') : ''; // href에서 1~2번째 path
		//logger.info('Href hrefPath:', hrefPath);
		
		if (hrefPath && hrefPath === currentPath) { // 서브 메뉴가 없고 href가 현재 경로가 일치할 경우
			$navMenu.addClass('select');
		}
		
		const $navSubMenuList = $navMenu.find('.side_sub_menu_list'); // 서브 메뉴 리스트
		if($navSubMenuList.length) { // 서브 메뉴가 있는 경우
			const currentParam = urlParams.get('infoNo'); // 카테고리 구분이 필요할 경우 비교할 값
		
			const $navSubMenus = $navSubMenuList.find('.side_sub_menu_btn'); // 서브 메뉴 리스트 포함된 모든 서브 메뉴
			$navSubMenus.each(function() {
				const $navSubMenu = $(this);
				const subHref = $navSubMenu.attr('href') || null;
				const subHrefPath = subHref ? new URL(subHref, window.location.origin).pathname.split('/').slice(0, 3).join('/') : ''; // 서브메뉴 href에서 1~2번째 path
				const subHrefQueryParams = new URLSearchParams(new URL(subHref, window.location.origin).search);
				//logger.info('Sub Href subHrefPath:', subHrefPath);
				
				if (subHrefPath && subHrefPath === currentPath) {
					// 쿼리 스트링 확인(같은 카테고리에서 구분이 필요할 경우)
					const subHerfParam = subHrefQueryParams.get('infoNo');
					
					if(!subHerfParam) { // subHerfParam에 infoNo가 없을 경우 비교하지 않음
						$navSubMenu.addClass('on'); // 네비 서브 메뉴 선택
						$navMenu.addClass('select'); // 네비 버튼 선택
						
					} else {
						if(subHerfParam === currentParam) {
							$navSubMenu.addClass('on'); 
							$navMenu.addClass('select'); 
						}
					}
				}
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