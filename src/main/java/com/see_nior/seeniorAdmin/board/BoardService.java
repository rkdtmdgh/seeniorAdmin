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
	
	public Object getList() {
		log.info("getList()");
		
		Map<String, Object> cateDtos = new HashMap<>();
		
		List<BoardCategoryDto> boardCategoryDtos = (List<BoardCategoryDto>) boardMapper.getList();
		
		cateDtos.put("boardCategoryDtos", boardCategoryDtos);
						
		return cateDtos;
	}

}
