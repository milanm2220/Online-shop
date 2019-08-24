package org.web.onlineshop.model;

import java.io.Serializable;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Entity
public class Administrator extends User implements Serializable 
{
	private static final long serialVersionUID = 213379985633075333L;
}