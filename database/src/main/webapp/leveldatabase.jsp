<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sawyerharris.gravitygame.database.LevelWriter" %>
<%@ page import="com.sawyerharris.gravitygame.database.LevelGroup" %>
<%@ page import="com.sawyerharris.gravitygame.database.Level" %>
<%@ page import="com.googlecode.objectify.Key" %>
<%@ page import="com.googlecode.objectify.ObjectifyService" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%	try {
		if (request.getParameter("op").equals("write")) {
				String group = request.getParameter("group");
				String level = request.getParameter("level");
				int hashCode = Integer.parseInt(request.getParameter("hash"));
				String password = request.getParameter("pass");
				String author = request.getParameter("author");
				
				LevelWriter writer = new LevelWriter();
				writer.write(group, level, hashCode, password, author);
		} else if (request.getParameter("op").equals("read")) {		
			String group = request.getParameter("group");
			pageContext.setAttribute("group", group);
			
			// Create the correct Ancestor key
			Key<LevelGroup> theGroup = Key.create(LevelGroup.class, group);
	
			// Run an ancestor query to ensure we see the most up-to-date
			// view of the Greetings belonging to the selected Guestbook.
			List<Level> levels = ObjectifyService.ofy()
				.load()
				.type(Level.class) // We want only levels
				.ancestor(theGroup)    // Anyone in this level group
				.order("-date")       // Most recent first - date is indexed.
				.list();
%>
Levels in Group ${fn:escapeXml(group)}
begin
<%
		// Look at all of our levels
			for (Level level : levels) {
				pageContext.setAttribute("level", level.level);
%>
${fn:escapeXml(level)}
<%
			}	
		}
	} catch (Exception e) {
		String exceptMes = e.getMessage();
		pageContext.setAttribute("exceptionMessage", exceptMes);
%>		
${fn:escapeXml(exceptionMessage)}
<%
	}
%>