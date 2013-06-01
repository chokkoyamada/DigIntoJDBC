package com.kirishikistudios.continuousops.digintojdbc;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: A12353
 * Date: 13/05/31
 * Time: 9:10
 */
@WebServlet(urlPatterns = {"/chapter7"})
public class Chapter7 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CachedRowSetUsage jdbcRowSetUsage = new CachedRowSetUsage();
        jdbcRowSetUsage.syncResolve();
        jdbcRowSetUsage.close();

        JoinRowSetUsage joinRowSetUsage = new JoinRowSetUsage();
        joinRowSetUsage.executeQuery("Acme, Inc.");
        joinRowSetUsage.close();

        response.setContentType("text/html; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.println("Chapter7");
    }
}
