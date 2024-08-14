// 관리자 계정 리스트 요청
export function getAdminList(page) {
	const seartStringInput = document.forms['search_form'].search_string;
	seartStringInput.value = '';
	let intPage = page || 1;
	
	$.ajax({
		url: '/account/get_admin_list',
		method: 'GET',
		data: {
			page: intPage,
		},
	})
	.then(response => {
		console.log('getAdminList() response:', response);
		const contentTable = document.querySelector('.content_table tbody');
		contentTable.innerHTML = '';
		
		if(response && response.adminAccountDtos) {
			let adminListCnt = response.adminListPage.accountListCnt;

			response.adminAccountDtos.forEach((data) => { 
				let innerContent = `
					<tr>
                        <td>
                            <p class="table_info">${adminListCnt}</p>
                        </td>
                        <td>
                            <a href="" class="table_info">${data.a_id}</a>
                        </td>
                        <td>
                            <a href="" class="table_info">${data.a_authority_role}</a>
                        </td>
                        <td>
                            <p class="table_info">${data.a_phone}</p>
                        </td>
                        <td>
                            <p class="table_info">${data.a_name}</p>
                        </td>
                        <td>
                            <p class="table_info">${formatDate(data.a_reg_date)}</p>
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