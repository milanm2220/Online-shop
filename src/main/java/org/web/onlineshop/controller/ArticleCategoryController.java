package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.ArticleCategoryDto;
import org.web.onlineshop.model.ArticleCategory;
import org.web.onlineshop.service.ArticleCategoryService;
import org.web.onlineshop.util.Constants;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/article_categories")
public class ArticleCategoryController 
{
	@Autowired
	private ArticleCategoryService articleCategoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleCategoryDto>> getArticleCategories()
	{
		List<ArticleCategory> articleCategories = this.articleCategoryService.findAll();
		List<ArticleCategoryDto> articleCategoryDtos = new ArrayList<>();
		articleCategories.stream().forEach(articleCategory ->
		{
			articleCategoryDtos.add(modelMapper.map(articleCategory, ArticleCategoryDto.class));
		});
		return new ResponseEntity<>(articleCategoryDtos, HttpStatus.OK);
	}
}