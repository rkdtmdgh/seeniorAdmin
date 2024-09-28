package com.see_nior.seeniorAdmin.board;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
	@GetMapping("/cate_info/category_list_form")
	public String categoryListForm() {
		log.info("categoryListForm()");
		
		String nextPage = "board/category_list_form";
		
		return nextPage;
	}
	
	//게시판 생성 양식으로 이동
	@GetMapping("/cate_info/create_category_form")
	public String createCategoryForm(Model model) {
		log.info("createCategoryForm()");
		
		int boardCategoryIdxMaxNum = boardService.getBoardCategoryIdxMaxNum();
		
		model.addAttribute("boardCategoryIdxMaxNum", boardCategoryIdxMaxNum);
		
		String nextPage = "board/create_category_form";
		
		return nextPage;
	}
	
	//게시판명 중복 확인
	@GetMapping("/cate_info/is_board_category")
	@ResponseBody
	public boolean isBoardCategory(BoardCategoryDto boardCategoryDto) {
		log.info("isBoardCategory()");
		
		boolean result = boardService.isBoardCategory(boardCategoryDto);
		
		return result;
	}
	
	
	//게시판 생성 요청 처리
	@PostMapping("/cate_info/create_category_confirm")
	@ResponseBody
	public boolean createCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("createCategoryConfirm()");
		
		boolean result = boardService.createCategoryConfirm(boardCategoryDto);
		
		return result;
	}
	
	//게시판 name,idx 수정 양식으로 이동
	@GetMapping("/cate_info/modify_category_form")
	public String modifyCategoryForm(BoardCategoryDto boardCategoryDto, Model model) {
		log.info("modifyCategoryForm()");
		
		List<BoardCategoryDto> boardCategoryDtosForModify = boardService.getBoardCategoryForModify(boardCategoryDto);
		
		int boardCategoryIdxMaxNum = boardService.getBoardCategoryIdxMaxNum();
		
		model.addAttribute("boardCategoryDtoForModify",boardCategoryDtosForModify.get(0));
		model.addAttribute("boardCategoryIdxMaxNum", boardCategoryIdxMaxNum);
		
		String nextPage = "board/modify_category_form";
		
		return nextPage;
	}
	
	//게시판 게시물 리스트 양식
	@GetMapping("/info/posts_list_form")
	public String postsListForm(@RequestParam("infoNo") int bc_no, Model model) {
		log.info("postsListForm()");
		
		model.addAttribute("infoNo", bc_no);
		
		String nextPage = "board/posts_list_form";
		
		return nextPage;
	}
	
	//특정 게시판 카테고리 정보 가져오기
	@GetMapping("/info/get_board_info")
	@ResponseBody
	public Object getBoardInfo(@RequestParam("infoNo") int bc_no) {
		log.info("getBoardInfo()");
				
		return boardService.getBoardInfo(bc_no);
	}
	
	//게시판 게시물 작성 양식
	@GetMapping("/info/create_form")
	public String createForm(@RequestParam("infoNo") int bp_category_no, Model model) {
		log.info("createForm()");
			
		model.addAttribute("bp_category_no", bp_category_no);
			
		String nextPage = "board/create_form";
			
		return nextPage;
	}
	
	//작성한 일반 게시물 등록 요청
	@PostMapping("/info/create_confirm")
	@ResponseBody
	public boolean createConfirm(@RequestParam("files") List<MultipartFile> files, 
								@RequestParam("bp_category_no") int bp_category_no, 
								@RequestParam("bp_writer_no") int bp_writer_no) {
		log.info("createConfirm()");
		
		log.info("files: {}",files.size());
		log.info("bp_category_no: {}",bp_category_no);
		log.info("bp_writer_no: {}",bp_writer_no);
		
		ResponseEntity<String> savedFileNames = 
				boardService.uploadFiles(files,bp_category_no,bp_writer_no);
//		Object savedFileNames = boardService.uploadFiles(files);
		
		log.info("savedFileNames: {}",savedFileNames.getBody());
		
		if(savedFileNames.getBody() != null) {
			log.info("uploadFiles succuess!");
			
			log.info("uploadFiles succuess!");
			
			return true;
		}else {
			log.info("uploadFiles fail!");
			
			return false;
		}
		
	}
	
	//작성한 공지 게시물 등록 요청
	@PostMapping("/info/create_notice_confirm")
	@ResponseBody
	public String createNoticeConfirm(@RequestParam("files") List<MultipartFile> files, 
									@RequestParam("bp_category_no") int bp_category_no, 
									@RequestParam("bp_writer_no") int bp_writer_no) {
		log.info("createNoticeConfirm()");
		
		log.info("files: {}",files.size());
		log.info("bp_category_no: {}",bp_category_no);
		log.info("bp_writer_no: {}",bp_writer_no);
		
		return null;
	}
	
	

}
