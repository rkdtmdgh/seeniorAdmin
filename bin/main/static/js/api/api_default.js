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
			setNavActiveToggle(); // NAV 선택 표시 및 토글
		}
	}

	getBoardList(); // 게시판 리스트 요청 후 NAV SET
});