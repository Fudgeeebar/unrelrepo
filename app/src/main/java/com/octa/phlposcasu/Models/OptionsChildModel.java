package com.octa.phlposcasu.Models;

public class OptionsChildModel {


    private String optionName, productId;
    private int optionQuantity;
    private double optionPrice;
    private boolean isChecked = false;

    public OptionsChildModel() {

    }

    public String getOptionName() {
        return optionName;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public String getProductId() {
        return productId;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}

