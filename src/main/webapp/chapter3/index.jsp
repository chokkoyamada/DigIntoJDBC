<%--
  Created by IntelliJ IDEA.
  User: A12353
  Date: 13/05/24
  Time: 9:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dig Into JDBC Chapter3</title>
</head>
<body>
<table>
    <tr>
        <th>name</th><td>${requestScope["name"]}</td>
    </tr>
    <tr>
        <th>address</th><td>${requestScope["address"]}</td>
    </tr>
    <tr>
        <th>tel</th><td>${requestScope["tel"]}</td>
    </tr>
    <tr>
        <th>email</th><td>${requestScope["email"]}</td>
    </tr>
</table>
<p>
Result: ${requestScope["result"]}
</p>
</body>
</html>