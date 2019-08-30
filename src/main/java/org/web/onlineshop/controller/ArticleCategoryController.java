package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.ArticleCategoryDto;
import org.web.onlineshop.exceptions.UnauthorizedAccessException;
import org.web.onlineshop.model.ArticleCategory;
import org.web.onlineshop.service.ArticleCategoryService;
import org.web.onlineshop.util.Constants;
import org.web.onlineshop.util.UserRole;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/article_categories")
public class ArticleCategoryController 
{
	@Autowired
	private ArticleCategoryService articleCategoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private HttpServletRequest request;
	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleCategoryDto> add(@RequestBody ArticleCategoryDto articleCategoryDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			ArticleCategory articleCategory = this.modelMapper.map(articleCategoryDto, ArticleCategory.class);
			articleCategory = this.articleCategoryService.save(articleCategory);
			articleCategoryDto = this.modelMapper.map(articleCategory, ArticleCategoryDto.class);
			return new ResponseEntity<>(articleCategoryDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleCategoryDto> update(@RequestBody ArticleCategoryDto articleCategoryDto)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			ArticleCategory articleCategory = this.modelMapper.map(articleCategoryDto, ArticleCategory.class);
			articleCategory = this.articleCategoryService.update(articleCategory);
			articleCategoryDto = this.modelMapper.map(articleCategory, ArticleCategoryDto.class);
			return new ResponseEntity<>(articleCategoryDto, HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> delete(@PathVariable Long id)
	{
		if (request.getSession().getAttribute("role") != UserRole.ADMINISTRATOR)
		{
			throw new UnauthorizedAccessException();
		}
		
		try
		{
			this.articleCategoryService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch(Exception exception) { throw exception; }
	}
	
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