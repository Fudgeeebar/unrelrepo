package com.octa.phlposcasu.Options.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octa.phlposcasu.Models.AddCategory.AddProductCategoryModel;
import com.octa.phlposcasu.Options.Models.OptionsProductsByCategoryModel;
import com.octa.phlposcasu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsProductsByCategoryAdapter extends RecyclerView.Adapter<OptionsProductsByCategoryAdapter.CardHolder> {

    Context context;
    private List<OptionsProductsByCategoryModel> optionsProductsByCategoryList;
    public boolean showShimmerCategory = true;


    public OptionsProductsByCategoryAdapter(Context context,
                                            List<OptionsProductsByCategoryModel> optionsProductsByCategoryList)
    {
        this.context = context;
        this.optionsProductsByCategoryList = optionsProductsByCategoryList;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_options_products_by_category_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);
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

            OptionsProductsByCategoryModel model = optionsProductsByCategoryList.get(position);

            holder.categoryName.setText(model.getOptionName());
            holder.categoryName.setBackground(null);


            
        }
    }




    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerCategory ? SHIMMER_ITEM_NUMBER : optionsProductsByCategoryList.size();

//        return categoryList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        ShimmerFrameLayout shimmerFrameLayout;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmerLayoutProduct);
            categoryName = itemView.findViewById(R.id.category_txt);

        }
    }
}