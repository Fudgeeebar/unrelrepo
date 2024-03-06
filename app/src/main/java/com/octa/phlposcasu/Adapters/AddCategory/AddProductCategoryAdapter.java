package com.octa.phlposcasu.Adapters.AddCategory;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

import android.content.Context;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.octa.phlposcasu.Models.AddCategory.AddCategoryModel;
import com.octa.phlposcasu.Models.AddCategory.AddProductCategoryModel;
import com.octa.phlposcasu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProductCategoryAdapter extends RecyclerView.Adapter<AddProductCategoryAdapter.CardHolder> {

    Context context;
    FirebaseFirestore firebaseFirestore;
    private List<AddProductCategoryModel> addproductcategoryList;
    public boolean showShimmerCategory = true;

    RecyclerView categoryRecycler;


    DatabaseReference databaseReference;
    String productName;
    double productPrice;
    int productQuantity;
    RelativeLayout saveBtn;

    private List<String> selectedCategories;

    String productImageUrl, productId, adminID;
    public AddProductCategoryAdapter(Context context, List<AddProductCategoryModel> addproductcategoryList,
                                     RecyclerView categoryRecycler, DatabaseReference databaseReference,
                                     String productName, double productPrice, int productQuantity,
                                     RelativeLayout saveBtn, String productImageUrl, String productId,
                                     String adminID)
    {
        this.context = context;
        this.addproductcategoryList = addproductcategoryList;
        this.categoryRecycler = categoryRecycler;
        this.databaseReference = databaseReference;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.saveBtn = saveBtn;
        this.selectedCategories = new ArrayList<>();
        this.productImageUrl = productImageUrl;
        this.productId = productId;
        this.adminID = adminID;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product_category_add_card, parent, false);
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

            AddProductCategoryModel model = addproductcategoryList.get(position);

            holder.categoryName.setText(model.getCategoryId());


            holder.categoryName.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedCategories.add(model.getCategoryId());
                } else {
                    selectedCategories.remove(model.getCategoryId());
                }
            });

            saveBtn.setOnClickListener(view -> saveData());
            
        }
    }

    private void saveData()
    {
        if (selectedCategories.isEmpty()) {
            Toast.makeText(context, "Please select at least one category", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            if (productImageUrl != null)
            {
                // Upload the image to Firebase Storage
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imageRef = storageRef.child("Merchants").child(adminID).child("ProductImages/"+productId+".jpg");
                UploadTask uploadTask = imageRef.putFile(Uri.parse(productImageUrl));

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Image upload success, get the download URL
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                // Store the imageURL in Firebase Realtime Database
                                for (String category : selectedCategories) {
                                    Map<String, Object> productData = new HashMap<>();
                                    productData.put("productCategory", category);
                                    productData.put("productId", productId);
                                    productData.put("productName", productName);
                                    productData.put("productQuantity", productQuantity);
                                    productData.put("productPrice", productPrice);
                                    productData.put("productImage", downloadUrl);

                                    databaseReference
                                            .child("Products")
                                            .child(category)
                                            .child(productId)
                                            .updateChildren(productData)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    clearSelection();
                                                    Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, "Failed to save data", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Image upload failed
//                        Toast.makeText(getApplicationContext(), "Image upload failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                // No image selected, show error message
//                Toast.makeText(getApplicationContext(), "Please select an image first", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void clearSelection()
    {
        selectedCategories.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        int SHIMMER_ITEM_NUMBER = 10;
        return showShimmerCategory ? SHIMMER_ITEM_NUMBER : addproductcategoryList.size();

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