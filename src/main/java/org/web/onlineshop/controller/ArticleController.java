package org.web.onlineshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web.onlineshop.dto.ArticleDto;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.ArticleCategory;
import org.web.onlineshop.service.ArticleCategoryService;
import org.web.onlineshop.service.ArticleService;
import org.web.onlineshop.util.ArticleSortType;
import org.web.onlineshop.util.Constants;

@RestController
@RequestMapping(value = Constants.REST_API_PREFIX + "/articles")
public class ArticleController 
{
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private ArticleCategoryService articleCategoryService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> getArticles()
	{
		List<Article> articles = this.articleService.findAll();
		List<ArticleDto> articleDtos = new ArrayList<>();
		articles.stream().forEach(article ->
		{
			articleDtos.add(modelMapper.map(article, ArticleDto.class));
		});
		return new ResponseEntity<>(articleDtos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/discount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> getArticlesOnDiscount()
	{
		List<Article> articles = this.articleService.findAllOnDiscount();
		List<ArticleDto> articleDtos = new ArrayList<>();
		articles.stream().forEach(article ->
		{
			articleDtos.add(modelMapper.map(article, ArticleDto.class));
		});
		return new ResponseEntity<>(articleDtos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/category/{categoryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> getArticlesByCategory(@PathVariable Long categoryId)
	{
		ArticleCategory articleCategory = null;
		try
		{
			articleCategory = this.articleCategoryService.findById(categoryId);
		}
		catch(Exception exception) { }
		
		List<Article> articles = this.articleService.findByCategory(articleCategory);
		List<ArticleDto> articleDtos = new ArrayList<>();
		articles.stream().forEach(article ->
		{
			articleDtos.add(modelMapper.map(article, ArticleDto.class));
		});
		return new ResponseEntity<>(articleDtos, HttpStatus.OK);
	}
	
	@RequestMapping(value = "sort/{sortType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> getSortedArticles(@PathVariable String sortType) 
	{
		List<Article> articles = this.articleService.findAllSorted(ArticleSortType.get(sortType));
		List<ArticleDto> articleDtos = new ArrayList<>();
		articles.stream().forEach(article ->
		{
			articleDtos.add(modelMapper.map(article, ArticleDto.class));
		});
		return new ResponseEntity<>(articleDtos, HttpStatus.OK);
	}
	
	@RequestMapping(value ="/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ArticleDto>> searchArticles(@RequestParam(required = false, name = "name") String name,
														@RequestParam(required = false, name = "description") String description,
														@RequestParam(required = false, name = "priceFrom") Double priceFrom,
														@RequestParam(required = false, name = "priceTo") Double priceTo)
	{
		List<Article> articles = this.articleService.search(name, description, priceFrom, priceTo);
		List<ArticleDto> articleDtos = new ArrayList<>();
		articles.stream().forEach(article ->
		{
			articleDtos.add(modelMapper.map(article, ArticleDto.class));
		});
		return new ResponseEntity<>(articleDtos, HttpStatus.OK);
	}
}