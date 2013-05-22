package com.kirishikistudios.continuousops.digintojdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Chapter2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        request.setAttribute("subject", "JDBC");
        Context context;
        try {
            context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            Connection connection = dataSource.getConnection();
            request.setAttribute("message", "Successfully connected to database.");
            connection.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getServletContext().getRequestDispatcher("/chapter2/index.jsp").forward(request, response);
    }
}
