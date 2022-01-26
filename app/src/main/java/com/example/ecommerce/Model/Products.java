package com.example.ecommerce.Model;

public class Products {
    private String pname, description,
            image, category, pid, date, time,
            productState, sellerPhone, sellerAddress,
            sellerName;

    private Integer price;

    public Products()
    {

    }

    public Products(String pname, String description, Integer price,
                    String image, String category, String pid, String date,
                    String time, String productState, String sellerPhone,
                    String sellerAddress, String sellerName) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.productState = productState;
        this.sellerPhone = sellerPhone;
        this.sellerAddress = sellerAddress;
        this.sellerName = sellerName;
    }
    //    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time, String productState) {
//        this.pname = pname;
//        this.description = description;
//        this.price = price;
//        this.image = image;
//        this.category = category;
//        this.pid = pid;
//        this.date = date;
//        this.time = time;
//        this.productState = productState;
//
//    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductState() {
        return productState;
    }

    public void setProductState(String productState) {
        this.productState = productState;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
