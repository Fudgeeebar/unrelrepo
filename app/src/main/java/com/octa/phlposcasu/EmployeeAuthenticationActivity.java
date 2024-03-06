package com.octa.phlposcasu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeAuthenticationActivity extends AppCompatActivity {

    RelativeLayout signInButton;
    EditText code1, code2, code3, code4, code5;
    String userPassWordString;
    FirebaseAuth mAuth;
    DocumentReference documentReference;
    String adminID;

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
        setContentView(R.layout.activity_employee_authentication);

        initTransparentSystem();

        initOnGets();
        documentReference = FirebaseFirestore.getInstance().collection("Restaurants").document(adminID);

        mAuth=FirebaseAuth.getInstance();

        initViews();


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
//                signInProgressBar.setVisibility(View.VISIBLE);
                userPassWordString = code1.getText()
                        .toString()+code2.getText().toString()+code3.getText().toString()+code4.getText().toString()+code5.getText().toString()+"0";
                documentReference.collection("Employees").document(userPassWordString).get().addOnSuccessListener(EmployeeAuthenticationActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists())
                        {
                            Intent i =new Intent(EmployeeAuthenticationActivity.this, MainActivity.class);
                            i.putExtra("adminID", adminID);
                            i.putExtra("employeeID", userPassWordString);
                            startActivity(i);
                            finish();
//                                                loadingDialog.dismiss();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                        else
                        {
                            Toast.makeText(EmployeeAuthenticationActivity.this, "You've entered an invalid code.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private void initOnGets()
    {
        adminID = getIntent().getStringExtra("adminID");
    }

    private void initViews()
    {
        signInButton = findViewById(R.id.play_btn);
        code1 = findViewById(R.id.code_1);
        code2 = findViewById(R.id.code_2);
        code3 = findViewById(R.id.code_3);
        code4 = findViewById(R.id.code_4);
        code5 = findViewById(R.id.code_5);
    }

    private void initTransparentSystem()
    {
        View decorView = getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(flags);
    }
}