package capstone.techmatrix.beacondetector;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;


import capstone.techmatrix.beacondetector.fragments.QdUserSignIn;
import capstone.techmatrix.beacondetector.fragments.QdUserSignup;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.service.SyncDBService;
import capstone.techmatrix.beacondetector.utils.Constants;

public class QDeals_SplashActivity extends AppCompatActivity implements FinishActivity {

    DB_Handler db_handler;
    Button signIn, signUp;
    Handler handler;
    TableLayout bottomLay;
    Snackbar snackbar = null;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_activity_splash);

        setIds();
        setClickListeners();
    }

    // Set Ids
    private void setIds() {
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup);
        bottomLay = findViewById(R.id.bottomLay);
        coordinatorLayout = findViewById(R.id.coordinatorLay);
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign In
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.fragment, new QdUserSignIn());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.fragment, new QdUserSignup());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }



}
