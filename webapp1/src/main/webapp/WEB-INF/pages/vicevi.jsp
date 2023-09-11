<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<p>Lista viceva: </p>

<table border="1">
<tr><th>Vic</th><th>Glasova</th></tr>
<c:forEach var="vicZapis" items="${vicevi}">
  <tr><td>${vicZapis.vic}</td><td>${vicZapis.glasovi}</td></tr>
</c:forEach>
</table>
