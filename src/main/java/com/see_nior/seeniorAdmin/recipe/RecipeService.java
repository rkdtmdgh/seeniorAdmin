package com.see_nior.seeniorAdmin.recipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	ApiExplorer apiExplorer;
	
	@Autowired
	RecipeMapper recipeMapper;
	
	@Autowired
	ObjectMapper objectMapper;

	// 레시피 API 불러온 후 json 으로 넘어온 API Data DB에 저장하기
	public void saveApiRecipeData() {
		log.info("saveApiRecipeData()");
		
		try {
			// API에서 JSON 데이터 가져오기
			String json1 = apiExplorer.getRecipe(1, 1000);
			String json2 = apiExplorer.getRecipe(1001, 1124);
			
			// JSON 데이터를 JsonNode로 파싱
			JsonNode json1Node = objectMapper.readTree(json1);
			JsonNode json2Node = objectMapper.readTree(json2);
			
			// JSON 객체를 병합
			ObjectNode jsonRecipeData = objectMapper.createObjectNode();
			mergeJsonNodes(jsonRecipeData, json1Node);
			mergeJsonNodes(jsonRecipeData, json2Node);
			
			// JSON 데이터를 JsonNode로 파싱
			JsonNode rowNode = jsonRecipeData.path("COOKRCP01").path("row");
			
			// rowNode를 배열 형식으로 잘 가지고 왔다면
			if (rowNode.isArray()) {
				
				// RecipeDto들을 담을 리스트 선언
				List<RecipeDto> recipes = new ArrayList<>();
				
				// rowNode 배열의 항목들을 JsonNode형의 row로 할당
				for (JsonNode row : rowNode) {
					
					// JsonNode는 수정할 수 없으므로 ObjectNode형으로 형변환
					ObjectNode objectNode = (ObjectNode) row;
					
					// objectNode의 키와 밸류값을 반복하여 뽑기
					Iterator<Entry<String, JsonNode>> fields = objectNode.fields();
					
					// objectNode의 키값을 소문자로 변환한 modifiedNode 생성
					ObjectNode modifiedNode = objectMapper.createObjectNode();
					
					// fields의 길이만큼 objectNode의 키값 소문자로 변경하는 로직
					while(fields.hasNext()) {
						// fields에서 한개의 데이터를 키, 밸류로 지정
						Entry<String, JsonNode> field = fields.next();
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
				log.info("Expected 'row' to be an array but found something else.");
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
			
	}

	// API 호출 항목 갯수 제한 때문에 전체 API 데이터를 모두 호출해 합치는 메서드
	private void mergeJsonNodes(ObjectNode target, JsonNode jsonData) {
		log.info("mergeJsonNodes()");
		
			// jsonData의 키와 밸류값을 반복하여 뽑기
			Iterator<Entry<String, JsonNode>> fields = jsonData.fields();
			
			while (fields.hasNext()) {
				// fields에서 한개의 데이터를 키, 밸류로 지정
				Entry<String, JsonNode> field = fields.next();
				// field에서 키값 뽑기
				String fieldKey = field.getKey();
				// field에서 밸류값 뽑기
				JsonNode fieldValue = field.getValue();
				
				// fieldValue가 JSON객체인지 확인
				if (fieldValue.isObject()) {
					
					// target객체에서 field fieldKey에 해당하는 값 가져와 subNode로 할당
					ObjectNode subNode = (ObjectNode) target.get(fieldKey);
					
					// subNode가 null일 경우 새로운 ObjectNode 생성하여 fieldKey할당.
					// 첫번째 객체일때 빈 fieldKey 초기화하기 위함.
					if (subNode == null) {
						subNode = objectMapper.createObjectNode();
						target.set(fieldKey, subNode);
						
					}
					
					// subNode와 현재의 fieldValue 병합. 재귀적으로 호출하므로 중첩된 객체도 병합 가능
					mergeJsonNodes(subNode, fieldValue);
					
				// fieldValue가 배열일 경우
				} else if (fieldValue.isArray()) {
					
					// target에서 fieldKey에 대응하는 배열을 가져오거나, 없으면 새로 생성하여 arrayNode에 할당.
					ArrayNode arrayNode = (ArrayNode) target.withArray(fieldKey);
					
					// fieldValue를 순회하며 fieldValue의 모든 요소가 arrayNode에 할당
					for (JsonNode fieldElement : fieldValue) {
						
						// fieldElement의 각 요소들을 target의 배열 필드에 결합하여, 기존의 배열의 내용이 유지되며 새로운 요소들이 병합됨.
						arrayNode.add(fieldElement);
						
					}
					
				// 배열이나 객체가 아닌 단순 데이터 형식일 시, target에 그대로 할당
				} else {
					target.set(fieldKey, fieldValue);
					
				}
				
			}
			
	}
		
} 
