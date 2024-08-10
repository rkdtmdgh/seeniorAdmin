document.addEventListener('DOMContentLoaded', () => {
	// 게시판 리스트 요청
	function getBoardList() {
		$.ajax({
			url: '/board/get_list',
			method: 'GET',
		})
		.then(response => {
			console.log('getBoardList() response:', response);
			
			// side_sub_menu_list에 응답 데이터를 기반으로 항목 추가
			const sideBoardSubMenu = document.getElementById('side_board_sub_menu');
			
			if(response && response.data) {
				let filterData = response.filter(data => data.IS_DELETE === 1) // IS_DELETED가 1인(정상) 데이터만 필터링
				.sort((a, b) => a.IDX - b.IDX); // 데이터를 IDX 오름차순으로 정렬
				
				filterData.forEach((data) => {
					let subMenuLink = document.createElement('a');
					subMenuLink.className = 'side_sub_menu_btn';
					subMenuLink.href = '/board/board_list?no=' + data.NO;
					
					let subMenuName = document.createElement('span');
					subMenuName.className = 'side_sub_menu';
					subMenuName.textContent = data.NAME;
					
					subMenuLink.appendChild(subMenuName);
					sideBoardSubMenu.appendChild(subMenuLink);
				});
			} else {
				console.error('데이터가 없거나 유효하지 않습니다.');
			}
		})
		.catch((error) => {
			console.error('getBoardList() error:', error);
		});
	}
	
	getBoardList();
});

