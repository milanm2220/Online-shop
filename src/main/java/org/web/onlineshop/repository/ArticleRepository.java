package org.web.onlineshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.Article;
import org.web.onlineshop.model.ArticleCategory;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> 
{
	List<Article> findByDiscountNotNullAndDiscountGreaterThan(Integer minimumDiscount);
	
	List<Article> findByCategory(ArticleCategory category);
	
	List<Article> findAllByOrderByNameAsc();
	
	List<Article> findAllByOrderByNameDesc();
	
	List<Article> findAllByOrderByPriceAsc();
	
	List<Article> findAllByOrderByPriceDesc();
	
	List<Article> findByNameIgnoreCaseContaining(String name);
	
	List<Article> findByDescriptionIgnoreCaseContaining(String description);
	
	List<Article> findByPriceGreaterThanEqual(Double price);

	List<Article> findByPriceLessThanEqual(Double price);
}