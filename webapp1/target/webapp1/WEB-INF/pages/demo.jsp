<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="si" 
  class="hr.fer.zemris.web.demo.StringInfo" 
  scope="request" />
  
  

<jsp:setProperty property="text" name="si" value="Perica" />
Radim s tekstom: <jsp:getProperty property="text" name="si"/> odnosno ${si.text}<br/>
Duljina je: <jsp:getProperty property="length" name="si"/> ili ${si.length}.<br/>
Prvi znak je: ${si.getCharAt(1)}<br/>
Prvi znak reverznog niza je: ${si.reversed.getCharAt(1)}<br/>

<ol>
<c:forEach var="u" items="${userList}">
<li>${u.lastName}, ${u.firstName}</li>
</c:forEach>
</ol>

<ol>
<c:forEach var="e" items="${primjerci}">
<li>${e.key} ==&gt; ${e.value.lastName}, ${e.value.firstName}</li>
</c:forEach>
</ol>

Tko je Odlikaš? ${primjerci["Odlikaš"].lastName}