package com.octa.phlposcasu.Options.Adapters;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octa.phlposcasu.Models.AddCategory.AddProductCategoryModel;
import com.octa.phlposcasu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddOptionsByCategoryAdapter extends RecyclerView.Adapter<AddOptionsByCategoryAdapter.CardHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    private List<AddProductCategoryModel> addproductcategoryList;
    public boolean showShimmerCategory = true;

    RecyclerView categoryRecycler;


    DatabaseReference databaseReference;
    String productName;
    double productPrice;
    int productQuantity;
    RelativeLayout saveBtn;

    private List<String> selectedCategories;

    String productId, adminID;
    public AddOptionsByCategoryAdapter(Context context, List<AddProductCategoryModel> addproductcategoryList,
                                       RecyclerView categoryRecycler, DatabaseReference databaseReference,
                                       String productName, double productPrice, int productQuantity,
                                       RelativeLayout saveBtn, String productId,
                                       String adminID)
    {
        this.context = context;
        this.addproductcategoryList = addproductcategoryList;
        this.categoryRecycler = categoryRecycler;
        this.databaseReference = databaseReference;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.saveBtn = saveBtn;
        this.selectedCategories = new ArrayList<>();
        this.productId = productId;
        this.adminID = adminID;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_category_add_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new CardHolder(view);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerCategory) {
            holder.shimmerFrameLayout.startShimmer();
        }
        else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            AddProductCategoryModel model = addproductcategoryList.get(position);

            holder.categoryName.setText(model.getCategoryId());


            holder.categoryName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCategories.add(model.getCategoryId());
                } else {
                    selectedCategories.remove(model.getCategoryId());
                }
            });

            saveBtn.setOnClickListener(view -> saveData());
            
        }
    }

    private void saveData()
    {
        if (selectedCategories.isEmpty()) {
            Toast.makeText(context, "Please select at least one category", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            for (String category : selectedCategories) {
                Map<String, Object> productData = new HashMap<>();
                productData.put("productId", productId);
                productData.put("optionName", productName);
                productData.put("optionQuantity", productQuantity);
                productData.put("optionPrice", productPrice);

                databaseReference
                        .child("Options")
                        .child("Products")
                        .child(category)
                        .child(productId)
                        .updateChildren(productData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                clearSelection();
                                Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    private void clearSelection()
    {
        selectedCategories.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerCategory ? SHIMMER_ITEM_NUMBER : addproductcategoryList.size();

//        return categoryList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        CheckBox categoryName;
        ShimmerFrameLayout shimmerFrameLayout;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmerLayout);
            categoryName = itemView.findViewById(R.id.categoryName_txt);

        }
    }
}