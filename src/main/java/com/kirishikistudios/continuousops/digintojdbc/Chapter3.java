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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Chapter3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Context context;
        Connection connection = null;
        try {
            context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            connection = dataSource.getConnection();

            insertRecord(connection);

            HashMap hashMap = selectRecord(connection);
            request.setAttribute("name", hashMap.get("name-1"));
            request.setAttribute("address", hashMap.get("address-1"));
            request.setAttribute("tel", hashMap.get("tel-1"));
            request.setAttribute("email", hashMap.get("email-1"));

            request.setAttribute("result", "success");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.getServletContext().getRequestDispatcher("/chapter3/index.jsp").forward(request, response);
    }

    private void insertRecord(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO address (name, address, tel, email) VALUES(?, ?, ?, ?)");
        preparedStatement.setString(1, getParameter("name"));
        preparedStatement.setString(2, getParameter("address"));
        preparedStatement.setString(3, getParameter("tel"));
        preparedStatement.setString(4, getParameter("email"));
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    private HashMap selectRecord(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM address LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        HashMap<String, String> map = new HashMap<>();
        if(resultSet.next()){
            map.put("name-1", resultSet.getString("name"));
            map.put("address-1", resultSet.getString("address"));
            map.put("tel-1", resultSet.getString("tel"));
            map.put("email-1", resultSet.getString("email"));
        }
        preparedStatement.close();
        return map;
    }

    private String getParameter(String key){
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "yamada");
        map.put("address", "urawa");
        map.put("tel", "090-1234-5678");
        map.put("email", "yamada@example.com");
        return map.get(key);
    }
}

