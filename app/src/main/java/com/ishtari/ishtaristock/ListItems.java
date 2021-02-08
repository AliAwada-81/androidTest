package com.ishtari.ishtaristock;

public class ListItems {
    public String id;
    public String Sku;
    public String barcode;
    public int quantity;
    public String imgUrl;
    public int  counter;

    ListItems(String id,String Sku,String barcode,int quantity, String imgUrl,int  counter){
        this.id = id;
        this.Sku = Sku;
        this.barcode = barcode;
        this.quantity = quantity;
        this.imgUrl =  imgUrl;
        this.counter = counter;
    }
}
