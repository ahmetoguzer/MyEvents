package com.ahmet.app.myevents;

/**
 * Created by Ahmet Oguz Er on 21.09.2018.
 */

public class Events {

    public String uid,tittle,comment,date;

    public Events(String uid, String tittle, String comment, String date) {
        this.uid = uid;
        this.tittle = tittle;
        this.comment = comment;
        this.date = date;
    }

    public Events() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
