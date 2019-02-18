package com.nibiru.billingkaraoke.Model;

public class DetailOrderModel {



    private String trx_cid;
    private String product_cid;
    private String product_name;
    private String qty;
    private String harga;
    private String diskon;
    private String harga_net;
    private String total_harga;

    public DetailOrderModel(String trx_cid, String product_cid, String product_name, String qty, String harga, String diskon, String harga_net, String total_harga) {
        this.trx_cid = trx_cid;
        this.product_cid = product_cid;
        this.product_name = product_name;
        this.qty = qty;
        this.harga = harga;
        this.diskon = diskon;
        this.harga_net = harga_net;
        this.total_harga = total_harga;
    }

    public String getTrx_cid() {
        return trx_cid;
    }

    public void setTrx_cid(String trx_cid) {
        this.trx_cid = trx_cid;
    }

    public String getProduct_cid() {
        return product_cid;
    }

    public void setProduct_cid(String product_cid) {
        this.product_cid = product_cid;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDiskon() {
        return diskon;
    }

    public void setDiskon(String diskon) {
        this.diskon = diskon;
    }

    public String getHarga_net() {
        return harga_net;
    }

    public void setHarga_net(String harga_net) {
        this.harga_net = harga_net;
    }

    public String getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(String total_harga) {
        this.total_harga = total_harga;
    }
}
