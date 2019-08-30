package org.web.onlineshop.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@Column
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
	
	@ManyToMany
    @JoinTable(name = "favorite_articles",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"))
	@EqualsAndHashCode.Exclude
	private Set<Customer> customers = new HashSet<>();
	
	@OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
	@EqualsAndHashCode.Exclude
    private Set<Item> items = new HashSet<>();
}