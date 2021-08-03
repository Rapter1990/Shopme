package com.shopme.admin.paging;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface PagingAndSortingParam {
	
	public String moduleURL();

	public String listName();
}
