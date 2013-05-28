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

public class Chapter5 extends HttpServlet {
    ResultSet resultSet = null;
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

            //resultList = viewTable(connection, "continuousops");
            //updateTableRevised(connection, "continuousops");
            batchUpdate(connection);

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
        resultSet.next();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        resultSet.next();
        resultSet.next();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        resultSet.previous();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        resultSet.last();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        resultSet.absolute(4);
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        resultSet.afterLast();
        System.out.println(resultSet.next());
        return resultList;
    }

    private void updateTableRevised(Connection connection, String dbName) throws SQLException {
        float percentage = 0.3f;
        String query = "SELECT cof_name, sup_id, price, sales, total FROM " + dbName + ".coffees" ;
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        resultSet = statement.executeQuery(query);
        resultSet.next();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
        float f = resultSet.getFloat("PRICE");
        resultSet.updateFloat("PRICE", f * percentage);
        resultSet.updateRow();
        System.out.println("|" + resultSet.getString("COF_NAME")
                + "|" + resultSet.getInt("SUP_ID")
                + "|" + resultSet.getBigDecimal("PRICE") + "|");
    }

    public void batchUpdate(Connection connection) throws SQLException {

        Statement stmt = null;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();

            stmt.addBatch(
                    "INSERT INTO COFFEES " +
                            "VALUES('Amaretto', 49, 9.99, 0, 0)");

            stmt.addBatch(
                    "INSERT INTO COFFEES " +
                            "VALUES('Hazelnut', 49, 9.99, 0, 0)");

            stmt.addBatch(
                    "INSERT INTO COFFEES " +
                            "VALUES('Amaretto_decaf', 49, " +
                            "10.99, 0, 0)");

            stmt.addBatch(
                    "INSERT INTO COFFEES " +
                            "VALUES('Hazelnut_decaf', 49, " +
                            "10.99, 0, 0)");

            int [] updateCounts = stmt.executeBatch();
            System.out.println(updateCounts);
            connection.commit();

        } catch(BatchUpdateException b) {
            b.printStackTrace();
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (stmt != null) { stmt.close(); }
            connection.setAutoCommit(true);
        }
    }
}
