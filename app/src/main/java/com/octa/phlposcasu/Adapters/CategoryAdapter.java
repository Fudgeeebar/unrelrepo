package com.octa.phlposcasu.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.octa.phlposcasu.AsyncTask.AsyncTaskProducts;
import com.octa.phlposcasu.MainActivity;
import com.octa.phlposcasu.Models.CategoryModel;
import com.octa.phlposcasu.Models.ProductModel;
import com.octa.phlposcasu.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CardHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    private List<CategoryModel> categoryList;

    List<ProductModel> productList;
    ProductAdapter productAdapter;

    public boolean showShimmerCategory = true;
    private int selectedPosition = 0;
    public RecyclerView productRecycler;
    Drawable mDefaultBackground;
    DatabaseReference databaseReference;
    ImageView productImage;
    TextView productName;
    View productView;
    TextView productPrice;
    RecyclerView optionsParentRecycler;
    RelativeLayout addToCartBtn;

    View cartView;
    String key, adminID;



    public CategoryAdapter(Context context, List<CategoryModel> categoryList, RecyclerView productRecycler, FirebaseFirestore firebaseFirestore,
                           TextView productName, ImageView productImage, View productView, TextView productPrice, RecyclerView optionsParentRecycler,
                           RelativeLayout addToCartBtn, View cartView, String key, DatabaseReference databaseReference, String adminID)
    {
        this.context = context;
        this.categoryList = categoryList;
        this.firebaseFirestore = firebaseFirestore;
        this.productRecycler = productRecycler;
        this.productName = productName;
        this.productImage = productImage;
        this.productView = productView;
        this.productPrice = productPrice;
        this.optionsParentRecycler = optionsParentRecycler;
        this.addToCartBtn = addToCartBtn;
        this.cartView = cartView;
        this.key = key;
        this.databaseReference = databaseReference;
        this.adminID = adminID;
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

            CategoryModel model = categoryList.get(position);

            holder.categoryName.setText(model.getCategoryId());
            holder.categoryName.setBackground(ContextCompat.getDrawable(context, R.drawable.checkbox_background));
            holder.categoryName.setChecked(position == selectedPosition);

            initTask(categoryList.get(selectedPosition).getCategoryId(), holder);

            holder.categoryName.setOnClickListener(v -> {
                // Update the selected position and notify the adapter
                selectedPosition = position;
                initTask(categoryList.get(selectedPosition).getCategoryId(), holder);
                notifyDataSetChanged();
            });



        }
    }

    private void initTask(String categoryId, CardHolder holder)
    {
        new AsyncTaskProducts(context, categoryId, productRecycler, productList, productAdapter,
                firebaseFirestore, productImage, productName, productView, productPrice, optionsParentRecycler,
                addToCartBtn, cartView, key, databaseReference, adminID).execute();
    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerCategory ? SHIMMER_ITEM_NUMBER : categoryList.size();

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