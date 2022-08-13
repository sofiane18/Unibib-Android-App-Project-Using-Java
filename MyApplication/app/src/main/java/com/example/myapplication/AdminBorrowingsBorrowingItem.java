package com.example.myapplication;

public class AdminBorrowingsBorrowingItem {
    private String ref,deliverDate;
    private int late;
    private int retrieved;
    private AdminDemandsDemandItem demandItem;

    public AdminBorrowingsBorrowingItem(String ref, String deliverDate, int late, int retrieved, AdminDemandsDemandItem demandItem) {
        this.ref = ref;
        this.deliverDate = deliverDate;
        this.late = late;
        this.demandItem = demandItem;
        this.retrieved = retrieved;
    }

    public String getRef() {
        return ref;
    }

    public String getDeliverDate() {
        return deliverDate;
    }

    public int getLate() {
        return late;
    }

    public int getRetrieved() {
        return retrieved;
    }

    public AdminDemandsDemandItem getDemandItem() {
        return demandItem;
    }
}
