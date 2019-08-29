package org.web.onlineshop.dto;

import lombok.Data;

@Data
public class ItemDto 
{
	private Long id;
	private ArticleDto article;
	private Integer amount;
}