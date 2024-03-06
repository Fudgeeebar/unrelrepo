package com.octa.phlposcasu.Products.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.octa.phlposcasu.Models.OptionsChildModel;
import com.octa.phlposcasu.Options.Models.OptionsProductsByCategoryModel;
import com.octa.phlposcasu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOptionsProductsByProductAdapter extends RecyclerView.Adapter<AddOptionsProductsByProductAdapter.CardHolder> {

    Context context;
    private List<OptionsProductsByCategoryModel> optionsProductsByCategoryList;
    private List<OptionsProductsByCategoryModel> selectedOptionsList; // New selected options list
    public boolean showShimmerProduct = true;
    DatabaseReference databaseReference;
    boolean required = false;
    String adminID, productId, categoryId, mainCategoryID;
    RelativeLayout saveBtn;
    public AddOptionsProductsByProductAdapter(Context context, List<OptionsProductsByCategoryModel> optionsProductsByCategoryList,
                                              DatabaseReference databaseReference, String adminID,
                                              RelativeLayout saveBtn, String productId, String categoryId,
                                              String mainCategoryID)
    {
        this.context = context;
        this.optionsProductsByCategoryList = optionsProductsByCategoryList;
        this.databaseReference = databaseReference;
        this.adminID = adminID;
        this.saveBtn = saveBtn;
        this.selectedOptionsList = new ArrayList<>();
        this.productId = productId;
        this.categoryId = categoryId;
        this.mainCategoryID = mainCategoryID;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_category_add_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);


        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerProduct) {
            holder.shimmerFrameLayoutProduct.startShimmer();
        }
        else {
            holder.shimmerFrameLayoutProduct.stopShimmer();
            holder.shimmerFrameLayoutProduct.setShimmer(null);

            OptionsProductsByCategoryModel model = optionsProductsByCategoryList.get(position);

            holder.categoryName.setText(model.getOptionName());

            holder.categoryName.setChecked(model.isChecked());

            holder.categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = holder.categoryName.isChecked();

                    model.setChecked(isChecked);
                    if (isChecked) {
                        selectedOptionsList.add(model); // Add the selected option to the list
                    } else {
                        selectedOptionsList.remove(model); // Remove the deselected option from the list
                    }

                }
            });

            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveCheckedOptionsToFirebase();
                }
            });

        }
    }

    private void saveCheckedOptionsToFirebase() {

        for (OptionsProductsByCategoryModel model : selectedOptionsList) { // Iterate over the selected options list
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("optionName", model.getOptionName());
            optionMap.put("optionPrice", model.getOptionPrice());
            optionMap.put("optionQuantity", model.getOptionQuantity());
            optionMap.put("productId", model.getProductId());

            databaseReference.child("OptionsByProduct").child("SubOptions")
                    .child(productId).child(mainCategoryID)
                    .child(model.getProductId()).setValue(optionMap);
        }

        // Optional: Show a toast message or perform any other action after saving the data
        Toast.makeText(context, "Checked options saved to Firebase", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerProduct ? SHIMMER_ITEM_NUMBER : optionsProductsByCategoryList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        CheckBox categoryName;
        ShimmerFrameLayout shimmerFrameLayoutProduct;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutProduct = itemView.findViewById(R.id.shimmerLayout);
            categoryName = itemView.findViewById(R.id.categoryName_txt);
//            productPriceTxt = itemView.findViewById(R.id.productPrice_txt);
//            productImage = itemView.findViewById(R.id.productImage);

        }
    }
}