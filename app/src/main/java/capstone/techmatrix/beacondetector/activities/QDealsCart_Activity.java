package capstone.techmatrix.beacondetector.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import capstone.techmatrix.beacondetector.QDeals_MainActivity;
import capstone.techmatrix.beacondetector.R;
import capstone.techmatrix.beacondetector.adapters.QdshopcartAdapter;
import capstone.techmatrix.beacondetector.database.DB_Handler;
import capstone.techmatrix.beacondetector.database.SessionManager;
import capstone.techmatrix.beacondetector.pojo.Cart;
import capstone.techmatrix.beacondetector.utils.Constants;

import java.text.DecimalFormat;
import java.util.List;


public class QDealsCart_Activity extends AppCompatActivity implements QdshopcartAdapter.UpdatePayableAmount, QdshopcartAdapter.MonitorListItems {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qdeals_user_shopping_cart);

        // Set Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set Title
        TextView titleToolbar = findViewById(R.id.titleToolbar);
        titleToolbar.setText(R.string.shopping_cart);

        // Hide Cart Icon
        ImageView cart = findViewById(R.id.cart);
        cart.setVisibility(View.GONE);






    }



    // finish activity if cart empty
    @Override
    public void finishActivity(List<Cart> shoppingCart) {
        try {
            if (shoppingCart.size() == 0) {
                overridePendingTransition(0,0);
                finish();
            }
        } catch (Exception e) {
            overridePendingTransition(0,0);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }
}
