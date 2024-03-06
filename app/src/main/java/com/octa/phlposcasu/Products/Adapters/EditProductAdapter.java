package com.octa.phlposcasu.Products.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.octa.phlposcasu.AsyncTask.AddCategory.AsyncTaskAddProductCategory;
import com.octa.phlposcasu.AsyncTask.AsyncTaskOptionsParent;
import com.octa.phlposcasu.Models.ProductModel;
import com.octa.phlposcasu.Options.OptionsActivity;
import com.octa.phlposcasu.Products.AsyncTask.AsyncTaskAddProductsOptionsByCategory;
import com.octa.phlposcasu.Products.ProductActivity;
import com.octa.phlposcasu.R;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditProductAdapter extends RecyclerView.Adapter<EditProductAdapter.CardHolder> {

    Context context;
    private List<ProductModel> productList;
    public boolean showShimmerProduct = true;
    DecimalFormat formater = new DecimalFormat("0.00");
    Drawable mDefaultBackground;
    FirebaseFirestore firebaseFirestore;

    RelativeLayout addToCartBtn;
    
    TextView productNameTxt, productPriceTxt, editBtn, addProductOptionsBtn;
    ImageView productImage;

    DatabaseReference databaseReference;
    boolean required = false;
    String adminID;
    
    public EditProductAdapter(Context context, List<ProductModel> productList, TextView productNameTxt,
                              TextView productPriceTxt, TextView editBtn, TextView addProductOptionsBtn,
                              ImageView productImage, DatabaseReference databaseReference, String adminID)
    {
        this.context = context;
        this.productList = productList;
        this.productImage = productImage;
        this.productNameTxt = productNameTxt;
        this.productPriceTxt = productPriceTxt;
        this.editBtn = editBtn;
        this.addProductOptionsBtn = addProductOptionsBtn;
        this.databaseReference = databaseReference;
        this.adminID = adminID;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_card, parent, false);
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

            ProductModel model = productList.get(position);

            holder.productNameTxt.setText(model.getProductName());
            holder.productNameTxt.setBackground(null);
            holder.productPriceTxt.setText("₱ "+model.getProductPrice());
            holder.productPriceTxt.setBackground(null);

            Glide.with(context).load(model.getProductImage())
//                    .error(mDefaultBackground)
                    .into(holder.productImage);

//            Toast.makeText(context, model.getProductName(), Toast.LENGTH_SHORT).show();

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                    Glide.with(context).load(model.getProductImage())
//                    .error(mDefaultBackground)
                            .into(productImage);
                    productNameTxt.setText(model.getProductName());
                    productPriceTxt.setText(String.valueOf(model.getProductPrice()));

                    addProductOptionsBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (context != null)
                            {
                                final AlertDialog alertAddCategory = new AlertDialog.Builder(context, R.style.AlertDialogTheme).create();
                                View viewAddress = alertAddCategory.getLayoutInflater().inflate(R.layout.dialog_add_option_product_title, null);
                                TextInputEditText categoryNameEdt = viewAddress.findViewById(R.id.category_name_edt);
                                TextInputEditText maxSelectionEdt = viewAddress.findViewById(R.id.max_selection_edt);

                                RelativeLayout addCategoryBtn = viewAddress.findViewById(R.id.add_category_btn);
                                CheckBox yesCheck = viewAddress.findViewById(R.id.yes_check);
                                CheckBox noCheck = viewAddress.findViewById(R.id.no_check);

                                yesCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                    {
                                        if (isChecked == true)
                                        {
                                            noCheck.setChecked(false);
                                            required = true;
                                        }
                                        else
                                        {
                                            noCheck.setChecked(true);
                                            required = false;
                                        }
                                    }
                                });

                                noCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
                                    {
                                        if (noCheck.isChecked())
                                        {
                                            yesCheck.setChecked(false);
                                            required = false;
                                        }
                                        else
                                        {
                                            yesCheck.setChecked(true);
                                            required = true;
                                        }
                                    }
                                });

                                addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String mainCategoryID = databaseReference.push().getKey();

                                        Map<String, Object> singleton = new HashMap<>();
                                        singleton.put("maxSelection", Integer.parseInt(maxSelectionEdt.getText().toString()));
                                        singleton.put("optionId", mainCategoryID);
                                        singleton.put("optionTitle", categoryNameEdt.getText().toString());
                                        singleton.put("required", required);
                                        databaseReference.child("OptionsByProduct").child("MainOptions")
                                                .child(model.getProductId())
                                                .child(mainCategoryID)
                                                .updateChildren(singleton)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        alertAddCategory.dismiss();
                                                        if (alertAddCategory != null)
                                                        {
//                                                            String productId = databaseReference.push().getKey();

                                                            final AlertDialog alertAddProductCategory = new AlertDialog.Builder(alertAddCategory.getContext(), R.style.AlertDialogTheme).create();
                                                            View viewAddress = alertAddProductCategory.getLayoutInflater().inflate(R.layout.dialog_add_options_product_by_product_selection, null);
                                                            RecyclerView categoryRecycler = viewAddress.findViewById(R.id.category_recycler);
                                                            RecyclerView optionsRecycler = viewAddress.findViewById(R.id.category_items_recycler);
                                                            RelativeLayout saveBtn = viewAddress.findViewById(R.id.save_btn);


                                                            new AsyncTaskAddProductsOptionsByCategory(alertAddProductCategory.getContext(), categoryRecycler,
                                                                    databaseReference, saveBtn, model.getProductId(), mainCategoryID, adminID, optionsRecycler).execute();


//            saveBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    List<String> checkedCategories = new ArrayList<>();
//
//                    for (int i = 0; i < categoryRecycler.getChildCount(); i++) {
//                        View view1 = categoryRecycler.getChildAt(i);
//                        CheckBox checkBox = view1.findViewById(R.id.categoryName_txt); // Replace R.id.checkBox with the ID of your checkbox view
//
//                        if (checkBox.isChecked()) {
//                            String category = checkedCategories.get(i);
//                            checkedCategories.add(category);
//                        }
//
//                    }
//
//                    for (String category : checkedCategories) {
//                        DatabaseReference productRef = databaseReference.child(category).push();
//
//                        productRef.child("productCategory").setValue(category);
//                        productRef.child("productId").setValue(productRef.getKey());
//                        productRef.child("productName").setValue(productNameEdt.getText().toString());
//                    }
//                    // Clear the editText and uncheck the checkboxes after saving the data
//                    for (int i = 0; i < categoryRecycler.getChildCount(); i++) {
//                        View itemView = categoryRecycler.getChildAt(i);
//                        CheckBox checkBox = itemView.findViewById(R.id.categoryName_txt);
//                        checkBox.setChecked(false);
//                    }
//
//
//                }
//            });


                                                            alertAddProductCategory.setView(viewAddress);
                                                            if (alertAddProductCategory.getWindow() != null) {
                                                                alertAddProductCategory.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                            }
                                                            alertAddProductCategory.setCancelable(true);
                                                            alertAddProductCategory.show();



                                                        }
                                                    }
                                                });
                                    }
                                });


                                View decorView = alertAddCategory.getWindow().getDecorView();
                                int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                                decorView.setSystemUiVisibility(flags);
                                alertAddCategory.setView(viewAddress);
                                if (alertAddCategory.getWindow() != null) {
                                    alertAddCategory.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                }
                                alertAddCategory.setCancelable(true);
                                alertAddCategory.show();
                            }
                        }
                    });

//                    new AsyncTaskOptionsParent(context, categoryId, model, firebaseFirestore, productView, optionsParentRecycler, addToCartBtn, cartView, key).execute();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerProduct ? SHIMMER_ITEM_NUMBER : productList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView productNameTxt, productPriceTxt;
        ImageView productImage;
        ShimmerFrameLayout shimmerFrameLayoutProduct;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutProduct = itemView.findViewById(R.id.shimmerLayoutProduct);
            productNameTxt = itemView.findViewById(R.id.productName_txt);
            productPriceTxt = itemView.findViewById(R.id.productPrice_txt);
            productImage = itemView.findViewById(R.id.productImage);

        }
    }
}