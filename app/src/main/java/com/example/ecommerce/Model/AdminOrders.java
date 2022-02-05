package com.example.ecommerce.Model;

public class AdminOrders {

    private  String orderId, sid, userPhone, sellerPhone, date, orderStatus;

    public AdminOrders() {
    }

    public AdminOrders(String id, String sid, String userPhone, String sellerPhone, String date, String orderStatus) {
        this.orderId = id;
        this.sid = sid;
        this.userPhone = userPhone;
        this.sellerPhone = sellerPhone;
        this.date = date;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String id) {
        this.orderId = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
