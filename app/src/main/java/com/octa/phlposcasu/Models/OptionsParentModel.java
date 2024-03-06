package com.octa.phlposcasu.Models;

import java.util.ArrayList;
import java.util.List;

public class OptionsParentModel {

    private String optionId, optionTitle, productId;
    private int maxSelection;
    private boolean required;

    private List<OptionsChildModel> childOptions;
    private List<OptionsChildModel> selectedChildOptions;



    public OptionsParentModel() {
        // Required empty constructor for Firebase Firestore deserialization
        selectedChildOptions = new ArrayList<>();
    }


    public String getOptionId() {
        return optionId;
    }

    public String getOptionTitle() {
        return optionTitle;
    }

    public String getProductId() {
        return productId;
    }

    public int getMaxSelection() {
        return maxSelection;
    }

    public boolean isRequired() {
        return required;
    }

    public void setSelectedChildOptions(List<OptionsChildModel> selectedChildOptions) {
        this.selectedChildOptions = selectedChildOptions;
    }

    public List<OptionsChildModel> getSelectedChildOptions() {
        return selectedChildOptions;
    }

    public void initializeSelectedChildOptions() {
        selectedChildOptions = new ArrayList<>();
    }
}

