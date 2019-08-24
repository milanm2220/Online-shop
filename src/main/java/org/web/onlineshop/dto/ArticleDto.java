package org.web.onlineshop.dto;

import lombok.Data;

@Data
public class ArticleDto 
{
	private Long id;
	private String name;
	private String description;
	private Double price;
	private Integer quantity;
	private ArticleCategoryDto category;
	private Integer discount;
}