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
			
			if(response && response.boardCategoryDtos) {
				let filterData = response.boardCategoryDtos.filter(data => data._deleted === true) // IS_DELETED가 1인(정상) 데이터만 필터링
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

