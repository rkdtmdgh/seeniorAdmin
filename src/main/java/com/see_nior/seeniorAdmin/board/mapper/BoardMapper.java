package com.see_nior.seeniorAdmin.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.see_nior.seeniorAdmin.dto.BoardCategoryDto;

@Mapper
public interface BoardMapper {

	List<BoardCategoryDto> getList();

	boolean isBoardCategory(String bc_name);

	List<BoardCategoryDto> getBoardCategoryIdxMaxNum();

	int updateBoardCategoryIdx(int bc_idx);

	int createBoardCategory(BoardCategoryDto boardCategoryDto);

	List<BoardCategoryDto> getBoardCategoryForModify(int bc_no);


}
