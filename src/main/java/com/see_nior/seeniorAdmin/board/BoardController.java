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
import com.see_nior.seeniorAdmin.dto.BoardPostsDto;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RequestBody;





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
	
	//페이징 처리된 게시판 항목 가져오기
	@GetMapping("/cate_info/get_category_list")
	@ResponseBody
	public Object getCategoryList(
			@RequestParam(value = "page", required = false, defaultValue = "1") int page, 
			@RequestParam(value = "sortValue", required = false, defaultValue = "bc_idx") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "asc") String order) {		
		log.info("getCategoryList()");
					
		// 페이지 번호에 따른 게시판 카테고리 리스트들 가져오기
		Map<String, Object> boardCategoryListWithPage = boardService.getBoardCategoryListWithPage(page, sortValue, order);
					
		// 게시판 카테고리 총 페이지 개수 가져오기
		Map<String, Object> boardCategoryListPageNum = boardService.getBoardCategoryListPageNum(page);
			
		boardCategoryListWithPage.put("boardCategoryListPageNum", boardCategoryListPageNum);
		boardCategoryListWithPage.put("sortValue", sortValue);
		boardCategoryListWithPage.put("order", order);
			
		return boardCategoryListWithPage;
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
					String bp_dir_name = (String) savedFileObj.get("dir_name");
					log.info("dir_name : {}",savedFileObj.get("dir_name"));
					log.info("savedFileNames : {}",savedFileNames);
					
					result = boardService.createConfirm(savedFileNames,bp_category_no,bp_writer_no,bp_title,bp_body,bp_dir_name);
					
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
			
			result = boardService.createConfirm(null, bp_category_no, bp_writer_no, bp_title, bp_body, null);
			
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
	
	//특정 게시판 게시물 리스트 가져오기
	@GetMapping("/info/get_posts_list")
	@ResponseBody
	public Object getPostsList(@RequestParam("infoNo") int bp_category_no, 
			@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "sortValue", required = false, defaultValue = "bp_no") String sortValue,
			@RequestParam(value = "order", required = false, defaultValue = "desc") String order) {
		log.info("getPostsList()");
		
		// 특정 게시판 페이지 번호에 따른 게시물 리스트들 가져오기
		Map<String, Object> boardPostsListWithPage = boardService.getBoardPostsListWithPage(bp_category_no, page, sortValue, order);
				
		// 특정 게시판 게시물 총 페이지 개수 가져오기
		Map<String, Object> boardPostsListPageNum = boardService.getBoardPostsListPageNum(bp_category_no, page);
				
		boardPostsListWithPage.put("boardPostsListPageNum", boardPostsListPageNum);
		boardPostsListWithPage.put("sortValue", sortValue);
		boardPostsListWithPage.put("order", order);
				
		return boardPostsListWithPage;
		
	}
	
	//게시판 순서 변경
	@PostMapping("/cate_info/modify_category_idx")
	@ResponseBody
	public boolean modifyCategoryIdx(@RequestParam("bc_no") int bc_no,
			@RequestParam("current_bc_idx") int current_bc_idx, 
			@RequestParam("bc_idx") int bc_idx) {
		log.info("get_posts_list()");
		
		int result = boardService.modifyCategoryIdx(bc_no,current_bc_idx,bc_idx);
		
		if(result <= 0) {
			return false;
		}else {
			return true;
		}
				
	}
	
	//게시판 카테고리 검색
	@GetMapping("/cate_info/search_board_category_list")
	@ResponseBody
	public Object searchBoardCategoryList(
			@RequestParam(value = "searchPart") String searchPart,
			@RequestParam(value = "searchString") String searchString,
			@RequestParam(value = "page", required = false, defaultValue = "1") int page) {
		log.info("searchBoardCategoryList()");
		
		// 페이지 번호에 따른 검색 게시판 카테고리 리스트들 가져오기
		Map<String, Object> searchBoardCategoryListWithPage = boardService.getSearchBoardCategoryListWithPage(searchPart, searchString, page);
				
		// 검색 게시판 카테고리 총 페이지 개수 가져오기
		Map<String, Object> searchBoardCategoryListPageNum = boardService.getSearchBoardCategoryListPageNum(searchPart, searchString, page);
				
		searchBoardCategoryListWithPage.put("searchBoardCategoryListPageNum", searchBoardCategoryListPageNum);
		searchBoardCategoryListWithPage.put("searchPart", searchPart);
		searchBoardCategoryListWithPage.put("searchString", searchString);
		
		return searchBoardCategoryListWithPage;
	}
	
	//게시판 카테고리 정보 수정
	@PostMapping("/cate_info/modify_category_confirm")
	@ResponseBody
	public boolean modifyCategoryConfirm(BoardCategoryDto boardCategoryDto, @RequestParam("current_bc_idx") int current_bc_idx) {
		log.info("modifyCategoryConfirm()");
		
		log.info("modifyDto: {}",boardCategoryDto);
		log.info("current_bc_idx: {}",current_bc_idx);
		
		boolean result = boardService.modifyCategoryConfirm(boardCategoryDto,current_bc_idx);
		
		return result;
	}
	
	//게시판 카테고리 삭제 요청
	@PostMapping("/cate_info/delete_category_confirm")
	@ResponseBody
	public boolean deleteCategoryConfirm(BoardCategoryDto boardCategoryDto) {
		log.info("deleteCategoryConfirm()");
		log.info("deleteDto: {}",boardCategoryDto);	
		boolean result = boardService.deleteCategoryConfirm(boardCategoryDto);
		
		return result;
	}
			
	//특정 게시물 정보 가져오기
	@GetMapping("/info/modify_form")
	public String modifyForm(@RequestParam("bp_no") int bp_no, Model model) {
		log.info("modifyForm()");
		
		BoardPostsDto boardPostsDto = boardService.modifyForm(bp_no);
		
		log.info("boardPostsDto: {}",boardPostsDto);
		
		model.addAttribute("boardPostsDto", boardPostsDto);
		
		String nextPage = "board/modify_form";
		
		return nextPage;
	}
	
}
