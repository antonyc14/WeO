package com.example.ecommerce.Model;

public class Users {
    private String name, phone, password, image, email;

    public Users() {
    }

    public Users(String name, String phone, String password, String image, String email) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.image = image;
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return email;
    }

    public void setAddress(String address) {
        this.email = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
}
