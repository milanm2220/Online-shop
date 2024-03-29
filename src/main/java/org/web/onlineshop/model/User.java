package org.web.onlineshop.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.validator.constraints.Length;
import org.web.onlineshop.util.UserRole;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@MappedSuperclass
public class User implements Serializable 
{
	private static final long serialVersionUID = -557431733986407255L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    @Length(min = 6)
    @Length(max = 20)
    private String username;

    @Column(nullable=false)
    @Length(min = 6)
    @Length(max = 32)
    private String password;

    @Column(nullable=false)
    private String firstname;

    @Column(nullable=false)
    private String lastname;

    @Enumerated(EnumType.STRING)
    @Column
    private UserRole role;

    @Column
    private String phoneNumber;

    @Column(nullable=false)
    private String email;

    @Column
    private String address;
}