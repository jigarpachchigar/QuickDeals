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


import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.fragments.QdUserSignIn;
import capstone.techmatrix.beacondetector.fragments.QdUserSignup;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.service.SyncDBService;
import capstone.techmatrix.beacondetector.utils.Constants;



public class QDeals_SplashActivity extends AppCompatActivity implements FinishActivity {

    Button signIn, signUp;
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


    // Check Session
    private void checkSession() {
        SessionManager sessionManager = new SessionManager(this);
        if (sessionManager.getSessionData(Constants.SESSION_EMAIL) != null && sessionManager.getSessionData(Constants.SESSION_EMAIL).trim().length() > 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadNextActivity();
                }
            }, 1000);
        } else {
            bottomLay.setVisibility(View.VISIBLE);
        }
    }

    // Load Next Activity
    private void loadNextActivity() {
        Intent i = new Intent(getApplicationContext(), QDeals_MainActivity.class);
        startActivity(i);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void finishActivity() {
        overridePendingTransition(0, 0);
        finish();
    }
}
