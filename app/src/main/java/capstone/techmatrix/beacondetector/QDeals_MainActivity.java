package capstone.techmatrix.beacondetector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import capstone.techmatrix.beacondetector.activities.QDealsCart_Activity;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.fragments.QdProducts;
import capstone.techmatrix.beacondetector.interfaces.FinishActivity;
import capstone.techmatrix.beacondetector.interfaces.ShowBackButton;
import capstone.techmatrix.beacondetector.interfaces.ToolbarTitle;
import capstone.techmatrix.beacondetector.pojo.Category;
import capstone.techmatrix.beacondetector.utils.Constants;

import java.io.Serializable;
import java.util.List;

public class QDeals_MainActivity extends AppCompatActivity implements ToolbarTitle, ShowBackButton, FinishActivity {


    SessionManager sessionManager;
    Toolbar toolbar;
    TextView titleToolbar;
    int cartCount = 0;
    List<Category> childCategories;
    ImageView backButton;
    String subCategoryTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_activity_main);

        sessionManager = new SessionManager(this);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.TitleHome);

        // Back Button
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backButtonClick();
            }
        });

        // initialize bottom navigation view
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        callProductsFragment();
        setToolbarIconsClickListeners();
    }

    // Set Toolbar Icons Click Listeners
    private void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), QDealsCart_Activity.class));
                    overridePendingTransition(0,0);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // call products fragment
    private void callProductsFragment() {

        Bundle args = new Bundle();
        args.putInt(Constants.CAT_ID_KEY, 0);

        QdProducts products = new QdProducts();
        products.setArguments(args);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.content, products, "HOME");
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Update Cart Count
        cartCount = db_handler.getCartItemCount(sessionManager.getSessionData(Constants.SESSION_EMAIL));
        TextView count = findViewById(R.id.count);
        if (cartCount > 0) {
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(cartCount));
        } else {
            count.setVisibility(View.GONE);
        }
    }

    // Back Button Click
    private void backButtonClick() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            if (fragmentManager.findFragmentByTag(Constants.FRAG_PDT).isVisible()) {
                // add bundle arguments
                Bundle bundle = new Bundle();
                bundle.putString(Constants.TITLE,subCategoryTitle);
                bundle.putSerializable(Constants.CAT_KEY, (Serializable) childCategories);

                QdProdSubCategory subcategories = new QdProdSubCategory();
                subcategories.setArguments(bundle);

                fragmentTransaction.replace(R.id.content, subcategories,Constants.FRAG_SUBCAT);
                fragmentTransaction.commit();
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            if (fragmentManager.findFragmentByTag(Constants.FRAG_SUBCAT).isVisible()) {
                fragmentTransaction.replace(R.id.content, new QdProdCategory());
                fragmentTransaction.commit();
                titleToolbar.setText(R.string.TitleCategories);
                backButton.setVisibility(View.INVISIBLE);
            }
        } catch (NullPointerException e) {
            super.onBackPressed();
        }
    }


    @Override
    public void onBackPressed() {
        backButtonClick();
    }

    // Set Toolbar Title
    @Override
    public void setToolbarTitle(String toolbarTitle) {
        titleToolbar.setText(toolbarTitle);
    }

    // show back button
    @Override
    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    // Save Subcategory Title - Need for backButtonClick method
    @Override
    public void saveSubcategoryTitle(String toolbaTitle) {
        subCategoryTitle = toolbaTitle;
    }

    // Finish Activity From Fragment
    @Override
    public void finishActivity() {
        overridePendingTransition(0,0);
        finish();
    }
}
