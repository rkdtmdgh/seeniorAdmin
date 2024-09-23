package com.see_nior.seeniorAdmin.board.util;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.board.mapper.BoardMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BoardItemCntUpdater {
	
final private BoardMapper boardMapper;
	
	public BoardItemCntUpdater( BoardMapper boardMapper ) {
		this.boardMapper = boardMapper;
	}
	
	//BOARD_POSTS 테이블에서 bc_no값으로 게시판에 게시물 갯수 가져오기
	public int selectCountBoardPostsByBcNo(int bc_no) {
		log.info("selectCountBoardPostsByBcNo()");
						
		try {
			
			int bc_item_cnt = boardMapper.selectCountBoardPostsByBcNo(bc_no);
			
            return bc_item_cnt;
            
        } catch (Exception e) {
            // 예외 발생 시 처리 로직
            log.error("게시판에 게시물 갯수 가져오기 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("게시판에 게시물 갯수 가져오기 조회 실패");
        }
		
	}
	
	//BOARD_CATEGORY 테이블에 bc_no 값으로 bc_item_cnt 컬럼 업데이트 하기
	public boolean updateBoardCategoryForBcItemCntByBcNo(int bc_no, int bc_item_cnt) {
		log.info("updateBoardCategoryForBcItemCntByBcNo()");
		
		int result = boardMapper.updateBoardCategoryForBcItemCntByBcNo(bc_no, bc_item_cnt);
		
		if(result <= 0) {
			log.info("updateBoardCategoryForBcItemCntByBcNo() Error!!");
			
			return false;
		}else {
			log.info("updateBoardCategoryForBcItemCntByBcNo() Success");
			
			return true;
		}
		
	}
	
}
