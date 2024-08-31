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
	private ApiExplorer apiExplorer;
	
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
			JsonNode cookRcp01Node = jsonRecipeData.path("COOKRCP01");
			JsonNode rowNode = cookRcp01Node.path("row");
			
			if (rowNode.isArray()) {
				List<RecipeDto> recipes = new ArrayList<>();
				
				for (JsonNode row : rowNode) {
					
					if (row.isObject()) {
						ObjectNode objectNode = (ObjectNode) row;
						Iterator<Entry<String, JsonNode>> fields = objectNode.fields();
						
						ObjectNode modifiedNode = objectMapper.createObjectNode();
						while(fields.hasNext()) {
							Entry<String, JsonNode> field = fields.next();
							String lowerCaseFieldName = field.getKey().toLowerCase();
							JsonNode fieldValue = field.getValue();
							modifiedNode.set(lowerCaseFieldName, fieldValue);
							
						}
						
						RecipeDto recipeDto = objectMapper.treeToValue(modifiedNode, RecipeDto.class);
						recipes.add(recipeDto);
						
					} 
				}
				
				for (RecipeDto recipeDto : recipes) {
					
					recipeMapper.insertApiRecipeData(recipeDto);
					
				}
				
				
			} else {
				log.info("Expected 'row' to be an array but found something else.");
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
			
	}

	private void mergeJsonNodes(ObjectNode target, JsonNode source) {
		log.info("mergeJsonNodes()");
		
		if (source.isObject()) {
			
			Iterator<Entry<String, JsonNode>> fields = source.fields();
			
			while (fields.hasNext()) {
				Entry<String, JsonNode> field = fields.next();
				String fieldName = field.getKey();
				JsonNode fieldValue = field.getValue();
				
				if (fieldValue.isObject()) {
					
					ObjectNode subNode = (ObjectNode) target.get(fieldName);
					
					if (subNode == null) {
						subNode = objectMapper.createObjectNode();
						target.set(fieldName, subNode);
						
					}
					mergeJsonNodes(subNode, fieldValue);
					
				} else if (fieldValue.isArray()) {
					
					ArrayNode arrayNode = (ArrayNode) target.withArray(fieldName);
					
					for (JsonNode element : fieldValue) {
						arrayNode.add(element);
						
					}
					
				} else {
					target.set(fieldName, fieldValue);
					
				}
				
				
			}
			
		} 
//		else if (source.isArray()) {
//			
//			ArrayNode arrayNode = objectMapper.createArrayNode();
//			if (target.has("row")) {
//				arrayNode = (ArrayNode) target.get("row");
//				
//			}
//			for (JsonNode element : source) {
//				arrayNode.add(element);
//				
//			}
//			target.set("row", arrayNode);
//			
//			}
			
		}
		
	} 
