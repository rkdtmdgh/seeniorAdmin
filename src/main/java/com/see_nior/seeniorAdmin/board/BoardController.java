package com.see_nior.seeniorAdmin.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;

import lombok.extern.log4j.Log4j2;


@Log4j2
@Controller
@RequestMapping("/board")
public class BoardController {
	
	final private BoardService boardService;
	
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	//모든 게시판 항목 가져오기
	@GetMapping("/cate_info/get_list")
	@ResponseBody
	public Object getList() {		
		log.info("getList()");
		
		return boardService.getList();
	}
	
	//게시판 관리 양식으로 이동
	@GetMapping("/cate_info/board_list_form")
	public String boardListForm() {
		log.info("boardListForm()");
		
		String nextPage = "board/board_list_form";
		
		return nextPage;
	}
	
	//게시판 생성 양식으로 이동
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm() {
		log.info("createCategoryForm()");
		
		String nextPage = "board/create_category_form";
		
		return nextPage;
	}
	
	//게시판 생성 요청 처리
	@PostMapping("/cate_info/create_category_confirm")
	public String createCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("createCategoryConfirm()");
		
		int result = boardService.createCategoryConfirm(boardCategoryDto);
		
		return null;
	}
	
	
	

}
