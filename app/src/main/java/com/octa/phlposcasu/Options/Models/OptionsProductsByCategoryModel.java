package com.octa.phlposcasu.Options.Models;

import com.octa.phlposcasu.Models.OptionsChildModel;

import java.util.ArrayList;
import java.util.List;

public class OptionsProductsByCategoryModel
{
    private String optionName, productId;
    private int optionQuantity;
    private double optionPrice;

    private boolean isChecked;
    private List<OptionsProductsByCategoryModel> selectedOptionsList;

    public OptionsProductsByCategoryModel() {
        // Required empty constructor for Firebase Firestore deserialization
    }


    public String getOptionName() {
        return optionName;
    }

    public String getProductId() {
        return productId;
    }

    public int getOptionQuantity() {
        return optionQuantity;
    }

    public double getOptionPrice() {
        return optionPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<OptionsProductsByCategoryModel> getSelectedOptionsList() {
        return selectedOptionsList;
    }

    public void setSelectedOptionsList(List<OptionsProductsByCategoryModel> selectedOptionsList) {
        this.selectedOptionsList = selectedOptionsList;
    }


}
