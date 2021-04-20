package capstone.techmatrix.beacondetector.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import capstone.techmatrix.beacondetector.QDeals_MainActivity;
import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.pojo.User;
import capstone.techmatrix.beacondetector.utils.Constants;
import capstone.techmatrix.beacondetector.utils.Util;


public class QdUserSignup extends Fragment {

    EditText name, email, password, mobile;
    Button signUp;
    ImageView back, showpassword;
    boolean isPasswordShown = false;
    FinishActivity finishActivityCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        finishActivityCallback = (FinishActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.qdeals_sign_up, container, false);

        setIds(view);
        setClickListeners();

        return view;
    }

    // Set Ids
    private void setIds(View view) {
        name = view.findViewById(R.id.name);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        mobile = view.findViewById(R.id.mobile);
        signUp = view.findViewById(R.id.signup);
        back = view.findViewById(R.id.back);
        showpassword = view.findViewById(R.id.showpassword);
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign Up
        signUp.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {

                // Set Values To User Model
                User user = new User();
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                user.setMobile(mobile.getText().toString());
                user.setPassword(password.getText().toString());

                // Validate Fields
                if (user.getName().trim().length() > 0) {
                    if (user.getEmail().trim().length() > 0) {
                        if (Util.isValidEmail(user.getEmail())) {
                            if (user.getMobile().trim().length() > 0) {
                                if (user.getPassword().trim().length() > 0) {

                                    // Register User
                                    DB_Handler db_handler = new DB_Handler(getActivity());
                                    long isInserted = db_handler.registerUser(user.getName(), user.getEmail(), user.getMobile(), user.getPassword());
                                    if (isInserted != -1) {
                                        // Save Session
                                        SessionManager sessionManager = new SessionManager(getActivity());
                                        sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                                        sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getPassword());

                                        // Load Main Activity
                                        Intent i = new Intent(getActivity(), QDeals_MainActivity.class);
                                        startActivity(i);
                                        finishActivityCallback.finishActivity();
                                    } else {
                                        showErrorToastEmailExists();
                                    }

                                } else {
                                    showErrorToast(getActivity().getResources().getString(R.string.password));
                                }
                            } else {
                                showErrorToast(getActivity().getResources().getString(R.string.mobile));
                            }
                        } else {
                            showErrorToastEmailNotValid();
                        }
                    } else {
                        showErrorToast(getActivity().getResources().getString(R.string.email));
                    }
                } else {
                    showErrorToast(getActivity().getResources().getString(R.string.name));
                }
            }
        });

        // Back Button Click
        back.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                ft.replace(R.id.fragment, new QdEmptyFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        // Show Password
        showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordShown) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    showpassword.setImageResource(R.drawable.ic_eye_off_grey600_24dp);
                    isPasswordShown = false;
                } else {
                    password.setTransformationMethod(null);
                    showpassword.setImageResource(R.drawable.ic_eye_white_24dp);
                    isPasswordShown = true;
                }
            }
        });
    }

    // Show Error Toast
    private void showErrorToast(String value) {
        Toast.makeText(getActivity(), value + getResources().getString(R.string.BlankError), Toast.LENGTH_LONG).show();
    }

    // Show Error Toast - Email Not Valid
    private void showErrorToastEmailNotValid() {
        Toast.makeText(getActivity(), R.string.EmailError, Toast.LENGTH_LONG).show();
    }

    // Show Error Toast - Email Exists
    private void showErrorToastEmailExists() {
        Toast.makeText(getActivity(), R.string.EmailExistsError, Toast.LENGTH_LONG).show();
    }
}
