package com.octa.phlposcasu.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.octa.phlposcasu.AsyncTask.AsyncTaskOptionsChild;
import com.octa.phlposcasu.Models.OptionsParentModel;
import com.octa.phlposcasu.Models.ProductModel;
import com.octa.phlposcasu.R;

import java.util.List;

public class OptionsParentAdapter extends RecyclerView.Adapter<OptionsParentAdapter.CardHolder> {

    Context context;
    private List<OptionsParentModel> optionsParentList;
    ProductModel productModel;
    public boolean showShimmerOptionsParent = true;

    RelativeLayout addToCartBtn;
    View cartView;
    View productView;
    String key, adminID;

    DatabaseReference databaseReference;



    public OptionsParentAdapter(Context context, List<OptionsParentModel> optionsParentList, ProductModel productModel, RelativeLayout addToCartBtn,
                                View cartView, View productView, String key, String adminID, DatabaseReference databaseReference)
    {
        this.context = context;
        this.optionsParentList = optionsParentList;
        this.productModel = productModel;
        this.addToCartBtn = addToCartBtn;
        this.cartView = cartView;
        this.productView = productView;
        this.key = key;
        this.adminID = adminID;
        this.databaseReference = databaseReference;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_addons_parent_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);


        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerOptionsParent) {
            holder.shimmerFrameLayoutOptionsParent.startShimmer();
        }
        else {
            holder.shimmerFrameLayoutOptionsParent.stopShimmer();
            holder.shimmerFrameLayoutOptionsParent.setShimmer(null);

            OptionsParentModel model = optionsParentList.get(position);

            holder.optionTitle.setText(model.getOptionTitle());
            holder.optionTitle.setBackground(null);

            if (model.isRequired() == true)
            {
                holder.optionQuantity.setText("("+model.getMaxSelection()+" Required"+")");
            }
            else
            {
                holder.optionQuantity.setText("(Optional)");
            }
            holder.optionQuantity.setBackground(null);

            new AsyncTaskOptionsChild(context, model, holder.optionChildRecycler, productModel, addToCartBtn,
                    optionsParentList, productView, cartView, holder, key, adminID, databaseReference).execute();

        }
    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 5;
        return showShimmerOptionsParent ? SHIMMER_ITEM_NUMBER : optionsParentList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView optionTitle, optionQuantity;
        RecyclerView optionChildRecycler;
        ShimmerFrameLayout shimmerFrameLayoutOptionsParent;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutOptionsParent = itemView.findViewById(R.id.shimmerLayoutOptionsParent);
            optionTitle = itemView.findViewById(R.id.option_title_txt);
            optionQuantity = itemView.findViewById(R.id.option_quantity_txt);
            optionChildRecycler = itemView.findViewById(R.id.option_child_recycler);

        }
    }
}