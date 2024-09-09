package com.see_nior.seeniorAdmin.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.see_nior.seeniorAdmin.board.mapper.BoardMapper;
import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BoardService {
	
	final private BoardMapper boardMapper;
	
	public BoardService( BoardMapper boardMapper ) {
		this.boardMapper = boardMapper;
	}
	
	//모든 게시판 항목 가져오기
	public Object getList() {
		log.info("getList()");
		
		Map<String, Object> cateDtos = new HashMap<>();
		
		List<BoardCategoryDto> boardCategoryDtos = (List<BoardCategoryDto>) boardMapper.getList();
		
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
	public int createCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("createConfirm()");
					
		int bc_idx = boardCategoryDto.getBc_idx(); // 프론트에서 입력값이 없으면 0으로 넘겨줌 
		String bc_name = boardCategoryDto.getBc_name();
		
		if(bc_idx == 0) {
			log.info("bc_idx: ",bc_idx);
			// List<Integer> boardIdxs = boardMapper.getAllBoardIdx();
			// int lastBoradIdx = boardIdxs.getFirst();
			
			// int result = boardMapper.createBoardCategory(lastBoradIdx+1,bc_name);
			
		}else {
			log.info("bc_idx: ",bc_idx);
		}
		
		log.info("bc_idx --- {}", bc_idx);
		log.info("bc_name --- {}", bc_name);
				
		return 0;
	}

		

	

}
