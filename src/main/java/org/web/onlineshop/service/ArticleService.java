package org.web.onlineshop.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.ArticleAlreadyExistsException;
import org.web.onlineshop.exceptions.ArticleNotExistsException;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.ArticleCategory;
import org.web.onlineshop.repository.ArticleRepository;
import org.web.onlineshop.util.ArticleSortType;

@Service
public class ArticleService 
{
	@Autowired
	private ArticleRepository articleRepository;
	
	public List<Article> findAll()
	{
		return this.articleRepository.findAll();
	}
	
	public List<Article> findAllOnDiscount()
	{
		return this.articleRepository.findByDiscountNotNullAndDiscountGreaterThan(0);
	}
	
	public List<Article> findByCategory(ArticleCategory category)
	{
		return this.articleRepository.findByCategory(category);
	}
	
	public List<Article> findAllSorted(ArticleSortType sortType)
	{
		if (sortType == ArticleSortType.NAME_ASC)
		{
			return this.articleRepository.findAllByOrderByNameAsc();
		}
		else if (sortType == ArticleSortType.NAME_DESC)
		{
			return this.articleRepository.findAllByOrderByNameDesc();
		}
		else if (sortType == ArticleSortType.PRICE_ASC)
		{
			return this.articleRepository.findAllByOrderByPriceAsc();
		}
		else if (sortType == ArticleSortType.PRICE_DESC)
		{
			return this.articleRepository.findAllByOrderByPriceDesc();
		}
		else
		{
			return new ArrayList<>();
		}
	}
	
	public List<Article> search(String name, String description, Double priceFrom, Double priceTo)
	{
		List<Article> articles = (name != null) ? this.articleRepository.findByNameIgnoreCaseContaining(name) : this.articleRepository.findAll();
	
		if (description != null)
		{
			articles.retainAll(this.articleRepository.findByDescriptionIgnoreCaseContaining(description));
		}
		
		if (priceFrom != null)
		{
			articles.retainAll(this.articleRepository.findByPriceGreaterThanEqual(priceFrom));
		}
		
		if (priceTo != null)
		{
			articles.retainAll(this.articleRepository.findByPriceLessThanEqual(priceTo));
		}
		
		return articles;
	}
	
	public Article save(Article article)
	{
		if (!this.articleRepository.findByName(article.getName()).isEmpty())
		{
			throw new ArticleAlreadyExistsException(article.getName());
		}
		return this.articleRepository.save(article);
	}
	
	public Article update(Article article)
	{
		if (this.articleRepository.existsById(article.getId()))
		{
			return this.articleRepository.save(article);			
		}
		else
		{
			throw new ArticleNotExistsException(article.getId());
		}
	}
	
	public void delete(Long id)
	{
		this.articleRepository.deleteById(id);
	}
}