package com.example.expandable.Model;

import android.widget.TextView;

public class Reviews {
String pid,review,username;
public Reviews(){}

    public Reviews(String pid, String review, String username) {
        this.pid = pid;
        this.review = review;
        this.username = username;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
