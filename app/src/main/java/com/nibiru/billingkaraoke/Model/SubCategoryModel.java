package com.nibiru.billingkaraoke.Model;

public class SubCategoryModel {

    private String kodekategori;
    private String namakategori;
    private String fotokategori;

    public SubCategoryModel(String kodekategori, String namakategori, String fotokategori) {
        this.kodekategori = kodekategori;
        this.namakategori = namakategori;
        this.fotokategori = fotokategori;
    }

    public String getKodekategori() {
        return kodekategori;
    }

    public void setKodekategori(String kodekategori) {
        this.kodekategori = kodekategori;
    }

    public String getNamakategori() {
        return namakategori;
    }

    public void setNamakategori(String namakategori) {
        this.namakategori = namakategori;
    }

    public String getFotokategori() {
        return fotokategori;
    }

    public void setFotokategori(String fotokategori) {
        this.fotokategori = fotokategori;
    }
}
