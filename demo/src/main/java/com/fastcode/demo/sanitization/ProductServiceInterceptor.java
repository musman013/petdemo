package com.fastcode.demo.sanitization;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerInterceptor;

/*
 * 
 * this is the request interceptor class which uses the once per request filter 
 * first any post request will cone here and then we filter it out with the json sanitizer
 * and then forward it to the controller
 */

@Component
public class ProductServiceInterceptor extends OncePerRequestFilter implements HandlerInterceptor  {
	@Autowired
	JsonSanitizition sanitizer;

	@Override
	public boolean preHandle
	(HttpServletRequest request, HttpServletResponse response, Object handler) 
			throws Exception {

		String requestStr = IOUtils.toString(request.getInputStream());

		CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
				new CachedBodyHttpServletRequest(request);


		//	   if ("POST".equalsIgnoreCase(request.getMethod())) 
		//	   {
		//	      String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		//	   }

		String jsonSanitize = sanitizer.jsonSanitize(requestStr);
		System.err.println("Pre Handle method is Calling>>>"+request);

		/*
		 * request.set
		 * 
		 * 
		 * public ResettableStreamHttpServletRequest(HttpServletRequest request) {
		 * super(request); this.request = request; this.servletStream = new
		 * ResettableServletInputStream(); }
		 */
		return true;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {


		CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
				new CachedBodyHttpServletRequest(request);




		filterChain.doFilter(cachedBodyHttpServletRequest, response);	
	}




}