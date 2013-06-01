package com.kirishikistudios.continuousops.digintojdbc;

import com.sun.rowset.CachedRowSetImpl;
import com.sun.rowset.JoinRowSetImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.JoinRowSet;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: A12353
 * Date: 13/06/01
 * Time: 18:42
 */
public class JoinRowSetUsage {
    Context context;
    DataSource dataSource;
    Connection connection;

    public JoinRowSetUsage(){
        try {
            context = new InitialContext();
            dataSource = (DataSource)context.lookup("java:comp/env/jdbc/continuousops");
            connection = dataSource.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void executeQuery(String supplierName) {

        CachedRowSet coffees = null;
        CachedRowSet suppliers = null;
        JoinRowSet jrs = null;

        try {
            coffees = new CachedRowSetImpl();
            coffees.setCommand("SELECT * FROM COFFEES");
            coffees.execute(connection);

            suppliers = new CachedRowSetImpl();
            suppliers.setCommand("SELECT * FROM SUPPLIERS");
            suppliers.execute(connection);

            jrs = new JoinRowSetImpl();
            jrs.addRowSet(coffees, "SUP_ID");
            jrs.addRowSet(suppliers, "SUP_ID");

            System.out.println("Coffees bought from " + supplierName + ": ");
            while (jrs.next()) {
                if (jrs.getString("SUP_NAME").equals(supplierName)) {
                    String coffeeName = jrs.getString(1);
                    System.out.println("     " + coffeeName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if (jrs != null) { jrs.close(); }
                if (suppliers != null) { suppliers.close(); }
                if (coffees != null) { coffees.close(); }
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public void close(){
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
