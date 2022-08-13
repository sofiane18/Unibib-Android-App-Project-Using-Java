package com.example.myapplication;

public class AdminDemandsDemandItem {

    private String ref;
    public AdminBooksBookItem bookItem;
    public AdminUsersUserItem userItem;
    private String demandDate;
    private String status;

    public AdminDemandsDemandItem(String ref, AdminBooksBookItem bookItem, AdminUsersUserItem userItem, String demandDate, String status) {
        this.ref = ref;
        this.bookItem = bookItem;
        this.userItem = userItem;
        this.demandDate = demandDate;
        this.status = status;
    }

    public String getRef() {
        return ref;
    }


    public String getDemandDate() {
        return demandDate;
    }


    public String getStatus() {
        return status;
    }
}
