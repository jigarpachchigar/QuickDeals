package capstone.techmatrix.beacondetector.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Product;
import capstone.techmatrix.beacondetector.pojo.Variant;
import capstone.techmatrix.beacondetector.utils.Constants;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;


public class QDealsProdDetails_Activity extends AppCompatActivity {

    Product product;
    DB_Handler db_handler;

    String selectedSize = null;
    String selectedColor = null;
    String selectedItemPrice = null;
    int selectedItemQuantity = 1;
    int selectedItemVariantId = 0;
    String userEmail = null;

    LinearLayout colorParentLay, sizeParentLay;
    FlowLayout colorsLay, sizeLay;
    TextView price, quantityValue;
    ImageView minus, plus;
    Button cart, buyNow;
    SessionManager sessionManager;
    int cartCount = 0;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_prod_details);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Hide Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setVisibility(View.GONE);

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Get User Email
        sessionManager = new SessionManager(this);
        userEmail = sessionManager.getSessionData(Constants.SESSION_EMAIL);

        // Get Product Id
        int id = getIntent().getIntExtra("ProductId", 0);


    }



    // Set Toolbar Icons Click Listeners
    private void setToolbarIconsClickListeners() {
        ImageView cart = findViewById(R.id.cart);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartCount > 0) {
                    startActivity(new Intent(getApplicationContext(), QDealsCart_Activity.class));
                } else {
                    Toast.makeText(getApplicationContext(), R.string.cart_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    // Get Item Adding To Cart Status
    private boolean isSuccessAddingToCart(boolean isBuyNow) {
        try {
            // Get Selected Item Price
            if (selectedSize.equals("-") || selectedSize != null) {
                if (selectedColor != null) {
                    long result = db_handler.insertIntoCart(product.getId(), selectedItemVariantId, selectedItemQuantity, userEmail);
                    if (result > 0 || isBuyNow) {
                        return true;
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.item_exists, Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.color_select, Toast.LENGTH_LONG).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), R.string.size_select, Toast.LENGTH_LONG).show();
            return false;
        }
    }






    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
