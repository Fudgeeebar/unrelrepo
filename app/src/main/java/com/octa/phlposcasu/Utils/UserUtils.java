package com.octa.phlposcasu.Utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.octa.phlposcasu.EmployeeAuthenticationActivity;
import com.octa.phlposcasu.MainActivity;


public class UserUtils {
    public static void updateToken(Context context, String token, String adminID) {

        Intent i = new Intent(context, EmployeeAuthenticationActivity.class);
        i.putExtra("adminID", adminID);
        context.startActivity(i);
        ((Activity)context).finish();
    }
}
