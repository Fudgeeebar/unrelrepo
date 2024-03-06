//package com.octa.phlposcasu.Adapters.Products;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.os.Build;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.facebook.shimmer.ShimmerFrameLayout;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.octa.phlposcasu.AsyncTask.AsyncTaskOptionsParent;
//import com.octa.phlposcasu.Models.ProductModel;
//import com.octa.phlposcasu.R;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
//public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.CardHolder> {
//
//    Context context;
//    private List<ProductModel> productList;
//    public boolean showShimmerProduct = true;
//    DecimalFormat formater = new DecimalFormat("0.00");
//    Drawable mDefaultBackground;
//    ImageView productImage;
//    TextView productName;
//    View productView;
//    TextView productPrice;
//    RecyclerView optionsParentRecycler;
//    String categoryId;
//    FirebaseFirestore firebaseFirestore;
//
//    RelativeLayout addToCartBtn;
//
//    View cartView;
//    String key;
//
//    public ProductAdapter(Context context, List<ProductModel> productList, String categoryId, TextView productName, ImageView productImage,
//                          View productView, TextView productPrice, RecyclerView optionsParentRecycler, FirebaseFirestore firebaseFirestore,
//                          RelativeLayout addToCartBtn, View cartView, String key)
//    {
//        this.context = context;
//        this.productList = productList;
//        this.productImage = productImage;
//        this.productName = productName;
//        this.productView = productView;
//        this.productPrice = productPrice;
//        this.optionsParentRecycler = optionsParentRecycler;
//        this.categoryId = categoryId;
//        this.firebaseFirestore = firebaseFirestore;
//        this.addToCartBtn = addToCartBtn;
//        this.cartView = cartView;
//        this.key = key;
//    }
//
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @NonNull
//    @Override
//    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_card, parent, false);
////        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);
//
//
//        return new CardHolder(view);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public void onBindViewHolder(@NonNull CardHolder holder, int position) {
//
//        if (showShimmerProduct) {
//            holder.shimmerFrameLayoutProduct.startShimmer();
//        }
//        else {
//            holder.shimmerFrameLayoutProduct.stopShimmer();
//            holder.shimmerFrameLayoutProduct.setShimmer(null);
//
//            ProductModel model = productList.get(position);
//
//            holder.productNameTxt.setText(model.getProductName());
//            holder.productNameTxt.setBackground(null);
//            holder.productPriceTxt.setText("₱ "+model.getProductPrice());
//            holder.productPriceTxt.setBackground(null);
//
//            Glide.with(context).load(model.getProductImage())
////                    .error(mDefaultBackground)
//                    .into(holder.productImage);
//
////            Toast.makeText(context, model.getProductName(), Toast.LENGTH_SHORT).show();
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    productView.setVisibility(View.VISIBLE);
//                    cartView.setVisibility(View.GONE);
//
//                    Glide.with(context).load(model.getProductImage())
////                    .error(mDefaultBackground)
//                            .into(productImage);
//                    productName.setText(model.getProductName());
//                    productPrice.setText(String.valueOf(model.getProductPrice()));
//
//
//                    new AsyncTaskOptionsParent(context, categoryId, model, firebaseFirestore, productView, optionsParentRecycler, addToCartBtn, cartView, key, adm).execute();
//                }
//            });
//        }
//    }
//
//
//    @Override
//    public int getItemCount() {
//        int SHIMMER_ITEM_NUMBER = 10;
//        return showShimmerProduct ? SHIMMER_ITEM_NUMBER : productList.size();
//
////        return productList == null ? 0 : Integer.MAX_VALUE;
//    }
//
//
//    public class CardHolder extends RecyclerView.ViewHolder {
//        TextView productNameTxt, productPriceTxt;
//        ImageView productImage;
//        ShimmerFrameLayout shimmerFrameLayoutProduct;
//
//
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        public CardHolder(@NonNull View itemView) {
//            super(itemView);
//
//            shimmerFrameLayoutProduct = itemView.findViewById(R.id.shimmerLayoutProduct);
//            productNameTxt = itemView.findViewById(R.id.productName_txt);
//            productPriceTxt = itemView.findViewById(R.id.productPrice_txt);
//            productImage = itemView.findViewById(R.id.productImage);
//
//        }
//    }
//}