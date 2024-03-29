package org.web.onlineshop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.web.onlineshop.util.OrderStatus;

import lombok.Data;


@Data
@Entity
public class Cart implements Serializable 
{
	private static final long serialVersionUID = -7350419981047041799L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();

	@Column
    private LocalDateTime timestamp;
    
    @ManyToOne
    private Customer customer;

    @ManyToOne 
    private Deliverer deliverer;

    @Enumerated(EnumType.STRING)
    @Column
    private OrderStatus state;
    
    @Column
    private Double totalPrice;
}