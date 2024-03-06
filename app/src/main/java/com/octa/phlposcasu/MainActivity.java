package com.octa.phlposcasu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.octa.phlposcasu.Adapters.CategoryAdapter;
import com.octa.phlposcasu.AsyncTask.AsyncTaskCategory;
import com.octa.phlposcasu.AsyncTask.AsyncTaskParentCart;
import com.octa.phlposcasu.Models.CategoryModel;
import com.octa.phlposcasu.Options.OptionsActivity;
import com.octa.phlposcasu.Products.ProductActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    NavigationView navigationMenu;
    DrawerLayout drawerLayout;
    CardView navigationBtn;
    static String adminID = "";
    static String employeeID = "";
    private DocumentReference documentReference;
    private DatabaseReference databaseReference;
    private FirebaseFirestore firebaseFirestore;

    private RecyclerView categoryRecycler, productRecycler, cartParentRecycler, optionsParentRecycler;

    private LinearLayoutManager categoryLinearLayoutManager;
    private List<CategoryModel> categoryList;
//    private List<ProductModel> productList;

    private CategoryAdapter categoryAdapter;
//    private ProductAdapter productAdapter;

    View productView, cartView;
    ImageView productImage;
    TextView productName, productPrice, cartLayoutTextView;

    RelativeLayout checkOutBtn, addToCartBtn, cancelBtn;

    String key;
    String paymentMethod = "Cash";

    TextView orderCartId, orderCartSubTotal, orderCartTotal, paymentPreference;

    ConstraintLayout paymentBtn;

    private boolean preferences = false;

    @Override
    protected void onStart() {
        super.onStart();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                categoryAdapter.showShimmerCategory = false;
//                categoryAdapter.notifyDataSetChanged();
//            }
//        },1000);
    }

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
        setContentView(R.layout.activity_main);

        initTransparentSystem();
        initOnGets();
        initViews();

        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = FirebaseFirestore.getInstance().collection("Restaurants").document(adminID);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("CustomerPOS").child(adminID);

        key = databaseReference.push().getKey();


        initArrayList();
        initLayoutManager();


//        Toast.makeText(this, adminID+"\n"+employeeID, Toast.LENGTH_SHORT).show();

        initHeader();


        navigationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        navigationMenu.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu1:
                    drawerLayout.close();
                    break;

                case R.id.menu2:

                    break;

                case R.id.menu3:
                    Intent productTab = new Intent(MainActivity.this, ProductActivity.class);
                    productTab.putExtra("adminID", adminID);
                    productTab.putExtra("employeeID", employeeID);
                    startActivity(productTab);
                    finishAfterTransition();
                    break;

                case R.id.menu4:
                    Intent optionsTab = new Intent(MainActivity.this, OptionsActivity.class);
                    optionsTab.putExtra("adminID", adminID);
                    optionsTab.putExtra("employeeID", employeeID);
                    startActivity(optionsTab);
                    finishAfterTransition();
                    break;
            }
            return false;
        });
    }

    private void initLayoutManager()
    {
//        SnapHelper snapHelper = new LinearSnapHelper();
//        categoryAdapter = new CategoryAdapter(this, categoryList,productRecycler, firebaseFirestore, productName, productImage,
//                productView, productPrice, optionsParentRecycler, addToCartBtn, cartView, key, databaseReference);
//
//        categoryLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        categoryRecycler.setLayoutManager(categoryLinearLayoutManager);
//        categoryRecycler.getRecycledViewPool().setMaxRecycledViews(0, 0);
//        snapHelper.attachToRecyclerView(categoryRecycler);
//        categoryRecycler.smoothScrollBy(1, 0);
    }

    private void initArrayList()
    {
//        categoryList = new ArrayList<>();
    }

    private void initHeader()
    {
        documentReference.collection("Employees").document(employeeID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error)
            {
                String employeeName = documentSnapshot.getString("firstName");
                cartLayoutTextView.setText(employeeName);

            }
        });

        new AsyncTaskCategory(MainActivity.this, cartParentRecycler, databaseReference,
                key, orderCartId, orderCartSubTotal, orderCartTotal, checkOutBtn, paymentMethod,
                preferences, paymentBtn, paymentPreference, categoryRecycler, productRecycler,
                firebaseFirestore, productName, productImage, productView, productPrice,
                optionsParentRecycler, addToCartBtn, cartView, adminID).execute();
    }

    private void initOnGets()
    {
        adminID = getIntent().getStringExtra("adminID");
        employeeID = getIntent().getStringExtra("employeeID");
    }

    private void initTransparentSystem()
    {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);
    }

    private void initViews()
    {
        navigationMenu = findViewById(R.id.nav_menu);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationBtn = findViewById(R.id.navigation_btn);

        productView = findViewById(R.id.layout_product);
        cartView = findViewById(R.id.layout_cart);

        categoryRecycler = findViewById(R.id.category_recycler);
        productRecycler = findViewById(R.id.product_recycler);

        productImage = productView.findViewById(R.id.product_image);
        productName = productView.findViewById(R.id.product_name);
        productPrice = productView.findViewById(R.id.product_price);
        optionsParentRecycler = productView.findViewById(R.id.options_parent_recycler);
        addToCartBtn = productView.findViewById(R.id.add_to_cart_btn);
        cancelBtn = productView.findViewById(R.id.cancel_btn);

        cartParentRecycler = cartView.findViewById(R.id.cart_recycler);
        orderCartId = cartView.findViewById(R.id.order_id_txt);
        orderCartSubTotal = cartView.findViewById(R.id.sub_total_txt);
        orderCartTotal = cartView.findViewById(R.id.total_txt);
        checkOutBtn = cartView.findViewById(R.id.check_out_btn);
        paymentBtn = cartView.findViewById(R.id.payment_btn);
        paymentPreference = cartView.findViewById(R.id.payment_preference);
        cartLayoutTextView = cartView.findViewById(R.id.textView3);

    }
}