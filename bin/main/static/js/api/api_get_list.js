export function getAdminList() {
	$.ajax({
		url: '/account/get_admin_list',
		method: 'GET',
	})
	.then(response => {
		console.log('getAdminList() response:', response);
		
		// side_sub_menu_list에 응답 데이터를 기반으로 항목 추가
		const contentTable = document.querySelector('.content_table tbody');
		contentTable.innerHTML = '';
		
		if(response && response.adminAccountDtos) {
			let filterData = response.adminAccountDtos.filter(data => data._deleted === true)
			let adminListCnt = response.adminListPage.accountListCnt;

			filterData.forEach((data) => { 
				let innerContent = `
					<tr>
                        <td>
                            <p class="table_info">${adminListCnt}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.id}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.authority_role}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.phone}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.name}</p>
                        </td>
                        <td>
                            <p class="table_info">${formatDate(data.reg_date)}</p>
                        </td>
                    </tr>
				`;
				contentTable.innerHTML += innerContent;
				adminListCnt --;
			});
		} else {
			console.log('데이터가 없거나 유효하지 않습니다.');
		}
	})
	.catch((error) => {
		console.error('getAdminList() error:', error);
	});
}