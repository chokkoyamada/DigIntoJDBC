<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: yamadanaoyuki
  Date: 2013/05/27
  Time: 21:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DigIntoJDBC Chapter4</title>
</head>
<body>
<div>
    Result:${requestScope['result']}
</div>
<table border='1'>
<tr>
    <th>cof_name</th>
    <th>sup_id</th>
    <th>price</th>
    <th>sales</th>
    <th>total</th>
</tr>
<c:forEach var="row" items="${requestScope['resultList']}">
    <tr>
        <td><c:out value="${row.get('cof_name')}" /></td>
        <td><c:out value="${row.get('sup_id')}" /></td>
        <td><c:out value="${row.get('price')}" /></td>
        <td><c:out value="${row.get('sales')}" /></td>
        <td><c:out value="${row.get('total')}" /></td>
    </tr>
</c:forEach>

</table>
</body>
</html>