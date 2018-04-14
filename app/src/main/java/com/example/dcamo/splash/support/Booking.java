package com.example.dcamo.splash.support;

public class Booking {
    public String __v, customerName, status, sorry, customerNumber, tableNumber, timeStart, timeEnd, quantity, callback, bookingDate, _id, createdDate, userId;



    public Booking(){
        this.sorry = "notSorry";
    }

    public Booking(String __v, String customerName, String customerNumber, String tableNumber, String timeStart, String timeEnd, String quantity, String callback, String bookingDate, String _id, String createdDate, String userId, String status) {
        this.__v = __v;
        this.customerName = customerName;
        this.customerNumber = customerNumber;
        this.tableNumber = tableNumber;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.quantity = quantity;
        this.callback = callback;
        this.bookingDate = bookingDate;
        this._id = _id;
        this.createdDate = createdDate;
        this.status = status;
        this.userId = userId;
        this.sorry = "sorry";
    }



}
