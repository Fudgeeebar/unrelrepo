package com.octa.phlposcasu.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octa.phlposcasu.Models.ChildCartModel;
import com.octa.phlposcasu.R;

import java.text.DecimalFormat;
import java.util.List;

public class ChildCartAdapter extends RecyclerView.Adapter<ChildCartAdapter.CardHolder> {

    Context context;
    private List<ChildCartModel> cartChildList;
    public boolean showShimmerCartChild = true;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DatabaseReference databaseReference;
    String key;
    TextView orderCartTotal, orderCartSubTotal;
    boolean preferences;
    ConstraintLayout paymentBtn;
    TextView paymentPreference;
    public ChildCartAdapter(Context context, List<ChildCartModel> cartChildList, String key,
                            TextView orderCartTotal, TextView orderCartSubTotal, boolean preferences, TextView paymentPreference, ConstraintLayout paymentBtn)
    {
        this.context = context;
        this.cartChildList = cartChildList;
        this.key = key;
        this.orderCartSubTotal = orderCartSubTotal;
        this.orderCartTotal = orderCartTotal;
        this.preferences = preferences;
        this.paymentBtn = paymentBtn;
        this.paymentPreference = paymentPreference;
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_child_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);


        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerCartChild) {
            holder.shimmerFrameLayoutChildCart.startShimmer();
        }
        else {
            holder.shimmerFrameLayoutChildCart.stopShimmer();
            holder.shimmerFrameLayoutChildCart.setShimmer(null);

            ChildCartModel model = cartChildList.get(position);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            holder.optionName.setText(model.getOptionName());
            holder.optionName.setBackground(null);

        }
    }

    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 2;
        return showShimmerCartChild ? SHIMMER_ITEM_NUMBER : cartChildList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView optionName;
        ShimmerFrameLayout shimmerFrameLayoutChildCart;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutChildCart = itemView.findViewById(R.id.shimmerLayoutChildCart);
            optionName = itemView.findViewById(R.id.option_txt);

        }
    }

}