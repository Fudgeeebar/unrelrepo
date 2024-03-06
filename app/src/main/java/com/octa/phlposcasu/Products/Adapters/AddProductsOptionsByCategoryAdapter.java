package com.octa.phlposcasu.Products.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octa.phlposcasu.Models.AddCategory.AddProductCategoryModel;
import com.octa.phlposcasu.Models.CategoryModel;
import com.octa.phlposcasu.Products.AsyncTask.AsyncTaskAddOptionsProductsByProduct;
import com.octa.phlposcasu.Products.AsyncTask.AsyncTaskProductsByCategory;
import com.octa.phlposcasu.R;

import java.util.List;

public class AddProductsOptionsByCategoryAdapter extends RecyclerView.Adapter<AddProductsOptionsByCategoryAdapter.CardHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    private List<AddProductCategoryModel> addproductcategoryList;
    public boolean showShimmerCategory = true;
    private int selectedPosition = 0;

    View layoutEditProduct;
    String adminID, productId, mainCategoryID;
    DatabaseReference databaseReference;
    RecyclerView categoryRecycler, optionsRecycler;
    RelativeLayout saveBtn;
    public AddProductsOptionsByCategoryAdapter(Context context, List<AddProductCategoryModel> addproductcategoryList,
                                               RecyclerView categoryRecycler, DatabaseReference databaseReference,
                                               RelativeLayout saveBtn, String productId, String adminID,
                                               String mainCategoryID, RecyclerView optionsRecycler)
    {
        this.context = context;
        this.adminID = adminID;
        this.databaseReference = databaseReference;
        this.addproductcategoryList = addproductcategoryList;
        this.categoryRecycler = categoryRecycler;
        this.saveBtn = saveBtn;
        this.productId = productId;
        this.mainCategoryID = mainCategoryID;
        this.optionsRecycler = optionsRecycler;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_category_card, parent, false);
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
            holder.categoryName.setBackground(ContextCompat.getDrawable(context, R.drawable.checkbox_background));
            holder.categoryName.setChecked(position == selectedPosition);

            initTask(addproductcategoryList.get(selectedPosition).getCategoryId(), holder);

            holder.categoryName.setOnClickListener(v -> {
                // Update the selected position and notify the adapter
                selectedPosition = position;
                initTask(addproductcategoryList.get(selectedPosition).getCategoryId(), holder);
                notifyDataSetChanged();
            });



        }
    }

    private void initTask(String categoryId, AddProductsOptionsByCategoryAdapter.CardHolder holder)
    {
        new AsyncTaskAddOptionsProductsByProduct(context, categoryId, optionsRecycler,
                firebaseFirestore, layoutEditProduct, databaseReference,
                adminID, mainCategoryID, saveBtn, productId).execute();
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