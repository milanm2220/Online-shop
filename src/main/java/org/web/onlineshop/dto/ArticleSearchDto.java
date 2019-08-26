package org.web.onlineshop.dto;

import lombok.Data;

@Data
public class ArticleSearchDto
{
	private String name;
	private String description;
	private Double priceFrom;
	private Double priceTo;
}