package com.octa.phlposcasu.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octa.phlposcasu.Models.OptionsChildModel;
import com.octa.phlposcasu.Models.OptionsParentModel;
import com.octa.phlposcasu.Models.ProductModel;
import com.octa.phlposcasu.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class OptionsChildAdapter extends RecyclerView.Adapter<OptionsChildAdapter.CardHolder> {

    Context context;
    private List<OptionsChildModel> optionsChildList;
    public boolean showShimmerOptionsChild = true;
    private int selectionCount = 0;
    private int mainQuantity = 1;
    ProductModel productModel;
    OptionsParentModel optionsParentModel;
    private double optionsTotal = 0.00;
    RelativeLayout addToCartBtn;
    List<OptionsParentModel> optionsParentList;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    View productView;
    View cartView;
    Map<String, Object> checkedValues;
    Map<String, Object> singleton = new HashMap<>();

    DatabaseReference databaseReference;
    OptionsParentAdapter.CardHolder optionsParentHolder;
    String keyMain, adminID;

    public OptionsChildAdapter(Context context, List<OptionsChildModel> optionsChildList, ProductModel productModel, OptionsParentModel optionsParentModel,
                               RelativeLayout addToCartBtn, List<OptionsParentModel> optionsParentList,
                               View productView, View cartView,
                               OptionsParentAdapter.CardHolder optionsParentHolder, String key, String adminID)
    {
        this.context = context;
        this.optionsChildList = optionsChildList;
        this.productModel = productModel;
        this.optionsParentModel = optionsParentModel;
        this.addToCartBtn = addToCartBtn;
        this.optionsParentList = optionsParentList;
        this.productView = productView;
        this.cartView = cartView;
        this.keyMain = key;
        this.adminID = adminID;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_addons_child_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerOptionsChild) {
            holder.shimmerFrameLayoutOptionsChild.startShimmer();
        }
        else {
            holder.shimmerFrameLayoutOptionsChild.stopShimmer();
            holder.shimmerFrameLayoutOptionsChild.setShimmer(null);

            OptionsChildModel model = optionsChildList.get(position);

            holder.optionTitle.setText("  "+model.getOptionName());
            holder.optionTitle.setBackground(null);
            holder.optionPrice.setText("+"+model.getOptionPrice());
            holder.optionPrice.setBackground(null);

            holder.optionTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = holder.optionTitle.isChecked();

                    if (isChecked && selectionCount >= optionsParentModel.getMaxSelection()) {
                        holder.optionTitle.setChecked(false);

                    }
                    else
                    {

                        // Check if selectedChildOptions is null and initialize it if necessary
                        if (optionsParentModel.getSelectedChildOptions() == null) {
                            optionsParentModel.setSelectedChildOptions(new ArrayList<>());
                        }

                        model.setChecked(isChecked);
                        if (isChecked) {
                            selectionCount++;
                            optionsParentModel.getSelectedChildOptions().add(model); // Add the selected child option to the list
//                            initQuantity(mainQuantity, productModel, optionsTotal, model);
                        } else {
                            selectionCount--;
                            optionsParentModel.getSelectedChildOptions().remove(model); // Remove the unselected child option from the list
//                            initQuantity(mainQuantity, productModel, optionsTotal, model);
                        }
                    }

                    // Calculate the sum of checked optionPrices
                    optionsTotal = calculateSumOfCheckedOptionPrices(); // Call the method to calculate the sum

                    // Use the sum as needed
//                    Toast.makeText(v.getContext(), "Sum of checked optionPrices: " + optionsTotal, Toast.LENGTH_SHORT).show();

                    initQuantity(productModel, optionsTotal, model);
                }
            });



            initQuantity(productModel, optionsTotal, model);

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    new AsyncTaskOptionsParent(context, categoryId, productList, model, firebaseFirestore, productView, optionsParentRecycler).execute();
//
//                }
//            });
        }
    }

    private double calculateSumOfCheckedOptionPrices() {
        double sum = 0.00;
        checkedValues = new HashMap<>();
        // Iterate through the selected options
        for (OptionsParentModel parentModel : optionsParentList) {
            for (OptionsChildModel childModel : parentModel.getSelectedChildOptions()) {
                checkedValues.put(childModel.getProductId(), childModel);
                sum += childModel.getOptionPrice();
            }
        }

        return sum;
    }



    private void initQuantity(ProductModel productModel, double optionsTotal, OptionsChildModel optionsChildModel) {


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (context != null)
                {
                    final AlertDialog builderAddress = new AlertDialog.Builder(context, R.style.AlertDialogTheme).create();
                    View viewAddress = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_quantity, null);
                    TextView actualPriceTxt = viewAddress.findViewById(R.id.actual_price_txt);
                    TextView quantityTxt = viewAddress.findViewById(R.id.quantity_indicator);
                    ImageButton addQtyBtn = viewAddress.findViewById(R.id.imageButton);
                    ImageButton reduceQtyBtn = viewAddress.findViewById(R.id.imageButton2);
                    RelativeLayout addToCartBtnConfirm = viewAddress.findViewById(R.id.add_to_cart_btn);

                    double actPrice = (productModel.getProductPrice()+optionsTotal)*mainQuantity;

                    initAddToCart(mainQuantity, addToCartBtnConfirm, actualPriceTxt, actPrice, quantityTxt, builderAddress);

                    addQtyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mainQuantity >= productModel.getProductQuantity())
                            {

                            }
                            else
                            {
                                mainQuantity = mainQuantity + 1;
                                double actPrice = (productModel.getProductPrice()+optionsTotal)*mainQuantity;
                                initAddToCart(mainQuantity, addToCartBtnConfirm, actualPriceTxt, actPrice, quantityTxt, builderAddress);
                            }
                        }
                    });

                    reduceQtyBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mainQuantity <= 1)
                            {

                            }
                            else
                            {
                                mainQuantity = mainQuantity - 1;
                                double actPrice = (productModel.getProductPrice()+optionsTotal)*mainQuantity;
                                initAddToCart(mainQuantity, addToCartBtnConfirm, actualPriceTxt, actPrice, quantityTxt, builderAddress);
                            }
                        }
                    });

                    builderAddress.setView(viewAddress);
                    if (builderAddress.getWindow() != null) {
                        builderAddress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    }
                    builderAddress.setCancelable(true);
                    builderAddress.show();



                }


            }
        });
    }

    private void initAddToCart(int mainQuantity, RelativeLayout addToCartBtnConfirm, TextView actualPriceTxt, double actPrice, TextView quantityTxt, AlertDialog builderAddress)
    {

        actualPriceTxt.setText("₱"+decimalFormat.format(actPrice));



        quantityTxt.setText(mainQuantity+"");
        addToCartBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderAddress.dismiss();
//                View viewWalletActivation = builderAddress.getLayoutInflater().inflate(R.layout.dialog_fullscreen_loading, null);
//                Dialog dialogWalletActivation = new Dialog(builderAddress.getContext(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
//                dialogWalletActivation.getWindow().getAttributes();
//                dialogWalletActivation.setContentView(viewWalletActivation);
//                dialogWalletActivation.setCancelable(false);
//                dialogWalletActivation.show();

                String key = databaseReference.push().getKey();

//                new AsyncTaskParentCart(context, optionsChildModel, productModel, productView, cartView, mainQuantity, actPrice);

                singleton.put("productCategory", productModel.getProductCategory());
                singleton.put("productId", productModel.getProductId());
                singleton.put("productImage", productModel.getProductImage());
                singleton.put("productName", productModel.getProductName());
                singleton.put("productPrice", actPrice);
                singleton.put("productQuantity", mainQuantity);
                singleton.put("productOrderId", key);
                databaseReference.child("Cart")
                        .child(adminID)
                        .child(keyMain).child(key)
                        .updateChildren(singleton)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                databaseReference.child("Cart")
                                        .child(adminID)
                                        .child(keyMain).child(key)
                                        .child("Options").setValue(checkedValues)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                cartView.setVisibility(View.VISIBLE);
                                                productView.setVisibility(View.GONE);

//                                                Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//                                                        .subscribe(() -> {
//                                                            dialogWalletActivation.dismiss();
//                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to save values
//                                                        Toast.makeText(context.getApplicationContext(), "Failed to save checked items.", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });


    }



    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 2;
        return showShimmerOptionsChild ? SHIMMER_ITEM_NUMBER : optionsChildList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView optionPrice;
        CheckBox optionTitle;
        ShimmerFrameLayout shimmerFrameLayoutOptionsChild;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutOptionsChild = itemView.findViewById(R.id.shimmerLayoutOptionsChild);
            optionTitle = itemView.findViewById(R.id.check_box);
            optionPrice = itemView.findViewById(R.id.textView);

        }
    }

}