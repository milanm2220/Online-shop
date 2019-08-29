package org.web.onlineshop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web.onlineshop.exceptions.ArticleCategoryAlreadyExistsException;
import org.web.onlineshop.exceptions.ArticleCategoryNotExistsException;
import org.web.onlineshop.model.ArticleCategory;
import org.web.onlineshop.repository.ArticleCategoryRepository;

@Service
public class ArticleCategoryService 
{
	@Autowired
	private ArticleCategoryRepository articleCategoryRepository;
	
	public List<ArticleCategory> findAll()
	{
		return this.articleCategoryRepository.findAll();
	}
	
	public ArticleCategory findById(Long id)
	{
		return this.articleCategoryRepository.findById(id).get();
	}
	
	public ArticleCategory save(ArticleCategory articleCategory)
	{
		if (!this.articleCategoryRepository.findByName(articleCategory.getName()).isEmpty())
		{
			throw new ArticleCategoryAlreadyExistsException(articleCategory.getName());
		}
		return this.articleCategoryRepository.save(articleCategory);
	}
	
	public ArticleCategory update(ArticleCategory articleCategory)
	{
		if (this.articleCategoryRepository.existsById(articleCategory.getId()))
		{
			return this.articleCategoryRepository.save(articleCategory);			
		}
		else
		{
			throw new ArticleCategoryNotExistsException(articleCategory.getId());
		}
	}
	
	public void delete(Long id)
	{
		this.articleCategoryRepository.deleteById(id);
	}
}