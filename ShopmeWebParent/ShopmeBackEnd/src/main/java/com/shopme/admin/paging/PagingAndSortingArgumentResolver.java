package com.shopme.admin.paging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingArgumentResolver implements HandlerMethodArgumentResolver{

	private static final Logger LOGGER = LoggerFactory.getLogger(PagingAndSortingArgumentResolver.class);
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		// TODO Auto-generated method stub
		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
		// TODO Auto-generated method stub
		
		PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
		String sortDir = request.getParameter("sortDir");
		String sortField = request.getParameter("sortField");
		String keyword = request.getParameter("keyword");

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("moduleURL", annotation.moduleURL());	

		LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | sortField : " + sortField);
		LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | sortDir : " + sortDir);
		LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | reverseSortDir : " + reverseSortDir);
		LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | keyword : " + keyword);
		LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | moduleURL : " + annotation.moduleURL());
		
		return new PagingAndSortingHelper(model, annotation.listName(),
				sortField, sortDir, keyword);
	}

}
