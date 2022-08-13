package com.example.myapplication;

public class AdminBooksBookItem {
    private String ref;
    private String image ;
    private String title ;
    private String author ;
    private String place ;
    private int quantity ;
    private String edition ;
    private String description ;
    private String tags ;
    private String shelfName ;
    private String shelfCol ;
    private String shelfRow ;
    private String shelfSide ;
    public AdminBooksBookItem(String ref, String image, String title, String author, String place, int quantity, String edition, String description, String tags, String shelfName, String shelfCol, String shelfRow, String shelfSide) {
        this.ref = ref;
        this.image = image;
        this.title = title;
        this.author = author;
        this.place = place;
        this.quantity = quantity;
        this.edition = edition;
        this.description = description;
        this.tags = tags;
        this.shelfName = shelfName;
        this.shelfCol = shelfCol;
        this.shelfRow = shelfRow;
        this.shelfSide = shelfSide;
    }

    public String getRef() {
        return ref;
    }
    public String getImage() {
        return image;
    }
    public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getPlace() {
        return place;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getEdition() {
        return edition;
    }
    public String getDescription() {
        return description;
    }
    public String getTags() {
        return tags;
    }
    public String getShelfName() {
        return shelfName;
    }
    public String getShelfCol() {
        return shelfCol;
    }
    public String getShelfRow() {
        return shelfRow;
    }
    public String getShelfSide() {
        return shelfSide;
    }
}
