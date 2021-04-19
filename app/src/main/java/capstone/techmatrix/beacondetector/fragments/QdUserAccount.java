package capstone.techmatrix.beacondetector.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import capstone.techmatrix.beacondetector.QDeals_SplashActivity;
import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.activities.QDealsOrders_Activity;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.pojo.User;
import capstone.techmatrix.beacondetector.utils.Constants;

public class QdUserAccount extends Fragment {

    TextView name, email, mobile;
    RelativeLayout orders, logoutLay;
    FinishActivity finishActivityCallback;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qdeals_user_account, container, false);

        // Get User

        User user = db_handler.getUser(sessionManager.getSessionData(Constants.SESSION_EMAIL));


        return view;
    }



}
