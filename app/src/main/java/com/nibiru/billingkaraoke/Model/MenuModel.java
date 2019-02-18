package com.nibiru.billingkaraoke.Model;

public class MenuModel {

    private String cid;
    private String product_name;
    private String product_image;
    private String product_harga;
    private String product_diskon;
    private String product_stock;
    private String harga_net;


    public MenuModel(String cid, String product_name, String product_image, String product_harga, String product_diskon, String product_stock, String harga_net) {
        this.cid = cid;
        this.product_name = product_name;
        this.product_image = product_image;
        this.product_harga = product_harga;
        this.product_diskon = product_diskon;
        this.product_stock = product_stock;
        this.harga_net = harga_net;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_harga() {
        return product_harga;
    }

    public void setProduct_harga(String product_harga) {
        this.product_harga = product_harga;
    }

    public String getProduct_diskon() {
        return product_diskon;
    }

    public void setProduct_diskon(String product_diskon) {
        this.product_diskon = product_diskon;
    }

    public String getProduct_stock() {
        return product_stock;
    }

    public void setProduct_stock(String product_stock) {
        this.product_stock = product_stock;
    }

    public String getHarga_net() {
        return harga_net;
    }

    public void setHarga_net(String harga_net) {
        this.harga_net = harga_net;
    }
}
