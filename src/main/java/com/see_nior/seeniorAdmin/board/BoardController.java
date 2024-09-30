package com.see_nior.seeniorAdmin.board;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		log.info("infoNo: {}",bc_no);
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
	public boolean createConfirm(@RequestParam(value = "files" , required = false) List<MultipartFile> files, 
								@RequestParam("bp_category_no") int bp_category_no, 
								@RequestParam("bp_writer_no") int bp_writer_no,
								@RequestParam("bp_title") String bp_title,
								@RequestParam("bp_body") String bp_body) {
		log.info("createConfirm()");
		
		Boolean result = false;
		
		//file 첨부가 되어 있는지 확인
		if( files != null && files.size() != 0 && files.get(0).getSize() != 0 ) {
			log.info("files in value!");
									
			ResponseEntity<String> savedFiles = 
					boardService.uploadFiles(files,bp_category_no,bp_writer_no);
								
			if(savedFiles != null) {
				log.info("uploadFiles succuess!");					
				
				ObjectMapper objectMapper = new ObjectMapper();
				
				try {
					Map<String,Object> savedFileObj = objectMapper.readValue(savedFiles.getBody(), new TypeReference<Map<String,Object>>() {});
					log.info("savedFiles(string) to savedFileNames(object) success!");
					
					List<String> savedFileNames = (List<String>) savedFileObj.get("savedFileNames");
					
					log.info("dir_name : {}",savedFileObj.get("dir_name"));
					log.info("savedFileNames : {}",savedFileNames);
					
					result = boardService.createConfirm(savedFileNames,bp_category_no,bp_writer_no,bp_title,bp_body);
					
				} catch (JsonMappingException e) {
					log.info("savedFiles(string) to savedFileNames(array) fail!");
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					log.info("savedFiles(string) to savedFileNames(array) fail!");
					e.printStackTrace();
				}
								
				return result;
			}else {
				log.info("uploadFiles fail!");
				
				return false;
			}
			
		}else {
			log.info("files empty!");
			
			result = boardService.createConfirm(null, bp_category_no, bp_writer_no, bp_title, bp_body);
			
			return result;
			
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
