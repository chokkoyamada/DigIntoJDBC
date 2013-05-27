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
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chapter4 extends HttpServlet {
    ResultSet resultSet = null;
    ResultSetMetaData resultSetMetaData = null;
    PreparedStatement preparedStatement = null;
    List<HashMap> resultList = new ArrayList<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        Context context;
        Connection connection = null;

        try {
            context = new InitialContext();
            DataSource dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            connection = dataSource.getConnection();

            resultList = viewTable(connection, "continuousops");
            resultSetMetaData = resultSet.getMetaData();

            DatabaseMetaData databaseMetaData = connection.getMetaData();
            //System.out.printf("[TYPE_FORWARD_ONLY]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
            //System.out.printf("[TYPE_SCROLL_INSENSITIVE]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
            //System.out.printf("[TYPE_SCROLL_SENSITIVE]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM coffees");
            statement.close();

            //System.out.printf("[TYPE_FORWARD_ONLY]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_FORWARD_ONLY));
            //System.out.printf("[TYPE_SCROLL_INSENSITIVE]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_INSENSITIVE));
            //System.out.printf("[TYPE_SCROLL_SENSITIVE]%s%n", databaseMetaData.supportsResultSetType(ResultSet.TYPE_SCROLL_SENSITIVE));

            //System.out.printf("[CONCUR_READ_ONLY]%s%n", databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY));
            //System.out.printf("[CONCUR_UPDATABLE]%s%n", databaseMetaData.supportsResultSetConcurrency(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE));

            DatabaseMetaData dbMetaData = connection.getMetaData();
            System.out.println("ResultSet.HOLD_CURSORS_OVER_COMMIT = " +
                    ResultSet.HOLD_CURSORS_OVER_COMMIT);

            System.out.println("ResultSet.CLOSE_CURSORS_AT_COMMIT = " +
                    ResultSet.CLOSE_CURSORS_AT_COMMIT);

            System.out.println("Default cursor holdability: " +
                    dbMetaData.getResultSetHoldability());

            System.out.println("Supports HOLD_CURSORS_OVER_COMMIT? " +
                    dbMetaData.supportsResultSetHoldability(
                            ResultSet.HOLD_CURSORS_OVER_COMMIT));

            System.out.println("Supports CLOSE_CURSORS_AT_COMMIT? " +
                    dbMetaData.supportsResultSetHoldability(
                            ResultSet.CLOSE_CURSORS_AT_COMMIT));

            request.setAttribute("resultList", resultList);
            request.setAttribute("result", "success");
        } catch (NamingException | SQLException e) {
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
            if(preparedStatement != null){
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        this.getServletContext().getRequestDispatcher("/chapter4/index.jsp").forward(request, response);
    }

    private List<HashMap> viewTable(Connection connection, String dbName) throws SQLException {
        String query = "SELECT cof_name, sup_id, price, sales, total FROM " + dbName + ".coffees" ;
        preparedStatement = connection.prepareStatement(query);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            HashMap<String, Object> row = new HashMap<>();
            row.put("cof_name", resultSet.getString("COF_NAME"));
            row.put("sup_id", resultSet.getInt("SUP_ID"));
            row.put("price", resultSet.getFloat("PRICE"));
            row.put("sales", resultSet.getInt("SALES"));
            row.put("total", resultSet.getInt("TOTAL"));
            resultList.add(row);
        }
        return resultList;
    }
}
