package com.sxkl.cloudnote.common.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainService {
	
	@Autowired
	private HttpServletRequest request;
	
	public String getDomain(){
		StringBuilder domain = new StringBuilder();
		domain.append(request.getScheme())
			  .append("://")
			  .append(request.getServerName())
			  .append(":")
			  .append(request.getServerPort())
			  .append(request.getContextPath());
		return domain.toString();
	}

}
