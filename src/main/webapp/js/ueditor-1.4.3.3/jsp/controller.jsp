<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.sxkl.cloudnote.editor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%
    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	String rootPath = application.getRealPath( "/" );
	String result = new ActionEnter( request, rootPath ).exec();
	out.write(result);
%>