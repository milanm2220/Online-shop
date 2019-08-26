package org.web.onlineshop.util;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum ArticleSortType 
{
	NAME_ASC("name_asc"),
	NAME_DESC("name_desc"),
	PRICE_ASC("price_asc"),
	PRICE_DESC("price_desc");
	
	@Getter
	private String type;
	
	private ArticleSortType(String type) { this.type = type; }
	
	private static final Map<String, ArticleSortType> lookup = new HashMap<>();

    static
    {
        for (ArticleSortType sortType : ArticleSortType.values())
        {
            lookup.put(sortType.getType(), sortType);
        }
    }
    
    public static ArticleSortType get(String type)
    {
    	return lookup.get(type);
    }
}