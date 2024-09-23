package com.see_nior.seeniorAdmin.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.board.mapper.BoardMapper;
import com.see_nior.seeniorAdmin.board.util.BoardItemCntUpdater;
import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BoardService {
	
	final private BoardMapper boardMapper;
	final private BoardItemCntUpdater boardItemCntUpdater;
	
	public BoardService( BoardMapper boardMapper, BoardItemCntUpdater boardItemCntUpdater ) {
		this.boardMapper = boardMapper;
		this.boardItemCntUpdater = boardItemCntUpdater;
	}
	
	//모든 게시판 항목 가져오기
	public Object getList() {
		log.info("getList()");
				
		Map<String, Object> cateDtos = new HashMap<>();
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getList();
		
		cateDtos.put("boardCategoryDtos", boardCategoryDtos);
						
		return cateDtos;
	}
	
	//게시판명 중복 확인
	public boolean isBoardCategory(BoardCategoryDto boardCategoryDto) {
		log.info("isBoardCategory()");
		
		String bc_name = boardCategoryDto.getBc_name();
		
		boolean result = boardMapper.isBoardCategory(bc_name);
		
		return result;
	}
	
	//일반 게시판 마지막 순서(idx) 번호 가져오기
	public int getBoardCategoryIdxMaxNum() {
		log.info("getBoardCategoryIdxMaxNum()");
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryIdxMaxNum();
		
//		int IdxMaxNum = boardCategoryDtos.getFirst().getBc_idx();
		int IdxMaxNum = boardCategoryDtos.get(0).getBc_idx();
		
		return IdxMaxNum;
	}
	
	//게시판 생성 요청 처리
	public boolean createCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("createCategoryConfirm()");
					
		int bc_idx = boardCategoryDto.getBc_idx();
					
		log.info("bc_idx: ",bc_idx);
		
		//DB에서 board category 마지막 idx값 꺼내오기
		int result = boardMapper.updateBoardCategoryIdx(bc_idx);
		
		if(result < 0) {
			
			log.info("updateBoardCategoryIdx() fail !!");
			
			return false;
			
		}else {
			
			//새로운 게시판 DB에 insert
			result = boardMapper.createBoardCategory(boardCategoryDto);
			
			if(result > 0) {
				log.info("createBoardCategory succuss!!");
				return true;
			}else {
				log.info("createBoardCategory fail!!");
				return false;
			}
			
		}
						
	}
	
	//게시판 name,idx 수정을 위한 dto 요청
	public List<BoardCategoryDto> getBoardCategoryForModify(BoardCategoryDto boardCategoryDto) {
		log.info("getBoardCategoryForModify()");
		
		int bc_no = boardCategoryDto.getBc_no();
		
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryForModify(bc_no);
						
		return boardCategoryDtos;
	}
	
	//특정 게시판 카테고리 정보 가져오기
	public Object getBoardInfo(int bc_no) {
		log.info("getBoardInfo()");
		
		//no값으로 dto가져오는 코드 재사용
		List<BoardCategoryDto> boardCategoryDtos = boardMapper.getBoardCategoryForModify(bc_no);
		
		Map<String, Object> boardCategoryDto = new HashMap<>();
		boardCategoryDto.put("boardCategoryDto", boardCategoryDtos.get(0));				;
		
		return boardCategoryDto;
	}

		

	

}
