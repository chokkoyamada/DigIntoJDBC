package com.kirishikistudios.continuousops.digintojdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(urlPatterns = {"/chapter6"})
public class Chapter6 extends HttpServlet {
    Connection connection = null;
    ResultSet resultSet = null;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Context context;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;

        try {
            context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            connection = dataSource.getConnection();

            /*
            connection.setAutoCommit(false);

            preparedStatement1 = connection.prepareStatement("UPDATE COFFEES SET PRICE=? WHERE COF_NAME = 'Amaretto' LIMIT 1");
            preparedStatement2 = connection.prepareStatement("UPDATE COFFEES SET PRICE=? WHERE COF_NAME = 'Espresso' LIMIT 1");

            preparedStatement1.setFloat(1, 7.99f);
            preparedStatement1.executeUpdate();

            preparedStatement2.setFloat(1, 6.99f);
            preparedStatement2.executeUpdate();

            connection.commit();
            */

            int isolationLevel = connection.getTransactionIsolation();
            System.out.println(isolationLevel);
        } catch (NamingException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement1 != null){
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement2 != null){
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        Savepoint savepoint1 = null;
        Savepoint savepoint2 = null;
        try {
            context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            connection = dataSource.getConnection();

            connection.setAutoCommit(false);

            savepoint1 = connection.setSavepoint("point1");

            preparedStatement1 = connection.prepareStatement("UPDATE COFFEES SET PRICE=? WHERE COF_NAME = 'Amaretto' LIMIT 1");
            preparedStatement2 = connection.prepareStatement("UPDATE COFFEES SET PRICE=? WHERE COF_NAME = 'Espresso' LIMIT 1");

            preparedStatement1.setFloat(1, 5.99f);
            preparedStatement1.executeUpdate();

            savepoint2 = connection.setSavepoint("point2");

            preparedStatement2.setFloat(1, 7.99f);
            preparedStatement2.executeUpdate();

            connection.rollback(savepoint2);

            connection.commit();

        } catch (NamingException | SQLException e) {
            try {
                connection.rollback(savepoint1);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement1 != null){
                try {
                    preparedStatement1.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(preparedStatement2 != null){
                try {
                    preparedStatement2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        response.setContentType("text/html; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("Chapter6");
    }
}

