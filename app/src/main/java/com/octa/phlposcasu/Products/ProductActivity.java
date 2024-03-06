package com.octa.phlposcasu.Products;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.octa.phlposcasu.AsyncTask.AddCategory.AsyncTaskAddCategory;
import com.octa.phlposcasu.AsyncTask.AddCategory.AsyncTaskAddProductCategory;
import com.octa.phlposcasu.MainActivity;
import com.octa.phlposcasu.Options.OptionsActivity;
import com.octa.phlposcasu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    static String adminID = "";
    static String employeeID = "";

    NavigationView navigationMenu;
    DrawerLayout drawerLayout;
    CardView navigationBtn;
    private DatabaseReference databaseReference;
    RecyclerView categoryRecycler, productRecycler;
    private RelativeLayout addCategoryBtn;

    private static final int GALLERY_REQUEST_CODE = 1;
    AlertDialog alertAddProduct;
    String productImageUrl;

    View layoutEditProduct;
    TextView productNameTxt, productPriceTxt, editBtn, addProductOptionsBtn;
    ImageView productImage;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        initTransparentSystem();
        initOnGets();

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("CustomerPOS").child(adminID);

        initViews();

        new AsyncTaskAddCategory(ProductActivity.this, categoryRecycler, databaseReference,
                layoutEditProduct, productRecycler, adminID, productNameTxt, productPriceTxt, editBtn,
                addProductOptionsBtn, productImage).execute();

        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        navigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1:
                    Intent dashTab = new Intent(ProductActivity.this, MainActivity.class);
                    dashTab.putExtra("adminID", adminID);
                    dashTab.putExtra("employeeID", employeeID);
                    startActivity(dashTab);
                    finishAfterTransition();
                    break;

                case R.id.menu2:

                    break;

                case R.id.menu3:
                    drawerLayout.close();
                    break;

                case R.id.menu4:
                    Intent optionsTab = new Intent(ProductActivity.this, OptionsActivity.class);
                    optionsTab.putExtra("adminID", adminID);
                    optionsTab.putExtra("employeeID", employeeID);
                    startActivity(optionsTab);
                    finishAfterTransition();
                    break;
            }
            return false;
        });

        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (this != null)
                {
                    final AlertDialog builderAddress = new AlertDialog.Builder(ProductActivity.this, R.style.AlertDialogTheme).create();
                    View viewAddress = getLayoutInflater().inflate(R.layout.dialog_add_selection, null);
                    RelativeLayout addCategoryBtn = viewAddress.findViewById(R.id.add_category_btn);
                    RelativeLayout addProductBtn = viewAddress.findViewById(R.id.add_product_btn);
                    RelativeLayout cancelBtn = viewAddress.findViewById(R.id.cancel_btn);

                    addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builderAddress.dismiss();
                            if (builderAddress != null)
                            {
                                final AlertDialog alertAddCategory = new AlertDialog.Builder(ProductActivity.this, R.style.AlertDialogTheme).create();
                                View viewAddress = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
                                TextInputEditText categoryNameEdt = viewAddress.findViewById(R.id.category_name_edt);
                                RelativeLayout addCategoryBtn = viewAddress.findViewById(R.id.add_category_btn);


                                addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Map<String, Object> singleton = new HashMap<>();
                                        singleton.put("categoryId", categoryNameEdt.getText().toString());
                                        singleton.put("checked", false);
                                        databaseReference.child("Categories")
                                                .child(categoryNameEdt.getText().toString())
                                                .updateChildren(singleton)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        alertAddCategory.dismiss();
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

                    addProductBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            builderAddress.dismiss();
                            if (builderAddress != null)
                            {
                                String productId = databaseReference.push().getKey();

                                alertAddProduct = new AlertDialog.Builder(ProductActivity.this, R.style.AlertDialogTheme).create();
                                View viewAddress = getLayoutInflater().inflate(R.layout.dialog_add_product, null);
                                TextInputEditText productNameEdt = viewAddress.findViewById(R.id.product_name_edt);
                                TextInputEditText productPriceEdt = viewAddress.findViewById(R.id.price_edt);
                                TextInputEditText productQuantityEdt = viewAddress.findViewById(R.id.qty_edt);
                                RelativeLayout selectCategoryBtn = viewAddress.findViewById(R.id.select_category_btn);

                                CardView addImageBtn = viewAddress.findViewById(R.id.add_image_btn);

                                addImageBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                                    }
                                });

                                selectCategoryBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        String productName = productNameEdt.getText().toString();
                                        double productPrice = Double.parseDouble(productPriceEdt.getText().toString());
                                        int productQuantity = Integer.parseInt(productQuantityEdt.getText().toString());

                                        initCategorySelection(alertAddProduct, productName, productPrice, productQuantity);

                                    }
                                });

                                View decorView = alertAddProduct.getWindow().getDecorView();
                                int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
                                decorView.setSystemUiVisibility(flags);

                                alertAddProduct.setView(viewAddress);
                                if (alertAddProduct.getWindow() != null) {
                                    alertAddProduct.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                }
                                alertAddProduct.setCancelable(true);
                                alertAddProduct.show();



                            }
                        }
                    });

                    cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builderAddress.cancel();
                        }
                    });

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


    }

    private void initTransparentSystem()
    {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);
    }

    private void initCategorySelection(AlertDialog alertAddProduct, String productName, double productPrice, int productQuantity)
    {
        alertAddProduct.dismiss();
        if (alertAddProduct != null)
        {
            String productId = databaseReference.push().getKey();

            final AlertDialog alertAddProductCategory = new AlertDialog.Builder(ProductActivity.this, R.style.AlertDialogTheme).create();
            View viewAddress = getLayoutInflater().inflate(R.layout.dialog_add_product_category_selection, null);
            RecyclerView categoryRecycler = viewAddress.findViewById(R.id.category_recycler);
            RelativeLayout saveBtn = viewAddress.findViewById(R.id.save_btn);


            new AsyncTaskAddProductCategory(alertAddProductCategory.getContext(), categoryRecycler,
                    databaseReference, productName, productPrice, productQuantity, saveBtn, productImageUrl, productId,
                    adminID).execute();


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

    private void initOnGets()
    {
        adminID = getIntent().getStringExtra("adminID");
        employeeID = getIntent().getStringExtra("employeeID");
    }

    private void initViews()
    {
        navigationMenu = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationBtn = findViewById(R.id.navigation_btn);

        categoryRecycler = findViewById(R.id.categoryAddRecycler);
        addCategoryBtn = findViewById(R.id.add_category_btn);
        productRecycler = findViewById(R.id.product_recycler);

        layoutEditProduct = findViewById(R.id.layout_edit_product);
        productNameTxt = layoutEditProduct.findViewById(R.id.product_name);
        productPriceTxt = layoutEditProduct.findViewById(R.id.product_price);
        editBtn = layoutEditProduct.findViewById(R.id.edit_btn);
        addProductOptionsBtn = layoutEditProduct.findViewById(R.id.add_product_options_btn);
        productImage = layoutEditProduct.findViewById(R.id.product_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            ImageView productImage = alertAddProduct.findViewById(R.id.productImage); // Update the ImageView reference
            productImage.setImageURI(selectedImageUri);
            productImageUrl = selectedImageUri.toString();
        }
    }
}