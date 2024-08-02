package com.see_nior.seeniorAdmin.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Log4j2
@Controller
@RequestMapping("/board")
public class BoardController {
	
	final private BoardService boardService;
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	//모든 게시판 항목 가져오기(비동기?)
	@GetMapping("/get_list")
	public String get_list(@RequestParam String param) {
		log.info("get_list()");
		
		
		return null;
	}
	
	//게시판 생성 양식으로 이동
	@GetMapping("/create_form")
	public String create_form() {
		log.info("create_form()");
		
		String nextPage = "board/create_form";
		
		return nextPage;
	}
	
	

}
