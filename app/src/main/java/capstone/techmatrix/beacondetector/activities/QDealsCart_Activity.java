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

        // Back Button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Get Cart Items
        final SessionManager sessionManager = new SessionManager(this);
        final DB_Handler db_handler = new DB_Handler(this);
        final List<Cart> shoppingCart = db_handler.getCartItems(sessionManager.getSessionData(Constants.SESSION_EMAIL));

        // Fill ListView With Items
        ListView listView = findViewById(R.id.listview);
        listView.setAdapter(new QdshopcartAdapter(this, shoppingCart));

        setPayableAmount(shoppingCart);

        // Order Button Click
        Button placeOrder = findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete from cart and place order
                db_handler.deleteCartItems();
                db_handler.insertOrderHistory(shoppingCart,sessionManager.getSessionData(Constants.SESSION_EMAIL));
                Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();

                // Call Main Activity
                Intent intent = new Intent(getApplicationContext(), QDeals_MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |  Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
            }
        });
    }

    // Calculate Payable Amount
    @SuppressLint("SetTextI18n")
    private void setPayableAmount(List<Cart> shoppingCart) {
        Double totalAmount = 0.0;
        for (int i = 0; i < shoppingCart.size(); i++) {
            int itemQuantity = shoppingCart.get(i).getItemQuantity();
            Double tax = shoppingCart.get(i).getProduct().getTax().getValue();
            Double price = Double.valueOf(shoppingCart.get(i).getVariant().getPrice());
            price = (price + tax) * itemQuantity;
            totalAmount = totalAmount + price;
        }

        // Set Value
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        TextView payable = findViewById(R.id.payableAmt);
        payable.setText("CAD "+formatter.format(totalAmount));
    }

    // update payable amount
    @Override
    public void updatePayableAmount(List<Cart> shoppingCart) {
        setPayableAmount(shoppingCart);
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
