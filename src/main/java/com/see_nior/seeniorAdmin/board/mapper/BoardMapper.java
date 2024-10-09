package com.see_nior.seeniorAdmin.board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;
import com.see_nior.seeniorAdmin.dto.BoardPostsDto;

@Mapper
public interface BoardMapper {

	List<BoardCategoryDto> getList();
	
	List<BoardCategoryDto> getBoardCategoryListWithPage(Map<String, Object> pagingParams);

	int getAllBoardCategoryCnt();

	boolean isBoardCategory(String bc_name);

	List<BoardCategoryDto> getBoardCategoryIdxMaxNum();

	int updateBoardCategoryIdx(int bc_idx);

	int createBoardCategory(BoardCategoryDto boardCategoryDto);

	List<BoardCategoryDto> getBoardCategoryForModify(int bc_no);

	int selectCountBoardPostsByBcNo(int bc_no);

	int updateBoardCategoryForBcItemCntByBcNo(Map<String, Object> updateParm);

	int createConfirm(BoardPostsDto boardPostsDto);

	List<BoardPostsDto> getBoardPostsListWithPage(Map<String, Object> pagingParams);

	int getBoardPostsByCategoryCnt(int bp_category_no);

	

}
