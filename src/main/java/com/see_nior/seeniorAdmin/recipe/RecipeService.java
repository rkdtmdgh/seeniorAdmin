package com.see_nior.seeniorAdmin.recipe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.see_nior.seeniorAdmin.api.ApiExplorer;
import com.see_nior.seeniorAdmin.dto.RecipeDto;
import com.see_nior.seeniorAdmin.recipe.mapper.RecipeMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class RecipeService {
	
	@Autowired
	private ApiExplorer apiExplorer;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	final private RecipeMapper recipeMapper;
	
	public RecipeService(RecipeMapper recipeMapper) {
		this.recipeMapper = recipeMapper;
		
	}
	
	// 페이지네이션 관련
	private int pageLimit = 10;	// 한 페이지당 보여줄 항목의 개수
	private int blockLimit = 5;	// 하단에 보여질 페이지 번호의 수
	
	// 기존 레시피 테이블 삭제 후 테이블 생성 후 API 데이터 DB에 저장하기
	@Transactional
	public void refreshApiRecipeData() {
		log.info("refreshApiRecipeData()");
		
		// 기존 레시피 테이블 drop (mybatis 매퍼에서 테이블이 있을 시만 삭제하게 구현하였음)
		recipeMapper.dropRecipeTable();
		
		// 새로운 레시피 테이블 create
		recipeMapper.createRecipeTable();
		
		try {
			
			// total_count(API 레시피 데이터의 총 개수) 뽑기
			String dummyRecipe = apiExplorer.getRecipe();
			
			JsonNode dummyRootNode = objectMapper.readTree(dummyRecipe);
			
			int total_count = dummyRootNode.path("COOKRCP01").path("total_count").asInt();
			
			// 병합된 데이터를 담을 ObjectNode 생성
			ObjectNode jsonRecipeData = objectMapper.createObjectNode();
			
			// total count의 개수가 1000개 초과라면 (API 호출 갯수 1000개 제한있음)
			if (total_count > 1000) {
				
				// API 호출을 몇번 할건지 구하기
				int roof = (int) Math.ceil(total_count / 1000.0);
				
				// roof의 횟수만큼 1000개씩 데이터 호출 하여 DB에 저장
				for (int i = 1; i <= roof; i++) {
					
					// startIdx와 endIdx 지정
					int startIdx = (i - 1) * 1000 + 1;
					int endIdx = i * 1000;

					// endIdx가 total_count보다 크다면 endIdx를 total_count로 할당
					if (endIdx > total_count) {
						endIdx = total_count;
						
					} 
					
					// API에서 JSON 데이터 가져오기
					String recipePart = apiExplorer.getRecipe(startIdx, endIdx);
					JsonNode recipePartNode = objectMapper.readTree(recipePart);
					
					// 불러온 JSON 데이터들 병합
					mergeJsonNodes(jsonRecipeData, recipePartNode);
					
				}
			
			// total_count의 개수가 1000개 이하라면
			} else {
			
				// API에서 JSON 데이터 가져오기
				String recipePart = apiExplorer.getRecipe(1, total_count);
				JsonNode recipePartNode = objectMapper.readTree(recipePart);
				
				// 불러온 JSON 데이터들 병합
				mergeJsonNodes(jsonRecipeData, recipePartNode);
				
			}	
			
			// JSON 데이터를 JsonNode 형으로 파싱
			JsonNode rowNode = jsonRecipeData.path("COOKRCP01").path("row");
			
			// rowNode를 배열 형식으로 잘 가지고 왔다면
			if (rowNode.isArray()) {
				
				// RecipeDto들을 담을 리스트 선언
				List<RecipeDto> recipes = new ArrayList<>();
				
				// rowNode 배열의 항목들을 JsonNode형의 row로 할당
				for (JsonNode row : rowNode) {
					
					// JsonNode는 수정할 수 없으므로 ObjectNode형으로 형변환
					ObjectNode recipesNode = (ObjectNode) row;
					
					// recipesNode의 키와 밸류값을 반복하여 뽑기
					Iterator<Entry<String, JsonNode>> recipeFields = recipesNode.fields();
					
					// recipesNode의 키값을 소문자로 변환해서 담을 modifiedNode 생성
					ObjectNode modifiedNode = objectMapper.createObjectNode();
					
					// recipeFields의 길이만큼 recipesNode의 키값 소문자로 변경하는 로직
					while(recipeFields.hasNext()) {
						
						// recipeFields에서 한개의 데이터를 뽑아 field로 지정
						Entry<String, JsonNode> field = recipeFields.next();
						
						// field 에서 키값을 뽑아 소문자로 변경
						String lowerCaseFieldName = field.getKey().toLowerCase();
						
						// field에서 밸류값 기존 그대로 가져오기
						JsonNode fieldValue = field.getValue();
						
						// 소문자로 변경된 키값과 기존의 밸류값을 modifiedNode에 할당
						modifiedNode.set(lowerCaseFieldName, fieldValue);
						
					}
					
					// modifiedNode의 데이터들을 RecipeDto형으로 변환
					RecipeDto recipeDto = objectMapper.treeToValue(modifiedNode, RecipeDto.class);
					
					// recipes 리스트에 recipeDto 추가하기
					recipes.add(recipeDto);
						
				}
				
				// DB에 recipes의 레시피들 insert
				for (RecipeDto recipeDto : recipes) {
					
					recipeMapper.insertApiRecipeData(recipeDto);
					
				}
			
			// rowNode를 가지고 오지 못 했다면
			} else {
				log.info("데이터의 row값을 가져오는데 실패하였습니다.");
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		}
		
	}

	// API 호출 항목 갯수 제한 때문에 전체 API 데이터를 모두 호출해 합치는 메서드
    private void mergeJsonNodes(ObjectNode jsonRecipeData, JsonNode recipePartNode) {
        // recipePartNode에서 'COOKRCP01'의 'row' 배열을 추출
        JsonNode rows = recipePartNode.path("COOKRCP01").path("row");
        
        // jsonRecipeData에 'COOKRCP01'이 있는지 확인하고, 'row' 배열을 가져옴
        JsonNode targetRows = jsonRecipeData.path("COOKRCP01").path("row");
        
        if (targetRows.isMissingNode()) {
            // jsonRecipeData에 'COOKRCP01'이 없거나 'row' 배열이 없는 경우, recipePartNode의 'COOKRCP01'을 jsonRecipeData에 추가
            ((ObjectNode) jsonRecipeData).set("COOKRCP01", recipePartNode.path("COOKRCP01"));
            
        } else {
            // jsonRecipeData에 'COOKRCP01'과 'row' 배열이 모두 있는 경우, recipePartNode의 'row' 배열을 jsonRecipeData에 추가
            ((ArrayNode) targetRows).addAll((ArrayNode) rows);
        }
    }

	// 페이지에 따른 식단 가져오기 (모든 식단)
	public Map<String, Object> getRecipeListWithPage(int page, String sort) {
		log.info("getRecipeListWithPage()");
		
		int pagingStart = (page - 1) * pageLimit;
		
		Map<String, Object> pagingList = new HashMap<>();
		
		Map<String, Object> pagingParams = new HashMap<>();
		pagingParams.put("start", pagingStart);
		pagingParams.put("limit", pageLimit);
		pagingParams.put("sort", sort);
		
		List<RecipeDto> recipeDtos = recipeMapper.getRecipeListWithPage(pagingParams);
		pagingList.put("recipeDtos", recipeDtos);
		
		return pagingList;
		
	}

	// 식단의 총 페이지 개수 구하기 (모든 식단)
	public Map<String, Object> getRecipeListPageNum(int page) {
		log.info("getRecipeListPageNum()");
		
		Map<String, Object> recipeListPageNum = new HashMap<>();
		
		// 전체 리스트 개수 조회
		int recipeListCnt = recipeMapper.getAllRecipeCnt();
		
		// 전체 페이지 개수 계산
		int maxPage = (int) (Math.ceil((double) recipeListCnt / pageLimit));
		
		// 시작 페이지 값 계산
		int startPage = ((int) (Math.ceil((double) page / blockLimit)) - 1) * blockLimit + 1;
		
		// 마지막 페이지 값 계산
		int endPage = startPage+ + blockLimit - 1;
		if (endPage > maxPage) endPage = maxPage;
		
		recipeListPageNum.put("recipeListCnt", recipeListCnt);
		recipeListPageNum.put("page", page);
		recipeListPageNum.put("maxPage", maxPage);
		recipeListPageNum.put("startPage", startPage);
		recipeListPageNum.put("endPage", endPage);
		recipeListPageNum.put("blockLimit", blockLimit);
		recipeListPageNum.put("pageLimit", pageLimit);
		
		return recipeListPageNum;
		
	}
	
		
} 
