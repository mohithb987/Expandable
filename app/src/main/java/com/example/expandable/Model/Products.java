package com.example.expandable.Model;

public class Products {
    public String category,pname,image,description,price,pid,date,time;
    public Products(){ //COMPULSORY
// right-click here and click "generate" to get the below methods
    }

    public Products(String category, String pname, String image, String description, String price, String pid, String date, String time) {
        this.category = category;
        this.pname = pname;
        this.image = image;
        this.description = description;
        this.price = price;
        this.pid = pid;
        this.date = date;
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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


}
