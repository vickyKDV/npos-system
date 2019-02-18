package com.nibiru.billingkaraoke.Model;

import java.util.ArrayList;

public class ReportModel {

    private String img_produk;
    private String product_name;
    private String harga_net;
    private String qty;
    private String total_harga;

    public ReportModel(String img_produk,String product_name, String harga_net, String qty, String total_harga) {
        this.img_produk = img_produk;
        this.product_name = product_name;
        this.harga_net = harga_net;
        this.qty = qty;
        this.total_harga = total_harga;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getHarga_net() {
        return harga_net;
    }

    public void setHarga_net(String harga_net) {
        this.harga_net = harga_net;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }

    public String getImg_produk() {
        return img_produk;
    }

    public void setImg_produk(String img_produk) {
        this.img_produk = img_produk;
    }
}
