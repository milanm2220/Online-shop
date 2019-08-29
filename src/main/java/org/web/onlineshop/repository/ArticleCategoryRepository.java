package org.web.onlineshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.web.onlineshop.model.ArticleCategory;

@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> 
{
	List<ArticleCategory> findByName(String name);
}