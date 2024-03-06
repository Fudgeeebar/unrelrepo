package com.octa.phlposcasu.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octa.phlposcasu.AsyncTask.AsyncTaskChildCart;
import com.octa.phlposcasu.Models.ParentCartModel;
import com.octa.phlposcasu.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ParentCartAdapter extends RecyclerView.Adapter<ParentCartAdapter.CardHolder> {

    Context context;
    private List<ParentCartModel> parentCartList;
    public boolean showShimmerCartParent = true;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    DatabaseReference databaseReference;
    TextView orderCartSubTotal;
    TextView orderCartTotal;
    RelativeLayout checkOutBtn;
    String key;
    boolean preferences;
    TextView paymentPreference;
    ConstraintLayout paymentBtn;

    String paymentMethod = "Cash";
    String diningPreference = "Dine In";


    public ParentCartAdapter(Context context, List<ParentCartModel> parentCartList, TextView orderCartSubTotal,
                             TextView orderCartTotal, RelativeLayout checkOutBtn, String key, boolean preferences,
                             TextView paymentPreference, ConstraintLayout paymentBtn)
    {
        this.context = context;
        this.parentCartList = parentCartList;
        this.orderCartSubTotal = orderCartSubTotal;
        this.orderCartTotal = orderCartTotal;
        this.checkOutBtn = checkOutBtn;
        this.key = key;
        this.preferences = preferences;
        this.paymentPreference = paymentPreference;
        this.paymentBtn = paymentBtn;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart_parent_card, parent, false);
//        mDefaultBackground = context.getResources().getDrawable(R.drawable.tu_lg);


        return new CardHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

        if (showShimmerCartParent) {
            holder.shimmerFrameLayoutParentCart.startShimmer();
        }
        else {
            holder.shimmerFrameLayoutParentCart.stopShimmer();
            holder.shimmerFrameLayoutParentCart.setShimmer(null);

            ParentCartModel model = parentCartList.get(position);

            databaseReference = FirebaseDatabase.getInstance().getReference();

            holder.productName.setText(model.getProductName());
            holder.productName.setBackground(null);
            holder.productPrice.setText(decimalFormat.format(model.getProductPrice())+"");
            holder.productPrice.setBackground(null);
            holder.productQuantity.setText(model.getProductQuantity()+"");
            holder.editBtn.setText("Edit");
            holder.editBtn.setBackground(null);

            new AsyncTaskChildCart(context, holder.optionsCartRecycler, databaseReference, model.getProductOrderId(),
                    key, orderCartTotal, orderCartSubTotal, preferences, paymentBtn, paymentPreference).execute();



            paymentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (context != null)
                    {
                        final AlertDialog builderAddress = new AlertDialog.Builder(context, R.style.AlertDialogTheme).create();
                        View viewAddress = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_preferences, null);
                        CheckBox cashCheck = viewAddress.findViewById(R.id.cash_check);
                        CheckBox eWalletCheck = viewAddress.findViewById(R.id.wallet_check);

                        CheckBox gCashCheck = viewAddress.findViewById(R.id.gcash_check);
                        CheckBox mayaCheck = viewAddress.findViewById(R.id.maya_check);
                        CheckBox grabPayCheck = viewAddress.findViewById(R.id.grab_check);
                        CheckBox shopeePayCheck = viewAddress.findViewById(R.id.shopee_check);

                        CheckBox dineInCheck = viewAddress.findViewById(R.id.dine_in_check);
                        CheckBox takeOutCheck = viewAddress.findViewById(R.id.take_out_check);
                        CheckBox deliveryCheck = viewAddress.findViewById(R.id.delivery_check);

                        FrameLayout AdLayout = viewAddress.findViewById(R.id.nativeAdLayout);

                        RelativeLayout confirmBtn = viewAddress.findViewById(R.id.add_to_cart_btn);

//                        RadioGroup radioGroup = viewAddress.findViewById(R.id.radio_group);

//                        initAdView(AdLayout);

                        initCheckBoxes(cashCheck, eWalletCheck, mayaCheck, grabPayCheck, shopeePayCheck);

                        initCheckBoxesForEwallet(mayaCheck, grabPayCheck, shopeePayCheck);

                        initCheckBoxesForDining(dineInCheck, takeOutCheck);


                        confirmBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (paymentMethod.equals("E-Wallet"))
                                {
                                    Toast.makeText(context, "Please select 1 Payment Channel", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    paymentPreference.setText(paymentMethod+" & "+diningPreference);
                                    builderAddress.dismiss();
                                }
                            }
                        });


                        initPayment();

                        View decorView = builderAddress.getWindow().getDecorView();
                        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN;
                        decorView.setSystemUiVisibility(flags);

                        builderAddress.setView(viewAddress);
                        if (builderAddress.getWindow() != null) {
                            builderAddress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        }
                        builderAddress.setCancelable(true);
                        builderAddress.show();



                    }
                }
            });

            initPayment();

        }
    }

    private void initCheckBoxesForDining(CheckBox dineInCheck, CheckBox takeOutCheck)
    {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(dineInCheck);
        checkBoxes.add(takeOutCheck);


        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CheckBox cb : checkBoxes) {
                        // Uncheck all CheckBoxes except the one that was clicked
                        if (cb != checkBox) {
                            cb.setChecked(false);
                        }
                    }
                    // Set the clicked CheckBox as checked
                    checkBox.setChecked(true);

                    // Get the value of the checked CheckBox
                    if (checkBox.isChecked()) {
                        if (checkBox == checkBoxes.get(0)) {
                            diningPreference = "Dine In";
                        } else if (checkBox == checkBoxes.get(1)) {
                            diningPreference = "Take Out";
                        }
                    }

                    // Use the selectedValue as needed (e.g., save to Firebase database)
                    Log.d("SelectedValue", paymentMethod);

                }
            });
        }
    }

    private void initCheckBoxesForEwallet(CheckBox mayaCheck, CheckBox grabPayCheck, CheckBox shopeePayCheck)
    {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(mayaCheck);
        checkBoxes.add(grabPayCheck);
        checkBoxes.add(shopeePayCheck);


        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CheckBox cb : checkBoxes) {
                        // Uncheck all CheckBoxes except the one that was clicked
                        if (cb != checkBox) {
                            cb.setChecked(false);
                        }
                    }
                    // Set the clicked CheckBox as checked
                    checkBox.setChecked(true);

                    // Get the value of the checked CheckBox
                    if (checkBox.isChecked()) {
                        if (checkBox == checkBoxes.get(0)) {
                            paymentMethod = "Maya";

                        } else if (checkBox == checkBoxes.get(1)) {
                            paymentMethod = "GrabPay";
                        }
                        else if (checkBox == checkBoxes.get(2)) {
                            paymentMethod = "ShopeePay";
                        }

                    }

                    // Use the selectedValue as needed (e.g., save to Firebase database)
                    Log.d("SelectedValue", paymentMethod);
                }
            });
        }
    }

    private void initCheckBoxes(CheckBox cashCheck, CheckBox eWalletCheck, CheckBox mayaCheck, CheckBox grabPayCheck, CheckBox shopeePayCheck)
    {
        ArrayList<CheckBox> checkBoxes = new ArrayList<>();
        checkBoxes.add(cashCheck);
        checkBoxes.add(eWalletCheck);


        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (CheckBox cb : checkBoxes) {
                        // Uncheck all CheckBoxes except the one that was clicked
                        if (cb != checkBox) {
                            cb.setChecked(false);
                        }
                    }
                    // Set the clicked CheckBox as checked
                    checkBox.setChecked(true);

                    // Get the value of the checked CheckBox
                    if (checkBox.isChecked()) {
                        if (checkBox == checkBoxes.get(0)) {
                            paymentMethod = "Cash";

                            mayaCheck.setEnabled(false);
                            grabPayCheck.setEnabled(false);
                            shopeePayCheck.setEnabled(false);

                            mayaCheck.setChecked(false);
                            grabPayCheck.setChecked(false);
                            shopeePayCheck.setChecked(false);

                        } else if (checkBox == checkBoxes.get(1)) {
                            paymentMethod = "E-Wallet";
                            mayaCheck.setEnabled(true);
                            grabPayCheck.setEnabled(true);
                            shopeePayCheck.setEnabled(true);
                        }
                    }

                    // Use the selectedValue as needed (e.g., save to Firebase database)
                    Log.d("SelectedValue", paymentMethod);

                }
            });
        }

//        cashCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
//            {
//                if (isChecked == true)
//                {
//                    eWalletCheck.setChecked(false);
//
//                    gCashCheck.setChecked(false);
//                    mayaCheck.setChecked(false);
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//
//                    gCashCheck.setEnabled(false);
//                    mayaCheck.setEnabled(false);
//                    grabPayCheck.setEnabled(false);
//                    shopeePayCheck.setEnabled(false);
//
//                    paymentMethod = "Cash";
//
////                    initPayment(gCashCheck, mayaCheck, grabPayCheck, shopeePayCheck, dineInCheck, takeOutCheck, deliveryCheck, confirmBtn, paymentMethod);
//                }
//                else
//                {
//                    eWalletCheck.setChecked(true);
//
//                    gCashCheck.setChecked(false);
//                    mayaCheck.setChecked(true);
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//
//                    gCashCheck.setEnabled(false);
//                    mayaCheck.setEnabled(true);
//                    grabPayCheck.setEnabled(true);
//                    shopeePayCheck.setEnabled(true);
//
//                    paymentMethod = "Maya";
//
////                    initPayment(gCashCheck, mayaCheck, grabPayCheck, shopeePayCheck, dineInCheck, takeOutCheck, deliveryCheck, confirmBtn, paymentMethod);
//                }
//            }
//        });
//
//        eWalletCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
//            {
//                if (isChecked == true)
//                {
//                    cashCheck.setChecked(false);
//
//                    gCashCheck.setChecked(false);
//                    mayaCheck.setChecked(true);
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//
//                    gCashCheck.setEnabled(false);
//                    mayaCheck.setEnabled(true);
//                    grabPayCheck.setEnabled(true);
//                    shopeePayCheck.setEnabled(true);
//
////                    initPayment(gCashCheck, mayaCheck, grabPayCheck, shopeePayCheck, dineInCheck, takeOutCheck, deliveryCheck, confirmBtn, paymentMethod);
//                }
//                else
//                {
//                    cashCheck.setChecked(true);
//
//                    gCashCheck.setChecked(false);
//                    mayaCheck.setChecked(false);
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//
//                    gCashCheck.setEnabled(false);
//                    mayaCheck.setEnabled(false);
//                    grabPayCheck.setEnabled(false);
//                    shopeePayCheck.setEnabled(false);
//
//                    paymentMethod = "Cash";
//
////                    initPayment(gCashCheck, mayaCheck, grabPayCheck, shopeePayCheck, dineInCheck, takeOutCheck, deliveryCheck, confirmBtn, paymentMethod);
//                }
//            }
//        });
//
//        mayaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
//            {
//                if (isChecked == true)
//                {
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//                }
//                else
//                {
//                    grabPayCheck.setChecked(true);
//                    shopeePayCheck.setChecked(false);
//                    mayaCheck.setChecked(false);
//                }
//            }
//        });
//
//        grabPayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
//            {
//                if (isChecked == true)
//                {
//                    mayaCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//                }
//                else
//                {
//                    shopeePayCheck.setChecked(true);
//                    grabPayCheck.setChecked(false);
//                    mayaCheck.setChecked(false);
//                }
//            }
//        });
//
//        shopeePayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked)
//            {
//                if (isChecked == true)
//                {
//                    mayaCheck.setChecked(false);
//                    grabPayCheck.setChecked(false);
//                }
//                else
//                {
//                    mayaCheck.setChecked(true);
//                    grabPayCheck.setChecked(false);
//                    shopeePayCheck.setChecked(false);
//                }
//            }
//        });
    }


    private void initPayment()
    {

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                View viewWalletActivation = ((Activity)context).getLayoutInflater().inflate(R.layout.dialog_fullscreen_loading, null);
                Dialog dialogWalletActivation = new Dialog(((Activity)context), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
                dialogWalletActivation.getWindow().getAttributes();
                dialogWalletActivation.setContentView(viewWalletActivation);
                dialogWalletActivation.setCancelable(false);
                dialogWalletActivation.show();

                databaseReference.child("Cart").child("DuNxkVpqRqOcZwzWlGubCLtIYQu1").child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Map<String, Object> singleton = new HashMap<>();
                            singleton.put("oderId", key);
                            singleton.put("paymentChannel", paymentMethod);
                            singleton.put("subTotal", Double.parseDouble(orderCartSubTotal.getText().toString()));
                            singleton.put("discount", 0.00);
                            singleton.put("finalTotal", Double.parseDouble(orderCartTotal.getText().toString()));
                            singleton.put("diningPreference", diningPreference);
                            singleton.put("status", "Ordered");
                            singleton.put("timeDate", "String.valueOf(RealTime.now())");
                            databaseReference.child("Purchase").child("DuNxkVpqRqOcZwzWlGubCLtIYQu1").child(key)
                                            .setValue(singleton)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            databaseReference.child("Purchase").child("DuNxkVpqRqOcZwzWlGubCLtIYQu1").child(key)
                                                                    .updateChildren((Map<String, Object>) snapshot.getValue())
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {

                                                                            String qrCode = "https://kashierasia.web.app/tracking.html?storeID=DuNxkVpqRqOcZwzWlGubCLtIYQu1&orderID="+key;

                                                                            Completable.timer(3, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                                                                                    .subscribe(() -> {
                                                                                        databaseReference.child("Cart").child("DuNxkVpqRqOcZwzWlGubCLtIYQu1").child(key)
                                                                                                .removeValue(new DatabaseReference.CompletionListener() {
                                                                                                    @Override
                                                                                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref)
                                                                                                    {
//                                                                                                        Completable.timer(2, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
//                                                                                                                .subscribe(() -> {
//                                                                                                                    Intent i = new Intent(context, QRCodeActivity.class);
//                                                                                                                    i.putExtra("qr",qrCode);
//                                                                                                                    context.startActivity(i);
////                                                                                                                    ((Activity)context).finishAndRemoveTask();
//                                                                                                                    dialogWalletActivation.dismiss();
//                                                                                                                });
                                                                                                    }
                                                                                                });
                                                                                    });

                                                                        }
                                                                    });
                                                        }
                                                    });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // Handle database read error
                            }
                        });
                Toast.makeText(context, paymentMethod+"\n"+diningPreference, Toast.LENGTH_SHORT).show();

            }
        });


    }


//    private void initAdView(FrameLayout AdLayout)
//    {
//        AdLoader.Builder builder = new AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110") // testAd
////        AdLoader.Builder builder = new AdLoader.Builder(this, "ca-app-pub-5604252494673138/1098432746") // liveAd
//                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//                    @Override
//                    public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
//                        NativeAdView nativeAdView = (NativeAdView) ((Activity)context).getLayoutInflater().inflate(R.layout.layout_native_ad, null);
//                        populateNativeADView(nativeAd, nativeAdView);
//                        AdLayout.removeAllViews();
//                        AdLayout.addView(nativeAdView);
////                        adEmpty.setVisibility(View.GONE);
//                    }
//                });
//
//        AdLoader adLoader = builder.withAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                super.onAdFailedToLoad(loadAdError);
//                Toast.makeText(((Activity)context), loadAdError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }).build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());
//    }
//
//    private void populateNativeADView(NativeAd nativeAd, NativeAdView adView)
//    {
//        adView.setMediaView(adView.findViewById(R.id.ad_media));
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
//        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
//        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
//
//        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
//        // check before trying to display them.
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        // This method tells the Google Mobile Ads SDK that you have finished populating your
//        // native ad view with this native ad.
//        adView.setNativeAd(nativeAd);
//    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 2;
        return showShimmerCartParent ? SHIMMER_ITEM_NUMBER : parentCartList.size();

//        return productList == null ? 0 : Integer.MAX_VALUE;
    }


    public class CardHolder extends RecyclerView.ViewHolder {
        TextView productName, productPrice, productQuantity, editBtn;
        RecyclerView optionsCartRecycler;
        ShimmerFrameLayout shimmerFrameLayoutParentCart;


        @RequiresApi(api = Build.VERSION_CODES.O)
        public CardHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayoutParentCart = itemView.findViewById(R.id.shimmerLayoutParentCart);
            productQuantity = itemView.findViewById(R.id.cart_quantity);
            productPrice = itemView.findViewById(R.id.product_price);
            productName = itemView.findViewById(R.id.product_name);
            optionsCartRecycler = itemView.findViewById(R.id.options_child_recycler);
            editBtn = itemView.findViewById(R.id.edit_btn);

        }
    }

}