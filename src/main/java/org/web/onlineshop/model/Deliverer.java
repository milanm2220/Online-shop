package org.web.onlineshop.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
public class Deliverer extends User implements Serializable 
{
	private static final long serialVersionUID = 8682722971999008851L;
	
	@OneToMany(mappedBy = "deliverer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Cart> deliveries = new HashSet<>();
}