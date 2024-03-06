package com.octa.phlposcasu.Models;

public class ParentCartModel {

    private String productCategory, productId, productImage, productName, productOrderId;
    private double productPrice;
    private int productQuantity;


    public ParentCartModel()
    {

    }

    public String getProductCategory() {
        return productCategory;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductOrderId() {
        return productOrderId;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }
}

