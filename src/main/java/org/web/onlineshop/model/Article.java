package org.web.onlineshop.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Entity
public class Article 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	@Min(0)
	private Double price;
	
	@Column(nullable = false)
	@Min(0)
	private Integer quantity;
	
	@ManyToOne
	private ArticleCategory category;
	
	@Column
	@Min(0)
    @Max(99)
	private Integer discount;
}