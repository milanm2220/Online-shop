package org.web.onlineshop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CustomerDto extends UserDto
{	    
    private Integer bonusPoints;
}