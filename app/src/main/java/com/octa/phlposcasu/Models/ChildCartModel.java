package com.octa.phlposcasu.Models;

public class ChildCartModel {

    private String optionName, pOptionId;
    private int optionQuantity;
    private double optionPrice;
    private boolean checked;



    public ChildCartModel()
    {

    }

    public String getOptionName() {
        return optionName;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public String getpOptionId() {
        return pOptionId;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public boolean isChecked() {
        return checked;
    }
}

