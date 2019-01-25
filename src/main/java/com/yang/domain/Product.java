package com.yang.domain;

public class Product {
    private int id;
    private String pid;
    private String url;
    private String title;
    private String brand;
    private String proName;
    private String proPrice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProPrice() {
        return proPrice;
    }

    public void setProPrice(String proPrice) {
        this.proPrice = proPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", pid='" + pid + '\'' +
                ", url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", brand='" + brand + '\'' +
                ", proName='" + proName + '\'' +
                ", proPrice='" + proPrice + '\'' +
                '}';
    }
}
