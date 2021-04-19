package capstone.techmatrix.beacondetector.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import capstone.techmatrix.beacondetector.QDeals_MainActivity;
import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;


public class QdUserSignIn extends Fragment {

    Button signIn;
    EditText email, password;
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
        View view = inflater.inflate(R.layout.qdeals_user_signin, container, false);

        setIds(view);
        setClickListeners();

        return view;
    }

    // Set Ids
    private void setIds(View view) {
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        signIn = view.findViewById(R.id.signin);
        back = view.findViewById(R.id.back);
        showpassword = view.findViewById(R.id.showpassword);
    }

    // Set Click Listeners
    private void setClickListeners() {
        // Sign In
        signIn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View view) {

                // Set Values To User Model
                User user = new User();
                user.setEmail(email.getText().toString());
                user.setPassword(password.getText().toString());

                // Validate Fields
                if (user.getEmail().trim().length() > 0) {
                    if (Util.isValidEmail(user.getEmail())) {
                        if (user.getPassword().trim().length() > 0) {

                            // Sign In User
                            DB_Handler db_handler = new DB_Handler(getActivity());
                            user = db_handler.getUser(user.getEmail());
                            try {
                                if (user.getEmail().trim().length() > 0) {
                                    // Save Session
                                    SessionManager sessionManager = new SessionManager(getActivity());
                                    sessionManager.saveSession(Constants.SESSION_EMAIL, user.getEmail());
                                    sessionManager.saveSession(Constants.SESSION_PASSWORD, user.getPassword());

                                    // Load Main Activity
                                    Intent i = new Intent(getActivity(), QDeals_MainActivity.class);
                                    startActivity(i);
                                    finishActivityCallback.finishActivity();
                                } else {
                                    showInvalidUser();
                                }
                            } catch (NullPointerException e) {
                                showInvalidUser();
                            }

                        } else {
                            showErrorToast(getActivity().getResources().getString(R.string.password));
                        }

                    } else {
                        showErrorToastEmailNotValid();
                    }
                } else {
                    showErrorToast(getActivity().getResources().getString(R.string.email));
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


    }


}
