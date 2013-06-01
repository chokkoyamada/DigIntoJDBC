package com.kirishikistudios.continuousops.digintojdbc;

import javax.sql.RowSetEvent;
import javax.sql.RowSetListener;

/**
 * User: A12353
 * Date: 13/06/01
 * Time: 18:17
 */
public class ExampleRowSetListener implements RowSetListener {

    public void rowSetChanged(RowSetEvent event) {
        System.out.println("Called rowSetChanged in ExampleRowSetListener");
    }

    public void rowChanged(RowSetEvent event) {
        System.out.println("Called rowChanged in ExampleRowSetListener");
    }

    public void cursorMoved(RowSetEvent event) {
        System.out.println("Called cursorMoved in ExampleRowSetListener");
    }
}
