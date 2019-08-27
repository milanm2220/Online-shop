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
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode
public class Article 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@EqualsAndHashCode.Exclude
	@Column(nullable = false, unique = true)
	private String name;
	
	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	private String description;
	
	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	@Min(0)
	private Double price;
	
	@EqualsAndHashCode.Exclude
	@Column(nullable = false)
	@Min(0)
	private Integer quantity;
	
	@EqualsAndHashCode.Exclude
	@ManyToOne
	private ArticleCategory category;
	
	@EqualsAndHashCode.Exclude
	@Column
	@Min(0)
    @Max(99)
	private Integer discount;
}