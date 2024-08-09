document.addEventListener('DOMContentLoaded', () => {
	// 게시판 리스트 요청
	function getBoardList() {
		$.ajax({
			url: '/board/get_list',
			method: 'GET',
		})
		.then(response => {
			// side_sub_menu_list에 응답 데이터를 기반으로 항목 추가
			const sideBoardSubMenu = document.getElementById('side_board_sub_menu');
			sideBoardSubMenu.innerHTML = ""; // 예외 상황에 대비하여 자식 요소 제거
			
			response.data.filter(data => data.IS_DELETED === 1) // IS_DELETED가 1인(정상) 데이터만 필터링
			.forEach((data) => {
				let subMenuLink = document.createElement('a');
				subMenuLink.className = 'side_sub_menu_btn';
				subMenuLink.href = '/board/board_list?no=' + data.NO;
				
				let subMenuName = document.createElement('span');
				subMenuName.className = 'side_sub_menu';
				subMenuName.textContent = data.NAME;
				
				subMenuLink.appendChild(subMenuName);
				sideBoardSubMenu.appendChild(subMenuLink);
			});
		})
		.catch((error) => {
			console.error('getBoardList() error:', error);
		});
	}
	
	getBoardList();
});

