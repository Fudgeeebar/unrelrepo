package com.octa.phlposcasu.Models;

public class ProductModel {

    private String productId, productImage, productName, productCategory;
    private int productQuantity;
    private double productPrice;



    public ProductModel() {
        // Required empty constructor for Firebase Firestore deserialization
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

    public double getProductPrice() {
        return productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public String getProductCategory() {
        return productCategory;
    }
}

