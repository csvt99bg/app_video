package com.example.app_video.Adapter.Categories;

public class Categories {
    String imgCategory, name;
    String Url;

    public Categories(String imgCategory, String name) {
        this.imgCategory = imgCategory;
        this.name = name;

    }

    public String getImgCategory() {
        return imgCategory;
    }

    public void setImgCategory(String imgCategory) {
        this.imgCategory = imgCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
