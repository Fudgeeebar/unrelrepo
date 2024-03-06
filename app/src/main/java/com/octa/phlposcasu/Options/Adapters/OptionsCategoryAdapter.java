package com.octa.phlposcasu.Options.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octa.phlposcasu.Models.CategoryModel;
import com.octa.phlposcasu.Options.AsyncTask.AsyncTaskOptionsProductsByCategory;
import com.octa.phlposcasu.R;

import java.util.List;

public class OptionsCategoryAdapter extends RecyclerView.Adapter<OptionsCategoryAdapter.CardHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    private List<CategoryModel> categoryList;
    public boolean showShimmerCategory = true;
    private int selectedPosition = 0;
    RecyclerView optionsCategoryRecycler, optionsItemRecycler;

    String adminID;

    DatabaseReference databaseReference;



    public OptionsCategoryAdapter(Context context, List<CategoryModel> categoryList,
                                  RecyclerView optionsCategoryRecycler, String adminID,
                                  DatabaseReference databaseReference, RecyclerView optionsItemRecycler)
    {
        this.context = context;
        this.categoryList = categoryList;
        this.optionsCategoryRecycler = optionsCategoryRecycler;
        this.adminID = adminID;
        this.databaseReference = databaseReference;
        this.optionsItemRecycler = optionsItemRecycler;
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

    private void initTask(String categoryId, OptionsCategoryAdapter.CardHolder holder)
    {
        new AsyncTaskOptionsProductsByCategory(context, categoryId, optionsItemRecycler,
                firebaseFirestore, databaseReference).execute();
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