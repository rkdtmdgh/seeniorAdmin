package com.see_nior.seeniorAdmin.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;


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
	@ResponseBody
	public Object getList() {		
		log.info("getList()");
		
		return boardService.getList();
	}
	
	//게시판 생성 양식으로 이동
	@GetMapping("/create_form")
	public String createForm() {
		log.info("createForm()");
		
		String nextPage = "board/create_form";
		
		return nextPage;
	}
	
	//게시판 생성 요청 처리
	@PostMapping("/create_confirm")
	public String createConfirm() {
		log.info("createConfirm()");
		
		return null;
	}
	
	
	

}
