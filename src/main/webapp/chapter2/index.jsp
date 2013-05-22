<%@ page contentType="text/html; charsert=UTF-8" %>
<%@ page import="javax.naming.Context, javax.naming.InitialContext, javax.sql.DataSource, java.sql.Connection " %>
<!DOCTYPE html>
<html>
<body>
<h2>Hello ${requestScope['subject']}!</h2>
<%
    Context context = new InitialContext();
    DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
    Connection connection = dataSource.getConnection();
    connection.close();
%>
</body>
</html>
