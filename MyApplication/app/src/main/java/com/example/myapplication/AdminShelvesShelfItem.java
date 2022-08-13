package com.example.myapplication;

import java.util.ArrayList;

public class AdminShelvesShelfItem {
    private String ref,name;
    private int sides,rows,cols;
    private ArrayList<AdminBooksBookItem> booksList;

    public AdminShelvesShelfItem(String ref, String name, int sides, int rows, int cols, ArrayList<AdminBooksBookItem> booksList) {
        this.ref = ref;
        this.name = name;
        this.sides = sides;
        this.rows = rows;
        this.cols = cols;
        this.booksList = booksList;
    }

    public String getRef() {
        return ref;
    }

    public String getName() {
        return name;
    }

    public int getSides() {
        return sides;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public ArrayList<AdminBooksBookItem> getBooksList() {
        return booksList;
    }
}
