package com.billaway.lyfepoints.data.models;


public class Brand {
    private String title;
    private String icon;

    public Brand(String title, String icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
