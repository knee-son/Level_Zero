package com.level_zero.greeniq;

public class History extends AppCompat{
    private String date, totalCarbon;

    public History(String date, String totalCarbon) {
        this.date = date;
        this.totalCarbon = totalCarbon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalCarbon() {
        return totalCarbon;
    }

    public void setTotalCarbon(String totalCarbon) {
        this.totalCarbon = totalCarbon;
    }
}
