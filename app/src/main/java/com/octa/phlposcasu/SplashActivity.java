package com.octa.phlposcasu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.octa.phlposcasu.Utils.UserUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore mFireStore;
    static int counter=0;
    static int duration=2000;

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
        setContentView(R.layout.activity_splash);

        initTransparentSystem();

        mAuth=FirebaseAuth.getInstance();
        mFireStore=FirebaseFirestore.getInstance();


        progress();
        start();

    }

    public void progress()
    {
        final Timer timer=new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                counter++;
                if(counter>=2000)
                {
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,100);
    }

    public void start()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mAuth.getCurrentUser()!=null)
                {
//                    uId=mAuth.getCurrentUser().getUid();
//                    documentReference=mFireStore.collection("Users")
//                            .document(uId);
//                    documentReference.get().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                            String email = documentSnapshot.getString("email");
//
//                            if (email.equals("SampleUser@gmail.com"))
//                            {
//                                Intent drawingView = new Intent(SplashActivity.this, MainGActivity.class);
//                                drawingView.putExtra("Case_number", 3);
//                                startActivity(drawingView);
//                                finish();
//                            }
//                            else
//                            {
//                                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
//                                    @Override
//                                    public void onSuccess(String s) {
//                                        UserUtils.updateToken(SplashActivity.this, s);
//                                    }
//                                });
//                            }
//
//
//                        }
//                    }).addOnFailureListener(SplashActivity.this, new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            if(e instanceof FirebaseNetworkException)
//                            {
//                                Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
//                                Toast.makeText(getApplicationContext(),"Error while fetching the data",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                    FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            String adminID = mAuth.getCurrentUser().getUid();
                            UserUtils.updateToken(SplashActivity.this, s, adminID);
                        }
                    });
                }
                else
                {
                    Intent intentToSignInActivity=new Intent(SplashActivity.this,AdminLoginActivity.class);
                    startActivity(intentToSignInActivity);
                    finish();
                }
            }
        },duration);
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