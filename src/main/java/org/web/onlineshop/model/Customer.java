package org.web.onlineshop.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Customer extends User implements Serializable 
{
	private static final long serialVersionUID = -797644671510990101L;

	@OneToOne(cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Cart> previousPurchases = new HashSet<>();
    
    @ManyToMany(mappedBy="customers", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    private Set<Article> favoriteArticles = new HashSet<>();
    
    @Column
    @Min(0)
    private Integer bonusPoints;
}