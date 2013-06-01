package com.kirishikistudios.continuousops.digintojdbc;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.sun.rowset.CachedRowSetImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.spi.SyncProviderException;
import javax.sql.rowset.spi.SyncResolver;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: A12353
 * Date: 13/06/01
 * Time: 14:41
 */
public class CachedRowSetUsage {
    Context context;
    DataSource dataSource;
    Connection connection;

    public CachedRowSetUsage(){
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

    public void syncResolve() {
        CachedRowSet cachedRowSet = null;
        CachedRowSet cachedRowSet2 = null;
        try{
            System.out.println("syncResolve...");
            connection.setAutoCommit(false);

            //getting first cachedRowSet
            cachedRowSet = new CachedRowSetImpl();
            cachedRowSet.addRowSetListener(new ExampleRowSetListener());
            cachedRowSet.setCommand("SELECT * FROM COFFEES");
            cachedRowSet.execute(connection);

            while(cachedRowSet.next()){
                System.out.println("COF_NAME:" + cachedRowSet.getString("COF_NAME") + ", SUP_ID:" + cachedRowSet.getInt("SUP_ID"));
            }

            //getting second cachedRowSet
            cachedRowSet2 = new CachedRowSetImpl();
            cachedRowSet2.setCommand("SELECT * FROM COFFEES");
            cachedRowSet2.execute(connection);

            cachedRowSet.beforeFirst();
            cachedRowSet.next();
            cachedRowSet.updateInt("SUP_ID", 101);
            cachedRowSet.updateRow();
            cachedRowSet.acceptChanges();

            cachedRowSet2.next();
            cachedRowSet2.updateInt("SUP_ID", 150);
            cachedRowSet2.updateRow();
            cachedRowSet2.acceptChanges();

            connection.setAutoCommit(true);
        } catch (SyncProviderException spe){
            System.out.println("conflict detected.");

            SyncResolver resolver = spe.getSyncResolver();

            Object crsValue;
            Object resolverValue;
            Object resolvedValue;

            try {
                while (resolver.nextConflict()) {

                    if (resolver.getStatus() == SyncResolver.UPDATE_ROW_CONFLICT) {
                        int row = resolver.getRow();
                        cachedRowSet2.absolute(row);

                        int colCount = cachedRowSet2.getMetaData().getColumnCount();
                        for (int j = 1; j <= colCount; j++) {
                            System.out.println("conflict value: " + resolver.getConflictValue(j));
                            if (resolver.getConflictValue(j) != null) {
                                crsValue = cachedRowSet2.getObject(j);
                                resolverValue = resolver.getConflictValue(j);

                                resolvedValue = crsValue;

                                resolver.setResolvedValue(j, resolvedValue);
                                System.out.println("conflict resolved.");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch(MySQLSyntaxErrorException ex){
            ex.getSQLState();
            ex.getMessage();
            ex.getErrorCode();
        } catch(SQLException ex) {
            ex.printStackTrace();
        } finally{
            if(cachedRowSet != null){
                try {
                    cachedRowSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(cachedRowSet2 != null){
                try {
                    cachedRowSet2.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            close();
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

